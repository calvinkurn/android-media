package com.tokopedia.product.manage.item.catalog.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog

interface ProductEditCatalogPickerView: CustomerView{
    fun onErrorLoadCatalog(throwable: Throwable?)
    fun onSuccessLoadCatalog(catalogs: List<ProductCatalog>, hasNextData: Boolean)
}