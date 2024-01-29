package com.tokopedia.recommendation_widget_common.widget.foryou

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetModel
import com.tokopedia.recommendation_widget_common.widget.foryou.play.PlayWidgetViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationGridViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationListViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationListener
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationUtil.getLayout
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsViewHolder

class ForYouRecommendationTypeFactoryImpl constructor(
    private val listener: HomeRecommendationListener,
    private val topAdsListener: BannerTopAdsViewHolder.Listener,
    private val playListener: PlayWidgetViewHolder.Listener,
    private val bannerListener: BannerRecommendationViewHolder.Listener,
    private val entityListener: RecomEntityCardViewHolder.Listener
) : BaseAdapterTypeFactory(), ForYouRecommendationTypeFactory {

    override fun type(model: HomeRecommendationModel) = model.getLayout()

    override fun type(model: BannerTopAdsModel) = BannerTopAdsViewHolder.LAYOUT

    override fun type(model: PlayWidgetModel) = PlayWidgetViewHolder.LAYOUT

    override fun type(model: BannerRecommendationModel) = BannerRecommendationViewHolder.LAYOUT

    override fun type(model: RecomEntityModel) = RecomEntityCardViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            HomeRecommendationGridViewHolder.LAYOUT -> HomeRecommendationGridViewHolder(parent, listener)
            HomeRecommendationListViewHolder.LAYOUT -> HomeRecommendationListViewHolder(parent, listener)
            BannerTopAdsViewHolder.LAYOUT -> BannerTopAdsViewHolder(parent, topAdsListener)
            PlayWidgetViewHolder.LAYOUT -> PlayWidgetViewHolder(parent, playListener)
            BannerRecommendationViewHolder.LAYOUT -> BannerRecommendationViewHolder(parent, bannerListener)
            RecomEntityCardViewHolder.LAYOUT -> RecomEntityCardViewHolder(parent, entityListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
