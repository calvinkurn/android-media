package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName.CLICK_COURIER
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CheckoutEgoldAnalytics @Inject constructor(val userSession: UserSessionInterface) : TransactionAnalytics() {

    companion object {
        // Event Action
        const val EVENT_ACTION_CLICK_EGOLD_ROUNDUP = "click egold roundup"

        // Event Label
        const val EVENT_LABEL_CHECK = "check"
        const val EVENT_LABEL_UNCHECK = "uncheck"
    }

    fun eventClickEgoldRoundup(checked: Boolean) {
        val gtmData = getGtmData(
                CLICK_COURIER,
                COURIER_SELECTION,
                EVENT_ACTION_CLICK_EGOLD_ROUNDUP,
                if (checked) EVENT_LABEL_CHECK else EVENT_LABEL_UNCHECK
        )

        gtmData[BUSINESS_UNIT] = DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[CURRENT_SITE] = DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[USER_ID] = userSession.userId

        sendGeneralEvent(gtmData)
    }

}