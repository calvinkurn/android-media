package com.tokopedia.inbox.universalinbox.view.uiState

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CHATBOT_TYPE
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

data class UniversalInboxMenuUiState(
    val isLoading: Boolean = false,
    val widgetMeta: UniversalInboxWidgetMetaUiModel = UniversalInboxWidgetMetaUiModel(),
    val menuList: List<Visitable<in UniversalInboxTypeFactory>> = listOf(),
    val miscList: List<Visitable<in UniversalInboxTypeFactory>> = listOf(),
    val notificationCounter: String = "0",
    val shouldTrackImpression: Boolean = false
) {
    fun getBuyerUnread(): Int {
        return (menuList as List<UniversalInboxMenuUiModel>).find {
            it.type == MenuItemType.CHAT_BUYER
        }?.counter ?: 0
    }

    fun getSellerUnread(): Int {
        return (menuList as List<UniversalInboxMenuUiModel>).find {
            it.type == MenuItemType.CHAT_SELLER
        }?.counter ?: 0
    }

    fun getDiscussionUnread(): Int {
        return (menuList as List<UniversalInboxMenuUiModel>).find {
            it.type == MenuItemType.DISCUSSION
        }?.counter ?: 0
    }

    fun getReviewUnread(): Int {
        return (menuList as List<UniversalInboxMenuUiModel>).find {
            it.type == MenuItemType.REVIEW
        }?.counter ?: 0
    }

    fun getHelpUnread(): Int? {
        return widgetMeta.widgetList.find {
            it.type == CHATBOT_TYPE
        }?.counter
    }
}
