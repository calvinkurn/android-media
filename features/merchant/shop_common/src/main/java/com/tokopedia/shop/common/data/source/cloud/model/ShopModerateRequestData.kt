package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateRequestData (

        @SerializedName("shopModerateRequestStatus")
        @Expose
        val shopModerateRequestStatus:ShopModerateRequestStatus

)

data class ShopModerateRequestStatus(

        @SerializedName("result")
        @Expose
        val result:ShopModerateRequestResult,

        @SerializedName("error")
        @Expose
        val error:ShopModerateRequestError
)

data class ShopModerateRequestError(
        @SerializedName("message")
        @Expose
        val message:String
)

data class ShopModerateRequestResult(

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