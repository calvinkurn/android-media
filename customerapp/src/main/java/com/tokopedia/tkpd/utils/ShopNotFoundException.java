package com.tokopedia.tkpd.utils;

public class ShopNotFoundException extends Exception {

    public ShopNotFoundException(String path) {
        super(path);
    }
}
