package com.tokopedia.tkpd.utils;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String path) {
        super(path);
    }
}
