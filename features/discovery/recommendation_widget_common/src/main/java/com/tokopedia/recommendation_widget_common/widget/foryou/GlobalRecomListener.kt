package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsListener

interface GlobalRecomListener : ParentRecommendationListener,
    BannerTopAdsListener,
    PlayWidgetViewHolder.Listener,
    PlayVideoWidgetView.Listener,
    TemporaryBannerRecommendationListener,
    RecomEntityCardViewHolder.Listener

interface TemporaryBannerRecommendationListener {
    fun onBannerClick(model: BannerRecommendationModel)
    fun onBannerImpression(model: BannerRecommendationModel)
}
