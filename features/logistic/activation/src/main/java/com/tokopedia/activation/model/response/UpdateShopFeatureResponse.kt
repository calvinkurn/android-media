package com.tokopedia.activation.model.response

import com.google.gson.annotations.SerializedName

class UpdateShopFeatureResponse(
        @SerializedName("data")
        var data: UpdateShopFeature = UpdateShopFeature()
)

data class UpdateShopFeature(
        @SerializedName("success")
        var success: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("createdId")
        var createdId: String = ""
)