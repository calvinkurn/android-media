package com.tokopedia.purchase_platform.common.analytics

import javax.inject.Inject

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class CheckoutAnalyticsMultipleAddress @Inject constructor() : TransactionAnalytics() {
    fun eventClickAddressCartMultipleAddressClickPlusFromMultiple() {
        sendEventCategoryAction(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PLUS_FROM_MULTIPLE
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanNotSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanFromEditSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickAddressCartMultipleAddressClickButtonSimpanFromEditNotSuccess() {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_ADDRESS,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        )
    }
}