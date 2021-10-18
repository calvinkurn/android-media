package com.tokopedia.purchase_platform.common.analytics;

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;


/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class CheckoutAnalyticsMultipleAddress extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsMultipleAddress() {
    }

    public void eventClickAddressCartMultipleAddressClickPlusFromMultiple() {
        sendEventCategoryAction(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_PLUS_FROM_MULTIPLE
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanNotSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanFromEditSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanFromEditNotSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        );
    }
}
