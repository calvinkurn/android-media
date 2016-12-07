package com.tokopedia.tkpd.home.recharge.util;

import com.tokopedia.core.database.recharge.product.Product;

import java.util.Comparator;

/**
 * Created by Alifa on 11/28/2016.
 */

public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p1.getAttributes().getWeight().compareTo(p2.getAttributes().getWeight());
    }

}