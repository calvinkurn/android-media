package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.*;

/**
 * Created by fajarnuha on 01/03/19.
 */
public class CornerAnalytics extends TransactionAnalytics {

    public CornerAnalytics() {

    }

    public void sendChooseCornerAddress() {
        sendEventCategoryAction(
                EventName.CLICK_REGISTER,
                EventCategory.CART_CHANGE_ADDRESS,
                EventAction.CHOOSE_LOCATION_CORNER
        );
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
