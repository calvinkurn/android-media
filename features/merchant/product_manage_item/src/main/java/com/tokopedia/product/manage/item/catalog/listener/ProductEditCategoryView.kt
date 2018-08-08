package com.tokopedia.product.manage.item.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.manage.item.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.manage.item.view.model.categoryrecomm.ProductCategoryPredictionViewModel

interface ProductEditCategoryView: CustomerView{
    fun onSuccessLoadRecommendationCategory(categories: List<ProductCategoryPredictionViewModel>)
    fun onErrorLoadRecommendationCategory(throwable: Throwable?)
    fun populateCategory(strings: List<String>)
    fun onErrorLoadCatalog(errorMessage: String?)
    fun onSuccessLoadCatalog(keyword: String, departmentId: Long, catalogs: List<Catalog>)

}