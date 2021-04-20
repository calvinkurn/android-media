package com.tokopedia.activation.model.response

import com.google.gson.annotations.SerializedName

class UpdateShopFeatureResponse(
        @SerializedName("updateShopFeature")
        var data: UpdateShopFeature = UpdateShopFeature()
)

data class UpdateShopFeature(
        @SerializedName("success")
        var success: Boolean = false,
        @SerializedName("message")
        var message: String = "",
        @SerializedName("createdId")
        var createdId: String = ""
)