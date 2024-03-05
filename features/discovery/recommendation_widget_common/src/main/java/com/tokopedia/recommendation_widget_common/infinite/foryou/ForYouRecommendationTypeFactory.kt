package com.tokopedia.recommendation_widget_common.infinite.foryou

import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.HeadlineTopAdsModel

interface ForYouRecommendationTypeFactory {

    fun type(model: PlayCardModel): Int
    fun type(model: ContentCardModel): Int
    fun type(model: RecommendationCardModel): Int

    fun type(model: BannerTopAdsModel): Int

    fun type(model: RetryButtonStateModel): Int
    fun type(model: ShimmeringStateModel): Int
    fun type(model: LoadMoreStateModel): Int
    fun type(model: ErrorStateModel): Int
    fun type(model: EmptyStateModel): Int

    // deprecated yet temporary needs for fixes unresolved resources for old experiment.
    fun type(model: BannerRecommendationModel): Int
    fun type(model: BannerOldTopAdsModel): Int
    fun type(model: HeadlineTopAdsModel): Int
}
