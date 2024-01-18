package com.tokopedia.shareexperience.data.analytic

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
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
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        label: String
    ) {
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_SHARE_BUTTON,
            ShareExAnalyticsConst.Key.EVENT_LABEL, label,
            ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_10365,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.TOP_NAV_PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = id
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackActionClickClose(
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        label: String
    ) {
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_CLOSE,
            ShareExAnalyticsConst.Key.EVENT_LABEL, label,
            ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_10366,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = id
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackActionClickChannel(
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        label: String
    ) {
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_SHARING_CHANNEL,
            ShareExAnalyticsConst.Key.EVENT_LABEL, label,
            ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_10367,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = id
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackImpressionBottomSheet(
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        label: String
    ) {
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.VIEW_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.VIEW_SHARING_CHANNEL,
            ShareExAnalyticsConst.Key.EVENT_LABEL, label,
            ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_10368,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = id
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }

    fun trackImpressionTickerAffiliate(
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        label: String
    ) {
        val bundle = Bundle().apply {
            putString(ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.IMPRESSION_TICKER_AFFILIATE)
            putString(ShareExAnalyticsConst.Key.EVENT_LABEL, label)
            putString(ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_31185)
            putString(ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE)
            putString(ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE)
        }
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                bundle.putString(ShareExAnalyticsConst.Key.EVENT_CATEGORY, ShareExAnalyticsConst.Category.PDP)
                bundle.putString(ShareExAnalyticsConst.Key.PRODUCT_ID, id)
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
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        label: String
    ) {
        val map: MutableMap<String, Any> = DataLayer.mapOf(
            ShareExAnalyticsConst.Key.EVENT, ShareExAnalyticsConst.Event.CLICK_COMMUNICATION,
            ShareExAnalyticsConst.Key.EVENT_ACTION, ShareExAnalyticsConst.Action.CLICK_TICKER_AFFILIATE,
            ShareExAnalyticsConst.Key.EVENT_LABEL, label,
            ShareExAnalyticsConst.Key.TRACKER_ID, ShareExAnalyticsConst.Tracker.ID_31186,
            ShareExAnalyticsConst.Key.BUSINESS_UNIT, ShareExAnalyticsConst.Default.SHARING_EXPERIENCE,
            ShareExAnalyticsConst.Key.CURRENT_SITE, ShareExAnalyticsConst.Default.TOKOPEDIA_MARKETPLACE
        )
        when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> {
                map[ShareExAnalyticsConst.Key.EVENT_CATEGORY] = ShareExAnalyticsConst.Category.PDP
                map[ShareExAnalyticsConst.Key.PRODUCT_ID] = id
            }
            else -> Unit
        }
        if (userSession.isLoggedIn) {
            map[ShareExAnalyticsConst.Key.USER_ID] = userSession.userId
        }
        tracker.sendGeneralEvent(map)
    }
}
