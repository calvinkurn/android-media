package com.tokopedia.recommendation_widget_common.widget.foryou

import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.HeadlineTopAdsModel

interface ForYouRecommendationTypeFactory {

    fun type(model: PlayCardModel): Int
    fun type(model: ContentCardModel): Int
    fun type(model: RecommendationCardModel): Int
    fun type(model: BannerRecommendationModel): Int

    fun type(model: BannerTopAdsModel): Int
    fun type(model: BannerOldTopAdsModel): Int
    fun type(model: HeadlineTopAdsModel): Int

    fun type(model: RetryButtonStateModel): Int
    fun type(model: ShimmeringStateModel): Int
    fun type(model: LoadMoreStateModel): Int
    fun type(model: ErrorStateModel): Int
    fun type(model: EmptyStateModel): Int
}
