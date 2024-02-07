package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.ContentCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsListener

interface GlobalRecomListener : ParentRecommendationListener,
    BannerTopAdsListener,
    PlayCardViewHolder.Listener,
    PlayVideoWidgetView.Listener,
    TemporaryBannerRecommendationListener,
    ContentCardViewHolder.Listener

@TemporaryBackwardCompatible
interface TemporaryBannerRecommendationListener {
    fun onBannerImpressed(model: BannerRecommendationModel)
    fun onBannerClicked(model: BannerRecommendationModel)
}
