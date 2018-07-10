package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class CheckoutAnalyticsChangeAddress extends CheckoutAnalytics {
    @Inject
    public CheckoutAnalyticsChangeAddress(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    // Klik pilih alamat lainnya dari ganti alamat
    public void eventClickChangeAddressClickChooseOtherAddressFromChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_ALAMAT_LAINNYA_FROM_GANTI_ALAMAT,
                "");
    }

    // Klik tambah alamat baru dari ganti alamat
    public void eventClickChangeAddressClickAddNewAddressFromChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT,
                "");
    }

    // Klik kirim ke beberapa alamat dari ganti alamat
    public void eventClickChangeAddressClickSendToMultipleAddressFromChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KIRIM_KE_BEBERAPA_ALAMAT_FROM_GANTI_ALAMAT,
                ""
        );
    }

    // Klik kirim ke alamat ini dari ganti alamat
    public void eventClickChangeAddressClickSendToThisAddressFromChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KIRIM_KE_ALAMAT_INI_FROM_GANTI_ALAMAT,
                ""
        );
    }

    // Klik X dari pilih alamat lainnya
    public void eventClickChangeAddressClickXFromChooseOtherAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_X_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    // Klik plus dari pilih alamat lainnya
    public void eventClickChangeAddressClickPlusFromChooseOtherAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PLUS_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    // Klik ubah dari pilih alamat lainnya
    public void eventClickChangeAddressClickEditFromChooseOtherAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    // Klik checklist alamat dari pilih alamat lainnya
    public void eventClickChangeAddressClickChecklistAddressFromChooseOtherAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    public void eventImpressionChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_CHANGE_ADDRESS,
                ""
        );
    }

    // Submit search dari pilih alamat lainnya
    public void eventClickChangeAddressSubmitSearchFromChooseOtherAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    // Klik tambah alamat dari plus
    public void eventClickChangeAddressClickAddAddressFromPlus() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_FROM_PLUS,
                ""
        );
    }

    // Klik arrow back dari ganti alamat
    public void eventClickChangeAddressClickArrowBackFromChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_ARROW_BACK_FROM_GANTI_ALAMAT,
                ""
        );
    }

    public void eventClickChangeAddressClickArrowBackFromPlus() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_ARROW_BACK_FROM_PLUS,
                "");
    }

}
