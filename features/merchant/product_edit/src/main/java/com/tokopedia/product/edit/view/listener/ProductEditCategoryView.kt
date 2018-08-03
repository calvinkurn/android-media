package com.tokopedia.product.edit.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel

interface ProductEditCategoryView: CustomerView{
    fun onSuccessLoadRecommendationCategory(categories: List<ProductCategoryPredictionViewModel>)
    fun onErrorLoadRecommendationCategory(throwable: Throwable?)

}