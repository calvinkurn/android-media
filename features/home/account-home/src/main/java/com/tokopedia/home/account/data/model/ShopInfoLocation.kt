package com.tokopedia.home.account.data.model

import com.google.gson.annotations.SerializedName

data class ShopInfoLocation(
        @SerializedName("shopInfoByID")
        val shopInfoByID: ShopInfoByID = ShopInfoByID()
)

data class ShopInfoByID(
        @SerializedName("result")
        val result: List<Result> = listOf()
)

data class Result(
        @SerializedName("shippingLoc")
        val shippingLoc: ShippingLoc = ShippingLoc()
)

data class ShippingLoc(
        @SerializedName("provinceID")
        val provinceID: Int = 0
)