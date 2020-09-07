package com.tokopedia.seller.menu.common.analytics

import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.ADD_PRODUCT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.BUYER_CLICK_COMPLAIN
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.BUYER_CLICK_DISCUSSION
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.BUYER_CLICK_REVIEW
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.CLICK_SHOP_ACCOUNT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.KEY_BUSINESS_UNIT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.KEY_CURRENT_SITE
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.KEY_USER_ID
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.MA_SHOP_ACCOUNT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.OTHER_CLICK_SELLER_EDU
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.OTHER_CLICK_SHOP_SETTINGS
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.OTHER_CLICK_TOKOPEDIA_CARE
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.PHYSICAL_GOODS
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.PRODUCT_LIST
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.SA_CLICK_ADS_AND_PROMO
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.SA_CLICK_FINANCIAL_SERVICES
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.SA_CLICK_POST_FEED
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.SA_CLICK_SHOP_STAT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 04/09/20
 */

class SellerMenuTracker(
        private val analytics: Analytics,
        private val userSession: UserSessionInterface
) {

    fun sendEventAddProductClick() {
        val event = createMenuItemEvent(ADD_PRODUCT)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickProductList() {
        val event = createMenuItemEvent(PRODUCT_LIST)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickReview() {
        val event = createMenuItemEvent(BUYER_CLICK_REVIEW)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickDiscussion() {
        val event = createMenuItemEvent(BUYER_CLICK_DISCUSSION)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickComplain() {
        val event = createMenuItemEvent(BUYER_CLICK_COMPLAIN)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickSellerEdu() {
        val event = createMenuItemEvent(OTHER_CLICK_SELLER_EDU)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickTokopediaCare() {
        val event = createMenuItemEvent(OTHER_CLICK_TOKOPEDIA_CARE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopSettings() {
        val event = createMenuItemEvent(OTHER_CLICK_SHOP_SETTINGS)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopStatistic() {
        val event = createMenuItemEvent(SA_CLICK_SHOP_STAT)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickCentralizePromo() {
        val event = createMenuItemEvent(SA_CLICK_ADS_AND_PROMO)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickFeedAndPlay() {
        val event = createMenuItemEvent(SA_CLICK_POST_FEED)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickFintech() {
        val event = createMenuItemEvent(SA_CLICK_FINANCIAL_SERVICES)
        analytics.sendGeneralEvent(event)
    }

    private fun createMenuItemEvent(actionName: String): Map<String, Any> {
        val event = TrackAppUtils.gtmData(
                CLICK_SHOP_ACCOUNT,
                MA_SHOP_ACCOUNT,
                actionName,
                ""
        )
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS
        return event
    }
}