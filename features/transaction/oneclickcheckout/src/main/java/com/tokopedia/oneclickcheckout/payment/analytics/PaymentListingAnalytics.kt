package com.tokopedia.oneclickcheckout.payment.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class PaymentListingAnalytics @Inject constructor() : TransactionAnalytics() {

    fun eventClickBackArrowInPilihPembayaran() {
        sendGeneralEvent(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_BACK_ARROW_IN_PILIH_METHOD_PAYMENT
        )
    }
}
