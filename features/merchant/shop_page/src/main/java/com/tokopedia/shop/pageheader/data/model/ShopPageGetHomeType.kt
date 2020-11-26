package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPageGetHomeType(
        @SerializedName("shopHomeType")
        @Expose
        val shopHomeType: String = ""
) {

    data class Response(
            @SerializedName("shopPageGetHomeType")
            val shopPageGetHomeType: ShopPageGetHomeType = ShopPageGetHomeType()
    )
}