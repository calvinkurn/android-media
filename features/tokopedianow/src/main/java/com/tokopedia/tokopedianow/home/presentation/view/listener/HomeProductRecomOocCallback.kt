package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics

class HomeProductRecomOocCallback (
    private val context: Context?,
    private val analytics: HomeAnalytics
): TokoNowRecommendationCarouselListener {

    override fun onImpressedRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        analytics.onImpressProductRecomOoc(
            headerName = data.recommendationData.title,
            recommendationItem = recomItem,
            position = itemPosition
        )
    }

    override fun onClickRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        analytics.onClickProductRecomOoc(
            headerName = data.recommendationData.title,
            recommendationItem = recomItem,
            position = itemPosition
        )

        openAppLink(recomItem.appUrl)
    }

    override fun onSeeMoreClick(
        data: RecommendationCarouselData,
        applink: String
    ) {
        analytics.onClickAllProductRecom(
            channelId = data.recommendationData.channelId,
            headerName = data.recommendationData.title,
            isOoc = true
        )

        RouteManager.route(context, applink)
    }

    override fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int) { /* do nothing */ }

    override fun onGetCarouselScrollPosition(adapterPosition: Int): Int = 0

    override fun onATCNonVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        recommendationCarouselPosition: Int,
        quantity: Int
    ) { /* do nothing */ }

    override fun onAddVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) { /* do nothing */ }

    override fun onBindRecommendationCarousel(
        model: TokoNowProductRecommendationOocUiModel,
        adapterPosition: Int
    ) { /* do noting */ }

    private fun openAppLink(appLink: String) {
        if(appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }

}
