package com.tokopedia.logisticCommon.data.analytics;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.logisticCommon.data.analytics.ConstantLogisticAnalytics.EventAction;
import com.tokopedia.logisticCommon.data.analytics.ConstantLogisticAnalytics.EventCategory;
import com.tokopedia.logisticCommon.data.analytics.ConstantLogisticAnalytics.EventName;
import com.tokopedia.logisticCommon.data.analytics.ConstantLogisticAnalytics.Key;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 08/01/19.
 */
public class CodAnalytics extends LogisticAnalytics {

    @Inject
    public CodAnalytics() {
    }

    public void eventClickXPadaSyarat() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION_COD,
                EventAction.CLICK_X_PADA_SYARAT
        );
    }

    public void eventClickBackOnConfirmation() {
        sendEventCategoryAction(
                EventName.CLICK_COD,
                EventCategory.CASH_ON_DELIVERY,
                EventAction.CLICK_BACK_ON_CONFIRMATION
        );
    }

    public void eventClickBayarDiTempatCod() {
        sendEventCategoryAction(
                EventName.CLICK_COD,
                EventCategory.CASH_ON_DELIVERY,
                EventAction.CLICK_BAYAR_DI_TEMPAT
        );
    }

    public void eventEEClickBayarDiTempat(Map<String, Object> data) {
        sendEnhancedEcommerce(
                DataLayer.mapOf(
                        Key.EVENT, EventName.CHECKOUT,
                        Key.EVENT_CATEGORY, EventCategory.CASH_ON_DELIVERY,
                        Key.EVENT_ACTION, EventAction.CLICK_BAYAR_DI_TEMPAT,
                        Key.EVENT_LABEL, "",
                        Key.E_COMMERCE, data
                )
        );
    }


}
