package com.tokopedia.loginregister.shopcreation.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2020-02-06.
 * ade.hadian@tokopedia.com
 */

data class ShopInfoPojo(
    @SerializedName("shopInfoByID")
    var data: ShopInfoByID = ShopInfoByID()
)

data class ShopInfoByID(
    @SerializedName("result")
    var result: List<TokoShopData> = listOf()
)

data class TokoShopData(
    @SerializedName("shippingLoc")
    var shippingLoc: ShippingLocData = ShippingLocData()
)

data class ShippingLocData(
    @SerializedName("provinceID")
    var provinceID: Int = 0
)
