package com.tokopedia.inbox.universalinbox.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuItemViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuSeparatorViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationLoaderViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationProductViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationTitleViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationWidgetViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsBannerViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsHeadlineViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxWidgetMetaViewHolder
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationWidgetUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.user.session.UserSessionInterface

class UniversalInboxTypeFactoryImpl(
    private val userSession: UserSessionInterface,
    private val widgetListener: UniversalInboxWidgetListener,
    private val menuListener: UniversalInboxMenuListener,
    private val tdnBannerResponseListener: TdnBannerResponseListener,
    private val topAdsClickListener: TopAdsImageViewClickListener,
    private val recommendationListener: RecommendationListener
) : BaseAdapterTypeFactory(), UniversalInboxTypeFactory {

    override fun type(uiModel: UniversalInboxMenuSeparatorUiModel): Int {
        return UniversalInboxMenuSeparatorViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxMenuUiModel): Int {
        return UniversalInboxMenuItemViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel): Int {
        return UniversalInboxRecommendationLoaderViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxRecommendationTitleUiModel): Int {
        return UniversalInboxRecommendationTitleViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxTopAdsBannerUiModel): Int {
        return UniversalInboxTopAdsBannerViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxTopadsHeadlineUiModel): Int {
        return UniversalInboxTopAdsHeadlineViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxWidgetMetaUiModel): Int {
        return UniversalInboxWidgetMetaViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxRecommendationUiModel): Int {
        return UniversalInboxRecommendationProductViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalInboxRecommendationWidgetUiModel): Int {
        return UniversalInboxRecommendationWidgetViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            UniversalInboxMenuSeparatorViewHolder.LAYOUT -> {
                UniversalInboxMenuSeparatorViewHolder(parent)
            }
            UniversalInboxMenuItemViewHolder.LAYOUT -> {
                UniversalInboxMenuItemViewHolder(parent, menuListener)
            }
            UniversalInboxRecommendationLoaderViewHolder.LAYOUT -> {
                UniversalInboxRecommendationLoaderViewHolder(parent)
            }
            UniversalInboxRecommendationTitleViewHolder.LAYOUT -> {
                UniversalInboxRecommendationTitleViewHolder(parent)
            }
            UniversalInboxTopAdsBannerViewHolder.LAYOUT -> {
                UniversalInboxTopAdsBannerViewHolder(
                    parent,
                    tdnBannerResponseListener,
                    topAdsClickListener
                )
            }
            UniversalInboxTopAdsHeadlineViewHolder.LAYOUT -> {
                UniversalInboxTopAdsHeadlineViewHolder(parent, userSession)
            }
            UniversalInboxWidgetMetaViewHolder.LAYOUT -> {
                UniversalInboxWidgetMetaViewHolder(parent, widgetListener)
            }
            UniversalInboxRecommendationProductViewHolder.LAYOUT -> {
                UniversalInboxRecommendationProductViewHolder(parent, recommendationListener)
            }
            UniversalInboxRecommendationWidgetViewHolder.LAYOUT -> {
                UniversalInboxRecommendationWidgetViewHolder(parent)
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}
