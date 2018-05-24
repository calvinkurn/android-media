package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 21/05/18.
 */
public class CheckoutAnalyticsCartShipmentPage extends CheckoutAnalytics {
    @Inject
    public CheckoutAnalyticsCartShipmentPage(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickShipmentClickBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW,
                ""
        );
    }

    public void eventClickShipmentClickKembaliDanHapusPerubahanFromBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_BACK_ARROW,
                ""
        );
    }

    public void eventClickShipmentClickTetapDiHalamanIniFromBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_TETAP_DI_HALAMAN_INI_FROM_BACK_ARROW,
                ""
        );
    }

    public void eventClickShipmentClickGantiAlamatAtauKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickShipmentClickGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickShipmentClickKuponSayaFromGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickShipmentClickKodePromoFromGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickShipmentClickSubtotal() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_SUBTOTAL,
                ""
        );
    }

    public void eventClickShipmentClickCobaLagiWhenGagalDiskonVoucher() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_COBA_LAGI_WHEN_GAGAL_DISKON_VOUCHER,
                ""
        );
    }

    public void eventClickShipmentClickHapusWhenGagalDiskonVoucher() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_WHEN_GAGAL_DISKON_VOUCHER,
                ""
        );
    }

    public void eventClickShipmentClickHapusWhenSuksesDiskonVoucher() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_WHEN_SUKSES_DISKON_VOUCHER,
                ""
        );
    }

    public void eventClickShipmentClickXOnBannerPromoCodeCode() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_ON_BANNER_PROMO_CODE,
                ""
        );
    }

    public void eventClickShipmentClickXOnCourierOption() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_ON_COURIER_OPTION,
                ""
        );
    }

    public void eventClickShipmentClickDropdownJenisPengiriman() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_DROPDOWN_JENIS_PENGIRIMAN,
                ""
        );
    }

    public void eventClickShipmentClickLogisticAgent(String shippingAgent) {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_LOGISTIC_AGENT,
                shippingAgent
        );
    }

    public void eventClickShipmentClickPilihLokasiPeta() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_LOKASI_PETA,
                ""
        );
    }

    public void eventImpressionShipmentImpressionCourierSelection() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_COURIER_SELECTION,
                ""
        );
    }

    public void eventClickShipmentClickAsuransiPengiriman() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_ASURANSI_PENGIRIMAN,
                ""
        );
    }

    public void eventClickShipmentClickDropship() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_DROPSHIP,
                ""
        );
    }

    // Cart change address starts here

    public void eventClickShipmentClickPilihAlamatLainnyaFromGantiAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_ALAMAT_LAINNYA_FROM_GANTI_ALAMAT,
                ""
        );
    }

    public void eventClickShipmentClickTambahAlamatBaruFromGantiAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT,
                ""
        );
    }

    public void eventClickShipmentClickKirimKeBeberapaAlamatFromGantiAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KIRIM_KE_BEBERAPA_ALAMAT_FROM_GANTI_ALAMAT,
                ""
        );
    }

    public void eventClickShipmentClickKirimKeAlamatIniFromGantiAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KIRIM_KE_ALAMAT_INI_FROM_GANTI_ALAMAT,
                ""
        );
    }

    public void eventClickShipmentClickXFromPilihAlamatLainnya() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_X_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    public void eventClickShipmentClickPlusFromPilihAlamatLainnya() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PLUS_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    public void eventClickShipmentClickUbahFromPilihAlamatLainnya() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    public void eventClickShipmentClickChecklistAlamatFromPilihAlamatLainnya() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    public void eventImpressionShipmentImpressionChangeAddress() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_CHANGE_ADDRESS,
                ""
        );
    }

    public void eventClickShipmentSubmitSearchFromPilihAlamatLainnya() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA,
                ""
        );
    }

    public void eventClickShipmentClickTambahAlamatFromPlus() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_FROM_PLUS,
                ""
        );
    }
    // Cart change address ends here
}
