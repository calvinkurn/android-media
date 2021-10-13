package com.tokopedia.mvcwidget.multishopmvc.data

data class MultiShopModel(
    val shopIcon: String = "",
    val shopName: String = "",
    val cashBackTitle: String = "",
    val cashBackValue: String = "",
    val couponCount: String = "",
    val couponProdFirst: String = "",
    val couponProdLast: String = "",
    val applink: String = "",
    val id:String="",
    val products: List<ProductsItem?>? = null,
    val AdInfo: AdInfo? = null,
)