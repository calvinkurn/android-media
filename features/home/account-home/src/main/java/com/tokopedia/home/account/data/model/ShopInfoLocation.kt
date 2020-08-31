package com.tokopedia.home.account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoLocation(
        @SerializedName("shopInfoByID")
        @Expose
        val shopInfoByID: ShopInfoByID = ShopInfoByID()
)

data class ShopInfoByID(
        @SerializedName("result")
        @Expose
        val result: List<Result> = listOf()
)

data class Result(
        @SerializedName("shippingLoc")
        @Expose
        val shippingLoc: ShippingLoc = ShippingLoc()
)

data class ShippingLoc(
        @SerializedName("provinceID")
        @Expose
        val provinceID: Int = 0
)