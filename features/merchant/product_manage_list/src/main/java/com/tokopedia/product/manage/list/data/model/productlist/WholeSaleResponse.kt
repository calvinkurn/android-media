package com.tokopedia.product.manage.list.data.model.productlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WholeSaleResponse(
        @SerializedName("minimum_quantity")
        @Expose
        val minimumQuantity: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Int = 0
)