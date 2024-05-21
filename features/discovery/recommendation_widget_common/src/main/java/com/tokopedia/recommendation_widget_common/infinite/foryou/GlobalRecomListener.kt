package com.tokopedia.recommendation_widget_common.infinite.foryou

import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.infinite.foryou.content.ContentCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.BannerTopAdsListener

interface GlobalRecomListener : ParentRecommendationListener,
    BannerTopAdsListener,
    PlayCardViewHolder.Listener,
    PlayVideoWidgetView.Listener,
    ContentCardViewHolder.Listener
