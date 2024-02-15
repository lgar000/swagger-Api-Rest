package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product product= new Product(productDto);
        productsService.save(product);
        URI createdProductUri = URI.create("");
        return ResponseEntity.created(createdProductUri).body(null);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productsService.all());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Optional<Product> product= productsService.findById(id);
        if(product.isEmpty()){
             throw new ProductNotFoundException(id);
        }else{
            return ResponseEntity.ok(product.get());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody ProductDto productDto) {
        Optional<Product> product= productsService.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException(id);
        }else{
            Product productActual= product.get();
            productActual.update(productDto);
            Product updateProduct= productsService.save(productActual);
            return ResponseEntity.ok(null);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        Optional<Product> product=productsService.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException(id);
        }else{
            productsService.deleteById(id);
            return ResponseEntity.ok().build();
        }

    }
}
