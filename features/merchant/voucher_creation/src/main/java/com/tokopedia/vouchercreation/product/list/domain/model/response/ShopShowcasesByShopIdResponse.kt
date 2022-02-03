package com.tokopedia.vouchercreation.product.list.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopShowcasesByShopIdResponse(
        @SerializedName("ShopShowcasesByShopID")
        @Expose val shopShowcasesByShopID: ShopShowcasesByShopID
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
        @Expose val name: String,
        @SerializedName("count")
        @Expose val count: Long,
        @SerializedName("type")
        @Expose val type: Long,
        @SerializedName("highlighted")
        @Expose val highlighted: Boolean,
        @SerializedName("alias")
        @Expose val alias: String,
        @SerializedName("uri")
        @Expose val uri: String,
        @SerializedName("useAce")
        @Expose val useAce: Boolean,
        @SerializedName("badge")
        @Expose val badge: String,
        @SerializedName("aceDefaultSort")
        @Expose val aceDefaultSort: Long
)