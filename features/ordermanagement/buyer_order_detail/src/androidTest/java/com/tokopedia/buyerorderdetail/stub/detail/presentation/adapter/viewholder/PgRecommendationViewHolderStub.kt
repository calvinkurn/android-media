package com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PgRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData

class PgRecommendationViewHolderStub(
    itemView: View,
    buyerOrderDetailBindRecomWidgetListener: BuyerOrderDetailBindRecomWidgetListener
) : PgRecommendationViewHolder(itemView, buyerOrderDetailBindRecomWidgetListener) {
    override fun bind(element: PGRecommendationWidgetUiModel) {}

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {}

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {}

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {}

    override fun onRecomProductCardImpressed(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {}

    override fun onRecomProductCardClicked(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        applink: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {}

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {}

    override fun onRecomBannerClicked(
        data: RecommendationCarouselData,
        applink: String,
        adapterPosition: Int
    ) {}

    override fun onChannelWidgetEmpty() {}

    override fun onWidgetFail(pageName: String, e: Throwable) {}
}
