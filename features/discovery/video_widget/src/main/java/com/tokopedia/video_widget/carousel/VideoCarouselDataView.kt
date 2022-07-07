package com.tokopedia.video_widget.carousel

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder

data class VideoCarouselDataView(
    val title: String = "",
    val url: String = "",
    val applink: String = "",
    val bannerImageUrl: String = "",
    val bannerLinkUrl: String = "",
    val bannerApplinkUrl: String = "",
    val identifier: String = "",
    val inspirationCarouselType: String = "",
    val layout: String = "",
    val position: Int = 0,
    val carouselTitle: String = "",
    val optionPosition: Int = 0,
    var isChipsActive: Boolean = false,
    val hexColor: String = "",
    val chipImageUrl: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val dimension90: String = "",
    val itemList: List<VideoItem> = listOf(),
    override val config: VideoCarouselConfigUiModel = VideoCarouselConfigUiModel(),
) : VideoCarouselConfigProvider {

    data class VideoItem(
        val id: String = "",
        val videoUrl: String = "",
        val imageUrl: String = "",
        val title: String ="",
        val subTitle: String = "",
        val applink: String = "",
        val name: String = "",
        val price: Int = 0,
        val priceStr: String = "",
        val rating: Int = 0,
        val countReview: Int = 0,
        val url: String = "",
        val description: List<String> = listOf(),
        val optionPosition: Int = 0,
        val inspirationCarouselType: String = "",
        val ratingAverage: String = "",
        val layout: String = "",
        val originalPrice: String = "",
        val discountPercentage: Int = 0,
        val position: Int = 0,
        val optionTitle: String = "",
        val shopLocation: String = "",
        val shopName: String = "",
        val isOrganicAds: Boolean = false,
        val topAdsViewUrl: String = "",
        val topAdsClickUrl: String = "",
        val topAdsWishlistUrl: String = "",
        val componentId: String = "",
        val inspirationCarouselTitle: String = "",
        val dimension90: String = "",
    ) : ImpressHolder() {
        fun getInspirationCarouselListProductAsObjectDataLayer(): Any {
            return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", "/search - carousel",
                "position", optionPosition,
                "attribution", "none / other"
            )
        }

        fun getInspirationCarouselListProductImpressionAsObjectDataLayer(): Any {
            return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", "/search - carousel",
                "position", optionPosition
            )
        }
    }
}
