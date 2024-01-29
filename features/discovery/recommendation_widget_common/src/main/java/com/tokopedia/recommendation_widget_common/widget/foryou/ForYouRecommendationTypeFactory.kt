package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsModel

interface ForYouRecommendationTypeFactory {

    fun type(model: BannerTopAdsModel): Int
    fun type(model: BannerOldTopAdsModel): Int
    fun type(model: PlayWidgetModel): Int
    fun type(model: HomeRecommendationModel): Int
    fun type(model: BannerRecommendationModel): Int
    fun type(model: RecomEntityModel): Int
}
