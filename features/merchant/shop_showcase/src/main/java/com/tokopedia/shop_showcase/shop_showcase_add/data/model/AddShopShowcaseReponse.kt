package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddShopShowcaseBaseReponse(
        @Expose
        @SerializedName("addShopShowcase") val addShopShowcaseReponse: AddShopShowcaseResponse = AddShopShowcaseResponse()
)

data class AddShopShowcaseResponse(
        @Expose
        @SerializedName("success") val success: Boolean = false,
        @Expose
        @SerializedName("message") val message: String = "",
        @Expose
        @SerializedName("createdId") val createdId: String = ""
)