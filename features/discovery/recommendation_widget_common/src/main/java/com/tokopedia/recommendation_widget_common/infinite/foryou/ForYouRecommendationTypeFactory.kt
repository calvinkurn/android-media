package com.tokopedia.recommendation_widget_common.infinite.foryou

import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel

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
}
