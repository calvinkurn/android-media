package com.tokopedia.oneclickcheckout.preference.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

class PreferenceListAnalytics : TransactionAnalytics() {

    fun eventClickBackArrowInPilihPembayaran() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_BACK_ARROW_IN_PILIH_METHOD_PAYMENT
        )
    }
}
