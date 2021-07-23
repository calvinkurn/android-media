package com.tokopedia.product.detail.common.data.model.pdplayout

data class BestSellerInfoContent(
    val productID: String = "",
    val content: String = "",
    val linkText: String = "",
    var color: String = "",
    var applink: String = "",
    val separator: String = "",
    var icon: String = "",
    val isVisible: Boolean = true
)