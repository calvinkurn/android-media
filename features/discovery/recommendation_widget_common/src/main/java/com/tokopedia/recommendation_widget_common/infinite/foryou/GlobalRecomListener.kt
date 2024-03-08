package com.tokopedia.recommendation_widget_common.infinite.foryou

import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.BannerTopAdsListener
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.TemporaryBackwardCompatible

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
