package com.tokopedia.recharge_slice.data

import android.os.Parcel
import android.os.Parcelable

data class TrackingData(
        var products : List<Product> = emptyList()
)

data class Product(
        var product_id : String = "",
        var product_name : String = "",
        var product_price : String = ""
)