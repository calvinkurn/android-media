package com.tokopedia.inbox.universalinbox.view

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetMetaResponse
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProvider
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CHATBOT_TYPE
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_SOURCE_KEY
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_SOURCE_REVIEW_INBOX
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WIDGET_PAGE_NAME
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxShopInfoUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class UniversalInboxMenuMapper @Inject constructor(
    private val resourceProvider: UniversalInboxResourceProvider
) {

    fun getStaticMenu(userSession: UserSessionInterface): List<Any> {
        val chatSectionList = getChatSectionList(userSession)
        val othersSectionList = getOthersSectionList()
        return chatSectionList + othersSectionList
    }

    // Generate Chat Section
    private fun getChatSectionList(userSession: UserSessionInterface): List<Any> {
        val chatSectionList = arrayListOf<Any>()
        chatSectionList.add(
            UniversalInboxMenuUiModel(
                type = MenuItemType.CHAT_BUYER,
                title = resourceProvider.getMenuChatBuyerTitle(),
                icon = IconUnify.CHAT_BUYER,
                counter = Int.ZERO,
                applink = getChatBuyerApplink()
            )
        )
        if (userSession.hasShop()) {
            chatSectionList.add(
                UniversalInboxMenuUiModel(
                    type = MenuItemType.CHAT_SELLER,
                    title = resourceProvider.getMenuChatSellerTitle(),
                    icon = IconUnify.CHAT_SELLER,
                    counter = Int.ZERO,
                    applink = getChatSellerApplink(),
                    additionalInfo = UniversalInboxShopInfoUiModel(
                        avatar = userSession.shopAvatar,
                        shopName = userSession.shopName
                    )
                )
            )
        }
        return chatSectionList
    }

    private fun getChatBuyerApplink(): String {
        return "${ApplinkConst.TOP_CHAT}?${ApplinkConst.Inbox.PARAM_ROLE}=${ApplinkConst.Inbox.VALUE_ROLE_BUYER}"
    }

    private fun getChatSellerApplink(): String {
        return "${ApplinkConst.TOP_CHAT}?${ApplinkConst.Inbox.PARAM_ROLE}=${ApplinkConst.Inbox.VALUE_ROLE_SELLER}"
    }

    private fun getOthersSectionList(): List<Any> {
        val othersSectionList = arrayListOf<Any>()
        othersSectionList.apply {
            add(
                UniversalInboxMenuUiModel(
                    type = MenuItemType.DISCUSSION,
                    title = resourceProvider.getMenuDiscussionTitle(),
                    icon = IconUnify.DISCUSSION,
                    counter = Int.ZERO,
                    applink = getDiscussionApplink()
                )
            )
            add(
                UniversalInboxMenuUiModel(
                    type = MenuItemType.REVIEW,
                    title = resourceProvider.getMenuReviewTitle(),
                    icon = IconUnify.STAR,
                    counter = Int.ZERO,
                    applink = getReviewApplink()
                )
            )
            add(UniversalInboxMenuSeparatorUiModel())
            add(getTopAdsUiModel())
            add(generateRecommendationWidgetModel())
        }
        return othersSectionList
    }

    open fun getTopAdsUiModel(): UniversalInboxTopAdsBannerUiModel {
        return UniversalInboxTopAdsBannerUiModel()
    }

    private fun getDiscussionApplink(): String {
        return ApplinkConstInternalGlobal.INBOX_TALK
    }

    private fun getReviewApplink(): String {
        return Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(
            PAGE_SOURCE_KEY,
            PAGE_SOURCE_REVIEW_INBOX
        ).build().toString()
    }

    fun mapWidgetMetaToUiModel(
        widgetMeta: UniversalInboxWidgetMetaResponse?,
        allCounter: UniversalInboxAllCounterResponse?
    ): UniversalInboxWidgetMetaUiModel {
        val result = UniversalInboxWidgetMetaUiModel()
        if (widgetMeta != null) {
            widgetMeta.metaData.forEach {
                it.mapToUiModel(allCounter)?.let { uiModel ->
                    result.widgetList.add(uiModel)
                }
            }
        } else {
            result.isError = true
        }
        return result
    }

    private fun UniversalInboxWidgetDataResponse.mapToUiModel(
        allCounter: UniversalInboxAllCounterResponse? = null
    ): UniversalInboxWidgetUiModel? {
        val counter = when (this.type) {
            CHATBOT_TYPE -> {
                allCounter?.othersUnread?.helpUnread ?: Int.ZERO
            }
            else -> Int.ZERO
        }
        return this.mapToWidget(counter)
    }

    private fun UniversalInboxWidgetDataResponse.mapToWidget(
        counter: Int = Int.ZERO
    ): UniversalInboxWidgetUiModel? {
        // If not dynamic (not controlled from BE) and no notification, do not show the widget
        return if (!this.isDynamic && counter < Int.ONE) {
            null
        } else {
            UniversalInboxWidgetUiModel(
                icon = this.icon.toIntOrZero(),
                title = this.title,
                subtext = this.subtext,
                applink = this.applink,
                counter = counter,
                type = this.type
            )
        }
    }

    private fun generateRecommendationWidgetModel(): RecommendationWidgetModel {
        return RecommendationWidgetModel(
            metadata = RecommendationWidgetMetadata(
                pageName = WIDGET_PAGE_NAME
            ),
            trackingModel = RecommendationWidgetTrackingModel(
                androidPageName = RecommendationCarouselTrackingConst.Category.INBOX_PAGE,
                eventActionImpression = RecommendationCarouselTrackingConst.Action.IMPRESSION_ON_PRODUCT_RECOMMENDATION_INBOX,
                eventActionClick = RecommendationCarouselTrackingConst.Action.CLICK_ON_PRODUCT_RECOMMENDATION_INBOX,
                listPageName = RecommendationCarouselTrackingConst.List.INBOX
            )
        )
    }
}
