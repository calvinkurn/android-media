package com.tokopedia.shop.showcase.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rafli Syam on 16/03/2021
 */

data class GetFeaturedShowcaseResponse(
        @SerializedName("getFeaturedShowcase")
        @Expose
        val getFeaturedShowcase: GetFeaturedShowcase?
)

data class GetFeaturedShowcase(
        @SerializedName("result")
        @Expose
        val result: List<ShopFeaturedShowcase> = listOf(),
        @SerializedName("error")
        @Expose
        val error: ShopFeaturedShowcaseError = ShopFeaturedShowcaseError()
)

data class ShopFeaturedShowcase(
        @SerializedName("id")
        @Expose
        val id: String = "0",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("alias")
        @Expose
        val alias: String = "",
        @SerializedName("count")
        @Expose
        val count: Int = 0,
        @SerializedName("uri")
        @Expose
        val uri: String = "",
        @SerializedName("imageURL")
        @Expose
        val imageUrl: String? = ""
)

data class ShopFeaturedShowcaseError(
        @SerializedName("message")
        @Expose
        val errorMessage: String = ""
)