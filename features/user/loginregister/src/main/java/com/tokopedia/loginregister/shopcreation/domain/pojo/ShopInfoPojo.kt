package com.tokopedia.loginregister.shopcreation.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2020-02-06.
 * ade.hadian@tokopedia.com
 */

data class ShopInfoPojo(
        @SerializedName("shopInfoByID")
        @Expose
        var data: ShopInfoByID = ShopInfoByID()
)

data class ShopInfoByID(
        @SerializedName("result")
        @Expose
        var result: List<TokoShopData> = listOf()
)

data class TokoShopData(
        @SerializedName("shippingLoc")
        @Expose
        var shippingLoc: ShippingLocData = ShippingLocData()
)

data class ShippingLocData(
        @SerializedName("provinceID")
        @Expose
        var provinceID: Int = 0
)