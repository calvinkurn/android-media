package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateShopShowcaseBaseResponse(
        @Expose
        @SerializedName("updateShopShowcase") val updateShopShowcaseResponse: UpdateShopShowcaseResponse = UpdateShopShowcaseResponse()
)

data class UpdateShopShowcaseResponse(
        @Expose
        @SerializedName("success") val success: Boolean? = false,
        @Expose
        @SerializedName("message") val message: String? = ""
)