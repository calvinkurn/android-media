package com.tokopedia.checkout.analytics;

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 01/03/19.
 */
public class CornerAnalytics extends TransactionAnalytics {

    @Inject
    public CornerAnalytics() {

    }

    public void sendClickCornerAddress(String cornerAddress) {
        sendEventCategoryActionLabel(
                EventName.CLICK_REGISTER,
                EventCategory.CORNER_ADDRES,
                EventAction.CLICK_CORNER_ADDRESS,
                cornerAddress
        );
    }

    public void sendViewCornerError() {
        sendEventCategoryAction(
                EventName.VIEW_REGISTER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_CORNER_ERROR
        );
    }

    public void sendViewCornerPoError() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_CORNER_PO_ERROR
        );
    }
}
