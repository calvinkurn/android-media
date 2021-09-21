package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import javax.inject.Inject

class CornerAnalytics @Inject constructor() : TransactionAnalytics() {
    fun sendViewCornerError() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.VIEW_REGISTER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_CORNER_ERROR
        )
    }

    fun sendViewCornerPoError() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_CORNER_PO_ERROR
        )
    }
}