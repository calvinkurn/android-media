package com.tokopedia.recharge_slice.data

data class TrackingData(
        var user_id :String = "",
        var products : List<Product> = emptyList()
)

data class Product(
        var product_id : String = "",
        var product_name : String = "",
        var product_price : String = ""
)