package com.tokopedia.discovery.categorynav.domain.model;

import java.util.ArrayList;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavDomainModel {

    private ArrayList<Category> categories = new ArrayList<>();

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Category getCategoryIndexById (String departmentId) {
        for (Category category: categories) {
            if (category.getId().equals(departmentId)) return category;
        }
        return null;
     }
}
