package com.tokopedia.seller.active.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateShopActiveModel(
        @SerializedName("success")
        @Expose
        val success: Boolean,
        @SerializedName("message")
        @Expose
        val message: String
)