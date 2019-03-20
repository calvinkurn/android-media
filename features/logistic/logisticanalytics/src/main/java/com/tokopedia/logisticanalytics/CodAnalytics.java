package com.tokopedia.logisticanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.logisticanalytics.ConstantLogisticAnalytics.*;

import javax.inject.Inject;


import java.util.Map;

/**
 * Created by fajarnuha on 08/01/19.
 */
public class CodAnalytics extends LogisticAnalytics {

    public CodAnalytics(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventViewBayarDiTempat() {
        sendEventCategoryAction(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_BAYAR_DI_TEMPAT
        );
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

    public void eventClickBayarDiTempatShipmentNotSuccessIneligible() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BAYAR_DI_TEMPAT,
                EventLabel.NOT_SUCCESS_INELIGIBLE
        );
    }

    public void eventClickBayarDiTempatShipmentNotSuccessIncomplete() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BAYAR_DI_TEMPAT,
                EventLabel.NOT_SUCCESS_INCOMPLETE
        );
    }

    public void eventClickBayarDiTempatShipmentSuccessEligible() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BAYAR_DI_TEMPAT,
                EventLabel.SUCCESS_ELIGIBLE
        );
    }

    public void eventViewErrorIneligible() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_ERROR_INELIGIBLE
        );
    }

    public void eventClickXIneligible() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_INELIGIBLE
        );
    }

    public void eventClickMengertiIneligible() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_MENGERTI_INELIGIBLE
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

    public void eventImpressionEligibleCod() {
        sendEventCategoryAction(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.IMPRESSION_ELIGIBLE_COD
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
