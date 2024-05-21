package com.tokopedia.checkoutpayment.list.view

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class PaymentListingAnalytics @Inject constructor() : TransactionAnalytics() {

    fun eventClickBackArrowInPilihPembayaran() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT_EXPRESS,
            ConstantTransactionAnalytics.EventCategory.PURCHASE_SETTING,
            ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW_IN_PILIH_METHOD_PAYMENT
        )
    }
}
