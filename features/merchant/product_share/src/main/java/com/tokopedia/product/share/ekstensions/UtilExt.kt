package com.tokopedia.product.share.ekstensions

import com.tokopedia.product.share.ProductData

fun ProductData.getShareContent(shortUrl: String): String{
    var content = ""
    if (!productName.isNullOrEmpty()){
        content = "Temukan $productName seharga $priceText hanya di Tokopedia!\n"
    }
    return "$content$shortUrl"
}