package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateRequestResult (
    @SerializedName("shopID")
    @Expose
    val shopID:String,

    @SerializedName("status")
    @Expose
    val status:Int,

    @SerializedName("success")
    @Expose
    val success:Boolean,

    @SerializedName("requestTime")
    @Expose
    val requestTime:String
)