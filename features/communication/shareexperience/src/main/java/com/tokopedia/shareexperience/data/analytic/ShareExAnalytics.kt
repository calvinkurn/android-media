package com.tokopedia.shareexperience.data.analytic

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShareExAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private val tracker: ContextAnalytics by lazy(LazyThreadSafetyMode.NONE) {
        TrackApp.getInstance().gtm
    }

    fun trackActionClickIconShare(
        productId: String = "",
        shopId: String = "",
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        label: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString()
        )
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_SHARE_BUTTON,
            ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_10365
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.TOP_NAV_PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.REVIEW -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50124
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.REVIEW
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.GOPAYLATER_REFERRAL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50464
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.GOPAYLATER_REFERRAL
            }
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45653
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.ORDER_DETAIL
                map[ShareExAnalyticsConst.Key.SHOP_ID] = shopId
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackActionClickClose(
        productId: String = "",
        shopId: String = "",
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        label: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString()
        )
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_CLOSE,
            ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_10366
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.REVIEW -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50125
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.REVIEW
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.GOPAYLATER_REFERRAL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50465
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.GOPAYLATER_REFERRAL
            }
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45654
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.ORDER_DETAIL
                map[ShareExAnalyticsConst.Key.SHOP_ID] = shopId
            }
            ShareExPageTypeEnum.THANK_YOU_PRODUCT -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45899
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.THANK_YOU_PAGE
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackActionClickChannel(
        productId: String = "",
        shopId: String = "",
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        channel: String,
        imageType: String,
        label: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString(),
            channel = channel,
            imageType = imageType
        )
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_SHARING_CHANNEL,
            ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_10367
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.REVIEW -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50126
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.REVIEW
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.GOPAYLATER_REFERRAL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50466
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.GOPAYLATER_REFERRAL
            }
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45655
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.ORDER_DETAIL
                map[ShareExAnalyticsConst.Key.SHOP_ID] = shopId
            }
            ShareExPageTypeEnum.THANK_YOU_PRODUCT -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45900
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.THANK_YOU_PAGE
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackImpressionBottomSheet(
        productId: String = "",
        shopId: String = "",
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        label: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString()
        )
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.VIEW_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.VIEW_SHARING_CHANNEL,
            ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_10368
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.REVIEW -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50127
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.REVIEW
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = productId
            }
            ShareExPageTypeEnum.GOPAYLATER_REFERRAL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50467
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.GOPAYLATER_REFERRAL
            }
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45656
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.ORDER_DETAIL
                map[ShareExAnalyticsConst.Key.SHOP_ID] = shopId
            }
            ShareExPageTypeEnum.THANK_YOU_PRODUCT -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_45901
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.THANK_YOU_PAGE
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackImpressionTickerAffiliate(
        identifier: String,
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        label: String
    ) {
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                trackImpressionTickerAffiliateEnhancedEcommerce(
                    label,
                    shareId,
                    pageTypeEnum,
                    identifier
                )
            }

            ShareExPageTypeEnum.ORDER_DETAIL -> {
                trackImpressionTickerAffiliateGeneralEvent(
                    identifier,
                    pageTypeEnum,
                    shareId,
                    label
                )
            }

            else -> Unit
        }
    }

    private fun trackImpressionTickerAffiliateGeneralEvent(
        identifier: String,
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        label: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString()
        )
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.VIEW_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.IMPRESSION_TICKER_AFFILIATE,
            ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50278
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.ORDER_DETAIL
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = identifier
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    private fun trackImpressionTickerAffiliateEnhancedEcommerce(
        label: String,
        shareId: String?,
        pageTypeEnum: ShareExPageTypeEnum,
        identifier: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString()
        )
        val bundle = Bundle().apply {
            putString(ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.VIEW_ITEM)
            putString(ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.IMPRESSION_TICKER_AFFILIATE)
            putString(ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel)
            putString(ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE)
            putString(ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE)
        }
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                bundle.putString(ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_31185)
                bundle.putString(ShareExAnalyticsConst.Key.EVENT_CATEGORY, ShareExAnalyticsConst.Category.PDP)
                bundle.putString(ShareExAnalyticsConst.Key.PRODUCT_ID, identifier)
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            bundle.putString(ShareExAnalyticsConst.Key.USER_ID, userSession.userId)
        }
        bundle.putParcelableArrayList(ShareExAnalyticsConst.Key.PROMOTIONS, generatePromotions())
        tracker.sendEnhanceEcommerceEvent(ShareExAnalyticsConst.Event.CLICK_COMMUNICATION, bundle)
    }

    private fun generatePromotions(): ArrayList<Bundle> {
        return arrayListOf(
            Bundle().apply {
                putString(ShareExAnalyticsConst.Key.CREATIVE_NAME, ShareExAnalyticsConst.Default.NOT_SET)
                putString(ShareExAnalyticsConst.Key.CREATIVE_SLOT, "1")
                putString(ShareExAnalyticsConst.Key.ITEM_ID, "")
                putString(ShareExAnalyticsConst.Key.ITEM_NAME, "")
            },
            Bundle().apply {
                putString(ShareExAnalyticsConst.Key.CREATIVE_NAME, ShareExAnalyticsConst.Default.NOT_SET)
                putString(ShareExAnalyticsConst.Key.CREATIVE_SLOT, "2")
                putString(ShareExAnalyticsConst.Key.ITEM_ID, "")
                putString(ShareExAnalyticsConst.Key.ITEM_NAME, "")
            }
        )
    }

    fun trackActionClickAffiliateRegistration(
        identifier: String,
        pageTypeEnum: ShareExPageTypeEnum,
        shareId: String?,
        label: String
    ) {
        val updatedLabel = updateLabel(
            label = label,
            shareId = shareId.toString()
        )
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_TICKER_AFFILIATE,
            ShareExAnalyticsConst.Key.EVENT_LABEL, updatedLabel,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_31186
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = identifier
            }
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50279
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.ORDER_DETAIL
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = identifier
            }
            ShareExPageTypeEnum.THANK_YOU_PRODUCT -> {
                map[ShareExAnalyticsConst.Key.TRACKER_ID] = ShareExAnalyticsConst.Tracker.ID_50879
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.THANK_YOU_PAGE
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = identifier
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    private fun updateLabel(
        label: String,
        shareId: String,
        channel: String = "",
        imageType: String = ""
    ): String {
        return label
            .replace(ShareExTrackerArg.SHARE_ID_KEY, shareId, ignoreCase = true)
            .replaceIfNotBlank(ShareExTrackerArg.CHANNEL_KEY, channel)
            .replaceIfNotBlank(ShareExTrackerArg.IMAGE_TYPE_KEY, imageType)
    }

    private fun String.replaceIfNotBlank(oldValue: String, newValue: String): String {
        return if (newValue.isNotBlank()) {
            this.replace(oldValue, newValue, ignoreCase = true)
        } else {
            this
        }
    }
}
