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

    public void eventClickPelajariSelengkapnya() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PELAJARI_SELENGKAPNYA
        );
    }

    public void eventClickBackArrow() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BACK_ARROW
        );
    }

    public void eventClickXPadaSyarat() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION_COD,
                EventAction.CLICK_X_PADA_SYARAT
        );
    }

    public void eventScrollTerms(int scrollNumber) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION_COD,
                EventAction.SCROLL_TERMS_AND_CONDS,
                String.valueOf(scrollNumber)
        );
    }

    public void eventClickChecklistShippingDuration(boolean promo, String shippingDuration, boolean cod) {
        String label = (promo ? "promo" : "non promo") + " - " + shippingDuration + " - " +
                (cod ? "cod" : "");
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CHECKLIST,
                label
        );
    }

    public void eventClickChangeCourier(boolean promo, String shippingProductId, boolean cod) {
        String label = (promo ? "promo" : "non promo") + " - " + shippingProductId + " - " +
                (cod ? "cod" : "");
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CHANGE_COURIER,
                label
        );
    }

    public void eventEEClickButtonCod(Map<String, Object> data) {
        String label = "success - eligible";
        sendEnhancedEcommerce(
                DataLayer.mapOf(
                        Key.EVENT, EventName.CHECKOUT,
                        Key.EVENT_CATEGORY, EventCategory.COURIER_SELECTION,
                        Key.EVENT_ACTION, EventAction.CLICK_BAYAR_DI_TEMPAT,
                        Key.EVENT_LABEL, label,
                        Key.E_COMMERCE, data
                )
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
