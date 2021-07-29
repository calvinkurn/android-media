package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SearchCategoryRecommendationCarouselListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView

class RecommendationCarouselViewHolder(
        itemView: View,
        private val recommendationCarouselListener: SearchCategoryRecommendationCarouselListener,
): AbstractViewHolder<RecommendationCarouselDataView>(itemView),
        RecommendationCarouselWidgetListener {

    companion object {
        @LayoutRes
        @JvmStatic
        val LAYOUT = R.layout.item_tokopedianow_search_category_recom_carousel
    }

    private val recommendationCarouselWidgetView =
            itemView.findViewById<RecommendationCarouselWidgetView?>(
                    R.id.tokoNowSearchCategoryRecomCarousel
            )

    override fun bind(element: RecommendationCarouselDataView?) {
        element ?: return
        val recomWidget = recommendationCarouselWidgetView ?: return
        val scrollToPosition =
                recommendationCarouselListener.onGetCarouselScrollPosition(adapterPosition)

        recomWidget.bind(
                carouselData = element.carouselData,
                adapterPosition = adapterPosition,
                widgetListener = this,
                scrollToPosition = scrollToPosition,
        )
        recommendationCarouselListener.onBindRecommendationCarousel(element, adapterPosition)
    }

    override fun onRecomProductCardImpressed(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int,
    ) {
        recommendationCarouselListener.onImpressedRecommendationCarouselItem(
                data = data,
                recomItem = recomItem,
                itemPosition = itemPosition,
                adapterPosition = adapterPosition
        )
    }

    override fun onRecomProductCardClicked(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            applink: String,
            itemPosition: Int,
            adapterPosition: Int,
    ) {
        recommendationCarouselListener.onClickRecommendationCarouselItem(
                data = data,
                recomItem = recomItem,
                itemPosition = itemPosition,
                adapterPosition = adapterPosition
        )
    }

    override fun onRecomProductCardAddToCartNonVariant(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            adapterPosition: Int,
            quantity: Int,
    ) {
        saveCarouselScrollPosition()

        val recommendationCarouselPosition = this.adapterPosition

        recommendationCarouselListener.onATCNonVariantRecommendationCarouselItem(
                data = data,
                recomItem = recomItem,
                recommendationCarouselPosition = recommendationCarouselPosition,
                quantity = quantity,
        )
    }

    private fun saveCarouselScrollPosition() {
        val adapterPosition = this.adapterPosition
        val carouselScrollPosition =
                recommendationCarouselWidgetView?.getCurrentPosition() ?: 0

        recommendationCarouselListener.onSaveCarouselScrollPosition(
                adapterPosition = adapterPosition,
                scrollPosition = carouselScrollPosition,
        )
    }

    override fun onRecomProductCardAddVariantClick(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            adapterPosition: Int,
    ) {
        recommendationCarouselListener.onAddVariantRecommendationCarouselItem(
                data = data,
                recomItem = recomItem,
        )
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {

    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {

    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {

    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {

    }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) {

    }

    override fun onChannelWidgetEmpty() {

    }

    override fun onViewRecycled() {
        saveCarouselScrollPosition()
        super.onViewRecycled()
    }
}