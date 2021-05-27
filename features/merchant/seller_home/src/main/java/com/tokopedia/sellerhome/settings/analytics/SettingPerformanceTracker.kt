package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.TrackingHelper
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SettingPerformanceTracker @Inject constructor(userSession: UserSessionInterface) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val typeShop = when {
        userSession.isShopOfficialStore -> {
            TrackingConstant.SHOP_TYPE_OS
        }
        userSession.isGoldMerchant -> {
            TrackingConstant.SHOP_TYPE_PM
        }
        else -> {
            TrackingConstant.SHOP_TYPE_RM
        }
    }

    fun clickItemEntryPointPerformance(isNewSeller: Boolean) {
        val event = mapOf(
                TrackingConstant.EVENT to TrackingConstant.CLICK_SHOP_SCORE,
                TrackingConstant.EVENT_CATEGORY to if (isNewSeller) TrackingConstant.CATEGORY_SHOP_SCORE_NEW_SELLER else TrackingConstant.CATEGORY_SHOP_SCORE,
                TrackingConstant.EVENT_ACTION to if (isNewSeller) TrackingConstant.ACTION_SHOP_SCORE_NEW_SELLER else TrackingConstant.ACTION_SHOP_SCORE,
                TrackingConstant.EVENT_LABEL to  if (isNewSeller) "${TrackingConstant.NEW_SELLER} $typeShop" else typeShop,
                TrackingConstant.BUSINESS_UNIT to TrackingConstant.PHYSICAL_GOODS,
                TrackingConstant.CURRENT_SITE to TrackingConstant.TOKOPEDIA_SELLER
        )
        tracker.sendGeneralEvent(event)
    }

    fun impressItemEntryPointPerformance(isNewSeller: Boolean) {
        val event = mapOf(
                TrackingConstant.EVENT to TrackingConstant.VIEW_SHOP_SCORE_IRIS,
                TrackingConstant.EVENT_CATEGORY to if (isNewSeller) TrackingConstant.CATEGORY_SHOP_SCORE_NEW_SELLER else TrackingConstant.CATEGORY_SHOP_SCORE,
                TrackingConstant.EVENT_ACTION to if (isNewSeller) TrackingConstant.IMPRESS_ACTION_SHOP_SCORE_NEW_SELLER else TrackingConstant.IMPRESS_ACTION_SHOP_SCORE,
                TrackingConstant.EVENT_LABEL to  if (isNewSeller) "${TrackingConstant.NEW_SELLER} $typeShop" else typeShop,
                TrackingConstant.BUSINESS_UNIT to TrackingConstant.PHYSICAL_GOODS,
                TrackingConstant.CURRENT_SITE to TrackingConstant.TOKOPEDIA_SELLER
        )
        tracker.sendGeneralEvent(event)
    }
}