package com.tokopedia.feedplus.browse.data.model

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface WidgetRecommendationModel {

    data class Banners(
        val banners: List<BannerWidgetModel>
    ) : WidgetRecommendationModel

    data class Authors(
        val channels: List<AuthorWidgetModel>
    ) : WidgetRecommendationModel

    object Empty : WidgetRecommendationModel
}
