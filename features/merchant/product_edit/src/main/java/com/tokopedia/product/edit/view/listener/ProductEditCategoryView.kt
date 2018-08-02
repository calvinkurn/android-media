package com.tokopedia.product.edit.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.edit.view.model.categoryrecomm.CategoryRecommViewModel

interface ProductEditCategoryView: CustomerView{
    fun onSuccessLoadRecommendationCategory(categories: CategoryRecommViewModel?)
    fun onErrorLoadRecommendationCategory(throwable: Throwable?)

}