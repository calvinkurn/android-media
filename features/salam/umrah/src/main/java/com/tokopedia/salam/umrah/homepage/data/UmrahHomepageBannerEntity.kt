package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

data class UmrahHomepageBannerEntity(
        @SerializedName("umrahBanners")
        @Expose
        val umrahBanners: List<UmrahBanner> = emptyList()
):UmrahHomepageModel(){

        override fun type(typeFactory: UmrahHomepageFactory): Int {
                return typeFactory.type(this)
        }
}

data class UmrahBanner(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("service")
        @Expose
        val service: String = "",
        @SerializedName("location")
        @Expose
        val location: String = "",
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",
        @SerializedName("webUrl")
        @Expose
        val webUrl: String = "",
        @SerializedName("applinkUrl")
        @Expose
        val applinkUrl: String = "",
        @SerializedName("weight")
        @Expose
        val weight: Int = 0,
        @SerializedName("isViewed")
        @Expose
        var isViewed : Boolean = false
        )