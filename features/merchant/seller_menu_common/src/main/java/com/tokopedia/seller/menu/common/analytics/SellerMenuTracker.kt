package com.tokopedia.seller.menu.common.analytics

import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.ADD_PRODUCT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.CLICK_SHOP_ACCOUNT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.KEY_BUSINESS_UNIT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.KEY_CURRENT_SITE
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.KEY_USER_ID
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.MA_SHOP_ACCOUNT
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.PHYSICAL_GOODS
import com.tokopedia.seller.menu.common.analytics.SellerMenuTrackingConst.TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 04/09/20
 */

class SellerMenuTracker @Inject constructor(
        private val analytics: Analytics,
        private val userSession: UserSessionInterface
) {

    fun sendEventAddProductClick() {
        val event = TrackAppUtils.gtmData(
                CLICK_SHOP_ACCOUNT,
                MA_SHOP_ACCOUNT,
                ADD_PRODUCT,
                ""
        )
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }
}