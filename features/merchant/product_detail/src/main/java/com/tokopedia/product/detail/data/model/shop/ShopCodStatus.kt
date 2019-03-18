package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopCodStatus(
        @SerializedName("value")
        @Expose
        val isCod: Boolean = false,

        @SerializedName("title")
        @Expose
        val title: String = ""
){
    data class Data(
            @SerializedName("data")
            @Expose
            val shopCodStatus: ShopCodStatus = ShopCodStatus()
    )

    data class Response(
            @SerializedName("shopFeature")
            @Expose
            val result: Data = Data()
    )
}