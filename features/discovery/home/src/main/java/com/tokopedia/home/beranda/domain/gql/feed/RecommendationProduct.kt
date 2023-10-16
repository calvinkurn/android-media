package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecommendationProduct(
    @SerializedName("pageName")
    @Expose
    val pageName: String = "",
    @SerializedName("layoutName")
    @Expose
    val layoutName: String = "",
    @SerializedName("product", alternate = ["products"])
    @Expose
    val product: List<Product> = listOf(),
    @SerializedName("banner", alternate = ["banners"])
    @Expose
    val banners: List<Banner> = listOf(),
    @SerializedName("position", alternate = ["positions"])
    @Expose
    val layoutTypes: List<LayoutType> = listOf(),
    @SerializedName("has_next_page", alternate = ["hasNextPage"])
    @Expose
    val hasNextPage: Boolean = false
)
