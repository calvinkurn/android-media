package com.tokopedia.core.common.category.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class CategoryLevelDomainModel {
    private long selectedCategoryId;
    private long parentCategoryId;
    private List<CategoryDomainModel> categoryModels;

    public void setSelectedCategoryId(long categoryId) {
        this.selectedCategoryId = categoryId;
    }

    public long getParentCategoryId() {
        return parentCategoryId;
    }

    public long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setParentCategoryId(long categoryId) {
        this.parentCategoryId = categoryId;
    }

    public void setCategoryModels(List<CategoryDomainModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public List<CategoryDomainModel> getCategoryModels() {
        return categoryModels;
    }
}
