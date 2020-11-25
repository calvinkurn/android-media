package com.tokopedia.navigation.presentation.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.navigation.domain.model.Inbox
import com.tokopedia.navigation.domain.model.InboxTopAdsBannerUiModel
import com.tokopedia.navigation.domain.model.RecomTitle
import com.tokopedia.navigation.domain.model.Recommendation
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxTopAdsBannerViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.RecomTitleViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.RecommendationViewHolder
import com.tokopedia.navigation.presentation.view.InboxAdapterListener
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener

/**
 * Author errysuprayogi on 13,March,2019
 */
class InboxAdapterTypeFactory constructor(
        private val listener: InboxAdapterListener,
        private val recommendationListener: RecommendationListener,
        private val topAdsResponseListener: TopAdsImageVieWApiResponseListener,
        private val topAdsClickListener: TopAdsImageViewClickListener
) : BaseAdapterTypeFactory(), InboxTypeFactory {

    override fun type(inbox: Inbox): Int {
        return InboxViewHolder.LAYOUT
    }

    override fun type(recomendation: Recommendation): Int {
        return RecommendationViewHolder.LAYOUT
    }

    override fun type(recomTitle: RecomTitle): Int {
        return RecomTitleViewHolder.LAYOUT
    }

    override fun type(inboxTopAdsBanner: InboxTopAdsBannerUiModel): Int {
        return InboxTopAdsBannerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InboxTopAdsBannerViewHolder.LAYOUT -> InboxTopAdsBannerViewHolder(view, topAdsResponseListener, topAdsClickListener)
            InboxViewHolder.LAYOUT -> InboxViewHolder(view, listener)
            RecommendationViewHolder.LAYOUT -> RecommendationViewHolder(view, recommendationListener)
            RecomTitleViewHolder.LAYOUT -> RecomTitleViewHolder(view)
            LoadingMoreViewHolder.LAYOUT -> object : LoadingMoreViewHolder(view) {
                override fun bind(element: LoadingMoreModel?) {
                }
            }
            else -> super.createViewHolder(view, type)
        }
    }
}
