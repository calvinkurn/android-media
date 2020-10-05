package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecommendationProduct (
        @SerializedName("pageName")
        @Expose
        val pageName: String = "",
        @SerializedName("product")
        @Expose
        val product: List<Product> = listOf(),
        @SerializedName("banner")
        @Expose
        val banners: List<Banner> = listOf(),
        @SerializedName("position")
        @Expose
        val layoutTypes: List<LayoutType> = listOf(),
        @SerializedName("has_next_page")
        @Expose
        val hasNextPage: Boolean = false
)