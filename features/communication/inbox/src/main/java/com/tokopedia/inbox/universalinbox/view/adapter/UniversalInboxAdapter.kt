package com.tokopedia.inbox.universalinbox.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuItemDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuSectionDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuSeparatorDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationLoaderDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationTitleDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxTopAdsBannerDelegate
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener

class UniversalInboxAdapter(
    tdnBannerResponseListener: TdnBannerResponseListener,
    topAdsClickListener: TopAdsImageViewClickListener,
    recommendationListener: RecommendationListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(UniversalInboxMenuSectionDelegate())
        delegatesManager.addDelegate(UniversalInboxMenuItemDelegate())
        delegatesManager.addDelegate(
            UniversalInboxTopAdsBannerDelegate(tdnBannerResponseListener, topAdsClickListener)
        )
        delegatesManager.addDelegate(UniversalInboxRecommendationTitleDelegate())
        delegatesManager.addDelegate(UniversalInboxRecommendationDelegate(recommendationListener))
        delegatesManager.addDelegate(UniversalInboxRecommendationLoaderDelegate())
        delegatesManager.addDelegate(UniversalInboxMenuSeparatorDelegate())
    }

    private val recommendationViewType: Int? = null

    fun getProductRecommendationViewType(): Int? {
        if (recommendationViewType != null) {
            return recommendationViewType
        } else {
            itemList.forEachIndexed { index, item ->
                if (item is RecommendationItem) {
                    return getItemViewType(index)
                }
            }
            return null
        }
    }
}
