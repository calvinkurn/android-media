package com.tokopedia.homenav.mainnav.data.pojo.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoPojo(
        @SerializedName("info")
        val info: Info = Info()
) {

    data class Response(
            @SerializedName("userShopInfo")
            val userShopInfo: ShopInfoPojo = ShopInfoPojo()
    )

    data class Info(
            @SerializedName("shop_name")
            val shopName: String = "",
            @SerializedName("shop_id")
            val shopId: String = ""
    )
}