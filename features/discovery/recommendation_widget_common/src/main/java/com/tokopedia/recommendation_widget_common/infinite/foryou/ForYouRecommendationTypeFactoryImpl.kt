package com.tokopedia.recommendation_widget_common.infinite.foryou

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayVideoWidgetManager
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardGridViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardListViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.HomeRecommendationUtil.getLayout
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder.EmptyStateViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder.ErrorStateViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder.LoadMoreStateViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder.RetryButtonStateViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.viewholder.ShimmeringStateViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model.HeadlineTopAdsModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.viewholder.BannerOldTopAdsViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.viewholder.BannerTopAdsViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.viewholder.HeadlineTopAdsViewHolder

class ForYouRecommendationTypeFactoryImpl constructor(
    private val listener: GlobalRecomListener,
    private val playVideoWidgetManager: PlayVideoWidgetManager
) : BaseAdapterTypeFactory(), ForYouRecommendationTypeFactory {

    override fun type(model: RecommendationCardModel) = model.getLayout()

    override fun type(model: BannerTopAdsModel) = BannerTopAdsViewHolder.LAYOUT

    override fun type(model: PlayCardModel) = PlayCardViewHolder.LAYOUT

    override fun type(model: ContentCardModel) = ContentCardViewHolder.LAYOUT

    override fun type(model: RetryButtonStateModel) = RetryButtonStateViewHolder.LAYOUT

    override fun type(model: ShimmeringStateModel) = ShimmeringStateViewHolder.LAYOUT

    override fun type(model: LoadMoreStateModel) = LoadMoreStateViewHolder.LAYOUT

    override fun type(model: ErrorStateModel) = ErrorStateViewHolder.LAYOUT

    override fun type(model: EmptyStateModel) = EmptyViewHolder.LAYOUT

    override fun type(model: BannerRecommendationModel) = BannerRecommendationViewHolder.LAYOUT

    override fun type(model: BannerOldTopAdsModel) = BannerOldTopAdsViewHolder.LAYOUT

    override fun type(model: HeadlineTopAdsModel) = HeadlineTopAdsViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // components
            RecommendationCardGridViewHolder.LAYOUT -> RecommendationCardGridViewHolder(parent, listener)
            RecommendationCardListViewHolder.LAYOUT -> RecommendationCardListViewHolder(parent, listener)
            PlayCardViewHolder.LAYOUT -> PlayCardViewHolder(parent, playVideoWidgetManager, listener, listener)
            ContentCardViewHolder.LAYOUT -> ContentCardViewHolder(parent, listener)

            // topads
            BannerTopAdsViewHolder.LAYOUT -> BannerTopAdsViewHolder(parent, listener)

            // states
            RetryButtonStateViewHolder.LAYOUT -> RetryButtonStateViewHolder(parent, listener)
            ShimmeringStateViewHolder.LAYOUT -> ShimmeringStateViewHolder(parent)
            LoadMoreStateViewHolder.LAYOUT -> LoadMoreStateViewHolder(parent)
            EmptyViewHolder.LAYOUT -> EmptyStateViewHolder(parent)
            ErrorStateViewHolder.LAYOUT -> ErrorStateViewHolder(parent, listener)

            // deprecated, it will be removed next cycle
            BannerRecommendationViewHolder.LAYOUT -> BannerRecommendationViewHolder(parent, listener)
            BannerOldTopAdsViewHolder.LAYOUT -> BannerOldTopAdsViewHolder(parent, listener)
            HeadlineTopAdsViewHolder.LAYOUT -> HeadlineTopAdsViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
