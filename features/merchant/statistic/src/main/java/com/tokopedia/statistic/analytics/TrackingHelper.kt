package com.tokopedia.statistic.analytics

import android.content.Context
import com.tokopedia.statistic.R
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 22/07/20
 */

object TrackingHelper {

    fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendEnhanceEcommerceEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun getShopStatus(userSession: UserSessionInterface): String {
        return when {
            userSession.isShopOfficialStore -> TrackingConstant.SHOP_OS
            userSession.isPowerMerchantIdle || userSession.isGoldMerchant -> TrackingConstant.SHOP_PM
            else -> TrackingConstant.SHOP_RM
        }
    }

    fun getCategoryPage(context: Context, pageTitle: String): String? {
        return when(pageTitle) {
            context.getString(R.string.stc_product) -> TrackingConstant.SELLER_APP_PRODUCT_INSIGHT
            context.getString(R.string.stc_shop) -> TrackingConstant.SELLER_APP_SHOP_INSIGHT
            context.getString(R.string.stc_buyer) -> TrackingConstant.SELLER_APP_BUYER_INSIGHT
            else -> null
        }
    }
}