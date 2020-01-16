package com.tokopedia.product.manage.item.catalog.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.Catalog
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata.Category

interface ProductEditCategoryView: CustomerView{
    fun onSuccessLoadRecommendationCategory(categories: List<Category>)
    fun onErrorLoadRecommendationCategory(throwable: Throwable?)
    fun populateCategory(strings: List<String>, categoryId: Long)
    fun onErrorLoadCatalog(errorMessage: String?)
    fun onSuccessLoadCatalog(keyword: String, departmentId: Long, catalogs: List<Catalog>)

}