package com.tokopedia.inbox.universalinbox.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.inbox.universalinbox.data.response.counter.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuItemDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuSectionDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuSeparatorDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationLoaderDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationTitleDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxTopAdsBannerDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxTopAdsHeadlineDelegate
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.user.session.UserSessionInterface

class UniversalInboxAdapter(
    userSession: UserSessionInterface,
    menuListener: UniversalInboxMenuListener,
    tdnBannerResponseListener: TdnBannerResponseListener,
    topAdsClickListener: TopAdsImageViewClickListener,
    recommendationListener: RecommendationListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(UniversalInboxMenuSectionDelegate())
        delegatesManager.addDelegate(UniversalInboxMenuItemDelegate(menuListener))
        delegatesManager.addDelegate(
            UniversalInboxTopAdsBannerDelegate(tdnBannerResponseListener, topAdsClickListener)
        )
        delegatesManager.addDelegate(UniversalInboxRecommendationTitleDelegate())
        delegatesManager.addDelegate(UniversalInboxRecommendationDelegate(recommendationListener))
        delegatesManager.addDelegate(UniversalInboxRecommendationLoaderDelegate())
        delegatesManager.addDelegate(UniversalInboxMenuSeparatorDelegate())
        delegatesManager.addDelegate(UniversalInboxTopAdsHeadlineDelegate(userSession))
    }

    private var recommendationViewType: Int? = null
    private var recommendationFirstPosition: Int? = null

    fun getProductRecommendationViewType(): Int? {
        if (recommendationViewType != null) {
            return recommendationViewType
        } else {
            itemList.forEachIndexed { index, item ->
                if (item is RecommendationItem) {
                    recommendationViewType = getItemViewType(index)
                    return recommendationViewType
                }
            }
            return null
        }
    }

    fun getProductRecommendationFirstPosition(): Int? {
        if (recommendationFirstPosition != null) {
            return recommendationFirstPosition
        } else {
            itemList.forEachIndexed { index, item ->
                if (item is RecommendationItem) {
                    recommendationFirstPosition = index
                    return recommendationFirstPosition
                }
            }
            return null
        }
    }

    fun isRecommendationLoader(position: Int): Boolean {
        return itemList[position]::class == UniversalInboxRecommendationLoaderUiModel::class
    }

    fun getFirstTopAdsBannerPositionPair(): Pair<Int, UniversalInboxTopAdsBannerUiModel>? {
        itemList.forEachIndexed { index, item ->
            if (item is UniversalInboxTopAdsBannerUiModel) {
                return Pair(index, item)
            }
        }
        return null
    }

    fun updateAllCounters(counterData: UniversalInboxAllCounterResponse): List<Int> {
        val listIndex = arrayListOf<Int>()
        itemList.forEachIndexed { index, item ->
            if (item is UniversalInboxMenuUiModel) {
                when (item.type) {
                    MenuItemType.CHAT_BUYER -> {
                        item.counter = counterData.chatUnread.unreadBuyer
                    }
                    MenuItemType.CHAT_SELLER -> {
                        item.counter = counterData.chatUnread.unreadSeller
                    }
                    MenuItemType.DISCUSSION -> {
                        item.counter = counterData.othersUnread.discussionUnread
                    }
                    MenuItemType.REVIEW -> {
                        item.counter = counterData.othersUnread.reviewUnread
                    }
                }
                listIndex.add(index)
            } else if (item is UniversalInboxMenuSeparatorUiModel) { // Not in static menu anymore
                return@forEachIndexed
            }
        }
        return listIndex
    }
}
