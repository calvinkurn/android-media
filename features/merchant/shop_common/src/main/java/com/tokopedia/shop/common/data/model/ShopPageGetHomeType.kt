package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPageGetHomeType(
        @SerializedName("shopHomeType")
        @Expose
        val shopHomeType: String = "",

        @SerializedName("homeLayoutData")
        @Expose
        val homeLayoutData: HomeLayoutData = HomeLayoutData()
) {

    data class Response(
            @SerializedName("shopPageGetHomeType")
            val shopPageGetHomeType: ShopPageGetHomeType = ShopPageGetHomeType()
    )

}