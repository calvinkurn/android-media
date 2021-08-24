package com.tokopedia.digital.common.analytic;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;


/**
 * @author by furqan on 13/08/18.
 */

public class DigitalAnalytics {

    public DigitalAnalytics() {
    }

    public void eventClickPanduanPage(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.DIGITAL_GENERAL_EVENT,
                DigitalEventTracking.Category.DIGITAL_NATIVE,
                DigitalEventTracking.Action.CLICK_PANDUAN_SECTION,
                categoryName.toLowerCase()
        ));
    }

    public void eventClickBuyOnNative(String categoryItem, String isInstant) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryItem,
                DigitalEventTracking.Action.CLICK_BELI + " - " + categoryItem,
                isInstant.toLowerCase()
        ));
    }

    public void eventSelectOperatorOnNativePage(String categoryName, String operatorName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.SELECT_OPERATOR,
                DigitalEventTracking.Label.PRODUCT + operatorName
        ));
    }

    public void eventSelectProductOnNativePage(String categoryName, String productDesc) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.SELECT_PRODUCT,
                DigitalEventTracking.Label.PRODUCT + productDesc
        ));
    }

    public void eventClickDaftarTransaksiEvent(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_DAFTAR_TX,
                DigitalEventTracking.Label.PRODUCT + categoryName
        ));
    }

    public void eventSelectNumberOnUserProfileNative(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.DIGITAL + categoryName,
                DigitalEventTracking.Action.CLICK_BILL,
                DigitalEventTracking.Label.DIGITAL
        ));
    }

    public void eventClickSearchBar(String categoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                DigitalEventTracking.Action.CLICK_SEARCH_BAR,
                DigitalEventTracking.Label.PRODUCT + categoryName
        ));
    }

    public void eventCheckInstantSaldo(String categoryName, boolean isChecked) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(DigitalEventTracking.Event.HOMEPAGE_INTERACTION,
                DigitalEventTracking.Category.RECHARGE + categoryName,
                isChecked ? DigitalEventTracking.Action.CHECK_INSTANT_SALDO : DigitalEventTracking.Action.UNCHECK_INSTANT_SALDO,
                DigitalEventTracking.Label.PRODUCT + categoryName));
    }

    public void onClickSliceRecharge(String userId, String rechargeProductFromSlice) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                DigitalEventTracking.Event.EVENT_KEY, "clickGAMain",
                DigitalEventTracking.Event.EVENT_CATEGORY, "ga main app",
                DigitalEventTracking.Event.EVENT_ACTION, "click item transaction",
                DigitalEventTracking.Event.EVENT_LABEL, rechargeProductFromSlice,
                DigitalEventTracking.Event.BUSINESS_UNIT, "recharge",
                DigitalEventTracking.Event.CURRENT_SITE, "tokopediadigital",
                DigitalEventTracking.Event.USER_ID, userId
        ));
    }

    public void onOpenPageFromSlice() {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                DigitalEventTracking.Event.EVENT_KEY, "openScreen",
                DigitalEventTracking.Event.EVENT_SCREEN_NAME, "digital product - from voice search - mainapp"
        ));
    }
}
