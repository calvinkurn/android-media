package com.tokopedia.purchase_platform.common.analytics

import javax.inject.Inject

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class CheckoutAnalyticsMultipleAddress @Inject constructor() : TransactionAnalytics() {
    fun eventClickAddressCartMultipleAddressClickPlusFromMultiple() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PLUS_FROM_MULTIPLE
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanSuccess() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanNotSuccess() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanFromEditSuccess() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanFromEditNotSuccess() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }
}