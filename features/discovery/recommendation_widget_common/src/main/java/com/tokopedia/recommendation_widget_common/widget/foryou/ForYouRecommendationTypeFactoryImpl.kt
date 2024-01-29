package com.tokopedia.recommendation_widget_common.widget.foryou

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayVideoWidgetManager
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationGridViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationListViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationUtil.getLayout
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder.EmptyStateViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder.ErrorStateViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder.LoadMoreStateViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder.RetryButtonStateViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder.ShimmeringStateViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsListener
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.HeadlineTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.viewholder.BannerOldTopAdsViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.viewholder.BannerTopAdsViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.viewholder.HeadlineTopAdsViewHolder

class ForYouRecommendationTypeFactoryImpl constructor(
    private val listener: ParentRecommendationListener,
    private val topAdsListener: BannerTopAdsListener,
    private val playListener: PlayWidgetViewHolder.Listener,
    private val playWidgetListener: PlayVideoWidgetView.Listener,
    private val bannerListener: BannerRecommendationViewHolder.Listener,
    private val entityListener: RecomEntityCardViewHolder.Listener,
    private val playVideoWidgetManager: PlayVideoWidgetManager
) : BaseAdapterTypeFactory(), ForYouRecommendationTypeFactory {

    override fun type(model: HomeRecommendationModel) = model.getLayout()

    override fun type(model: BannerTopAdsModel) = BannerTopAdsViewHolder.LAYOUT

    override fun type(model: BannerOldTopAdsModel) = BannerOldTopAdsViewHolder.LAYOUT

    override fun type(model: PlayWidgetModel) = PlayWidgetViewHolder.LAYOUT

    override fun type(model: BannerRecommendationModel) = BannerRecommendationViewHolder.LAYOUT

    override fun type(model: RecomEntityModel) = RecomEntityCardViewHolder.LAYOUT

    override fun type(model: RetryButtonStateModel) = RetryButtonStateViewHolder.LAYOUT

    override fun type(model: ShimmeringStateModel) = ShimmeringStateViewHolder.LAYOUT

    override fun type(model: LoadMoreStateModel) = LoadMoreStateViewHolder.LAYOUT

    override fun type(model: ErrorStateModel) = ErrorStateViewHolder.LAYOUT

    override fun type(model: EmptyStateModel) = EmptyViewHolder.LAYOUT

    override fun type(model: HeadlineTopAdsModel) = HeadlineTopAdsViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // components
            HomeRecommendationGridViewHolder.LAYOUT -> HomeRecommendationGridViewHolder(parent, listener)
            HomeRecommendationListViewHolder.LAYOUT -> HomeRecommendationListViewHolder(parent, listener)
            PlayWidgetViewHolder.LAYOUT -> PlayWidgetViewHolder(parent, playVideoWidgetManager, playListener, playWidgetListener)
            BannerRecommendationViewHolder.LAYOUT -> BannerRecommendationViewHolder(parent, bannerListener)
            RecomEntityCardViewHolder.LAYOUT -> RecomEntityCardViewHolder(parent, entityListener)

            // topads
            BannerTopAdsViewHolder.LAYOUT -> BannerTopAdsViewHolder(parent, topAdsListener)
            BannerOldTopAdsViewHolder.LAYOUT -> BannerOldTopAdsViewHolder(parent, topAdsListener)
            HeadlineTopAdsViewHolder.LAYOUT -> HeadlineTopAdsViewHolder(parent, topAdsListener)

            // states
            RetryButtonStateViewHolder.LAYOUT -> RetryButtonStateViewHolder(parent, listener)
            ShimmeringStateViewHolder.LAYOUT -> ShimmeringStateViewHolder(parent)
            LoadMoreStateViewHolder.LAYOUT -> LoadMoreStateViewHolder(parent)
            EmptyViewHolder.LAYOUT -> EmptyStateViewHolder(parent)
            ErrorStateViewHolder.LAYOUT -> ErrorStateViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
