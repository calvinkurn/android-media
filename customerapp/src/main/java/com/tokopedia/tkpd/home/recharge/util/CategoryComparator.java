package com.tokopedia.tkpd.home.recharge.util;



import com.tokopedia.core.database.model.category.Category;

import java.util.Comparator;

/**
 * Created by Alifa on 11/28/2016.
 */

public class CategoryComparator implements Comparator<Category> {

    @Override
    public int compare(Category c1, Category c2) {
        return c1.getAttributes().getWeight().compareTo(c2.getAttributes().getWeight());
    }

}