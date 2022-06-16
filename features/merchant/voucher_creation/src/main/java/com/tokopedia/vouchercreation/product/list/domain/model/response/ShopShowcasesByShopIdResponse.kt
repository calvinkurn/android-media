package com.tokopedia.vouchercreation.product.list.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopShowcasesByShopIdResponse(
        @SerializedName("shopShowcasesByShopID")
        @Expose val shopShowcasesByShopId: ShopShowcasesByShopID
)

data class ShopShowcasesByShopID(
        @SerializedName("result")
        @Expose val result: List<ShopShowcase>,
        @SerializedName("error")
        @Expose val error: Error
)

data class Error(
        @SerializedName("message")
        @Expose val message: String
)

data class ShopShowcase(
        @SerializedName("id")
        @Expose val id: String,
        @SerializedName("name")
        @Expose val name: String
)