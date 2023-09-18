package com.tokopedia.inbox.universalinbox.domain.mapper

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxMenuAndWidgetMetaResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxMenuDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProvider
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_SOURCE_KEY
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_SOURCE_REVIEW_INBOX
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuLabel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UniversalInboxMenuMapper @Inject constructor(
    private val resourceProvider: UniversalInboxResourceProvider
) {

    fun mapToInboxMenu(
        userSession: UserSessionInterface,
        inboxMenuResponse: List<UniversalInboxMenuDataResponse>,
        counterResponse: UniversalInboxAllCounterResponse
    ): List<UniversalInboxMenuUiModel> {
        val result = arrayListOf<UniversalInboxMenuUiModel>()
        inboxMenuResponse.forEach {
            val menuItemType = getType(it.type)
            if (menuItemType == MenuItemType.CHAT_SELLER && !userSession.hasShop()) {
                return@forEach // If user does not have shop, skip chat seller menu
            }
            val uiModel = UniversalInboxMenuUiModel(
                type = menuItemType,
                title = it.title,
                icon = it.icon.toIntOrZero(),
                counter = getCounter(menuItemType, counterResponse),
                applink = it.applink,
                label = UniversalInboxMenuLabel(
                    color = it.label.color,
                    text = it.label.text
                ),
                additionalInfo = getAdditionalInfo(menuItemType, userSession)
            )
            result.add(uiModel)
        }
        return result
    }

    private fun getType(counterType: String): MenuItemType {
        return when (counterType) {
            MenuItemType.CHAT_SELLER.counterType -> MenuItemType.CHAT_SELLER
            MenuItemType.CHAT_BUYER.counterType -> MenuItemType.CHAT_BUYER
            MenuItemType.DISCUSSION.counterType -> MenuItemType.DISCUSSION
            MenuItemType.REVIEW.counterType -> MenuItemType.REVIEW
            else -> MenuItemType.OTHER
        }
    }

    private fun getCounter(
        menuItemType: MenuItemType,
        counterResponse: UniversalInboxAllCounterResponse
    ): Int {
        return when (menuItemType) {
            MenuItemType.CHAT_BUYER -> counterResponse.chatUnread.unreadBuyer
            MenuItemType.CHAT_SELLER -> counterResponse.chatUnread.unreadSeller
            MenuItemType.DISCUSSION -> counterResponse.othersUnread.discussionUnread
            MenuItemType.REVIEW -> counterResponse.othersUnread.reviewUnread
            MenuItemType.OTHER -> Int.ZERO
        }
    }

    private fun getAdditionalInfo(
        menuItemType: MenuItemType,
        userSession: UserSessionInterface
    ): Any? {
        return when (menuItemType) {
            MenuItemType.CHAT_SELLER -> {
                UniversalInboxShopInfoUiModel(
                    avatar = userSession.shopAvatar,
                    shopName = userSession.shopName
                )
            }
            else -> null
        }
    }

    // Edge case - Fallback menu generator
    fun generateFallbackMenu(shouldShowLocalLoad: Boolean): UniversalInboxWrapperResponse {
        val chatSectionList = getFallbackChatSectionList()
        val othersSectionList = getOthersSectionList()
        return UniversalInboxWrapperResponse(
            UniversalInboxMenuAndWidgetMetaResponse(
                inboxMenu = chatSectionList + othersSectionList
            ).apply {
                this.shouldShowLocalLoad = shouldShowLocalLoad
            }
        )
    }

    // Generate Chat Section
    private fun getFallbackChatSectionList(): List<UniversalInboxMenuDataResponse> {
        val chatMenuList = arrayListOf<UniversalInboxMenuDataResponse>()
        // Chat buyer
        chatMenuList.add(
            UniversalInboxMenuDataResponse(
                icon = IconUnify.CHAT_BUYER.toString(),
                title = resourceProvider.getMenuChatBuyerTitle(),
                applink = getFallbackChatBuyerApplink(),
                type = MenuItemType.CHAT_BUYER.counterType
            )
        )
        // Chat seller
        chatMenuList.add(
            UniversalInboxMenuDataResponse(
                icon = IconUnify.CHAT_BUYER.toString(),
                title = resourceProvider.getMenuChatSellerTitle(),
                applink = getFallbackChatSellerApplink(),
                type = MenuItemType.CHAT_SELLER.counterType
            )
        )
        return chatMenuList
    }

    private fun getFallbackChatBuyerApplink(): String {
        return "${ApplinkConst.TOP_CHAT}?${ApplinkConst.Inbox.PARAM_ROLE}=${ApplinkConst.Inbox.VALUE_ROLE_BUYER}"
    }

    private fun getFallbackChatSellerApplink(): String {
        return "${ApplinkConst.TOP_CHAT}?${ApplinkConst.Inbox.PARAM_ROLE}=${ApplinkConst.Inbox.VALUE_ROLE_SELLER}"
    }

    // Generate Other Section
    private fun getOthersSectionList(): List<UniversalInboxMenuDataResponse> {
        val otherMenuList = arrayListOf<UniversalInboxMenuDataResponse>()
        // Discussion
        otherMenuList.add(
            UniversalInboxMenuDataResponse(
                icon = IconUnify.DISCUSSION.toString(),
                title = resourceProvider.getMenuDiscussionTitle(),
                applink = getFallbackDiscussionApplink(),
                type = MenuItemType.DISCUSSION.counterType
            )
        )
        // Review
        otherMenuList.add(
            UniversalInboxMenuDataResponse(
                icon = IconUnify.STAR.toString(),
                title = resourceProvider.getMenuReviewTitle(),
                applink = getFallbackReviewApplink(),
                type = MenuItemType.REVIEW.counterType
            )
        )
        return otherMenuList
    }

    private fun getFallbackDiscussionApplink(): String {
        return ApplinkConstInternalGlobal.INBOX_TALK
    }

    private fun getFallbackReviewApplink(): String {
        return Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(
            PAGE_SOURCE_KEY,
            PAGE_SOURCE_REVIEW_INBOX
        ).build().toString()
    }
}
