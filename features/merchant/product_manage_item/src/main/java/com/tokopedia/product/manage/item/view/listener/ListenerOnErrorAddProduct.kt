package com.tokopedia.product.manage.item.view.listener

interface ListenerOnErrorAddProduct {
    fun onErrorName()
    fun onErrorCategoryEmpty()
    fun onErrorEtalase()
    fun onErrorPrice()
    fun onErrorWeight()
    fun onErrorImage()
}