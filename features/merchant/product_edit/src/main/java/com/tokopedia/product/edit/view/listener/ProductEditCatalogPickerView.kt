package com.tokopedia.product.edit.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.product.edit.price.model.ProductCatalog

interface ProductEditCatalogPickerView: CustomerView{
    fun onErrorLoadCatalog(throwable: Throwable?)
    fun onSuccessLoadCatalog(catalogs: List<ProductCatalog>, hasNextData: Boolean)
}