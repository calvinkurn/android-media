package com.tokopedia.product.manage.item.main.base.domain.model;

import java.util.List;

/**
 * Created by Hendry on 5/6/2017.
 */

public class CategoryRecommDomainModel {
    private List<ProductCategoryPredictionDomainModel> productCategoryPrediction = null;

    public List<ProductCategoryPredictionDomainModel> getProductCategoryPrediction() {
        return productCategoryPrediction;
    }

    public void setProductCategoryPrediction(List<ProductCategoryPredictionDomainModel> productCategoryPrediction) {
        this.productCategoryPrediction = productCategoryPrediction;
    }
}
