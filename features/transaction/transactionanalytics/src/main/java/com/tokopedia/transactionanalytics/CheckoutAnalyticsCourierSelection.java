package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 06/06/18.
 */
public class CheckoutAnalyticsCourierSelection extends CheckoutAnalytics {
    @Inject
    public CheckoutAnalyticsCourierSelection(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickCourierSelectiontClickBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW,
                ""
        );
    }

    public void eventClickCourierSelectionClickKembaliDanHapusPerubahanFromBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_BACK_ARROW,
                ""
        );
    }

    public void eventClickCourierSelectionClickTetapDiHalamanIniFromBackArrow() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_TETAP_DI_HALAMAN_INI_FROM_BACK_ARROW,
                ""
        );
    }

    public void eventClickCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickCourierSelectionClickGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCourierSelectionClickKuponSayaFromGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCourierSelectionClickKodePromoFromGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCourierSelectionClickSubtotal() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_SUBTOTAL,
                ""
        );
    }

    public void eventClickCourierSelectionClickCobaLagiWhenGagalDiskonVoucher() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_COBA_LAGI_WHEN_GAGAL_DISKON_VOUCHER,
                ""
        );
    }

    public void eventClickCourierSelectionClickHapusWhenGagalDiskonVoucher() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_WHEN_GAGAL_DISKON_VOUCHER,
                ""
        );
    }

    public void eventClickCourierSelectionClickHapusWhenSuksesDiskonVoucher() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_WHEN_SUKSES_DISKON_VOUCHER,
                ""
        );
    }

    public void eventClickCourierSelectionClickXOnBannerPromoCodeCode() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_ON_BANNER_PROMO_CODE,
                ""
        );
    }

    public void eventClickCourierSelectionClickXOnCourierOption() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_ON_COURIER_OPTION,
                ""
        );
    }

    public void eventClickCourierSelectionClickDropdownJenisPengiriman() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_DROPDOWN_JENIS_PENGIRIMAN,
                ""
        );
    }

    public void eventClickCourierSelectionClickLogisticAgent(String shippingAgent) {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_LOGISTIC_AGENT,
                shippingAgent
        );
    }

    public void eventClickCourierSelectionClickPilihLokasiPeta() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_LOKASI_PETA,
                ""
        );
    }

    public void eventImpressionCourierSelectionImpressionCourierSelection() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_COURIER_SELECTION,
                ""
        );
    }

    public void eventClickCourierSelectionClickAsuransiPengiriman() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_ASURANSI_PENGIRIMAN,
                ""
        );
    }

    public void eventClickCourierSelectionClickDropship() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_DROPSHIP,
                ""
        );
    }

    public void eventClickCourierSelectionClickTerimaSebagian() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_TERIMA_SEBAGIAN,
                ""
        );
    }

    public void eventClickCourierSelectionClickXFromGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCourierSelectionClickKuponFromGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCourierSelectionClickPilihMetodePembayaranSuccess() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickCourierSelectionClickPilihMetodePembayaranNotSuccess() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        );
    }

    public void eventClickCourierSelectionClickPilihMetodePembayaranCourierNotComplete() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.COURIER_NOT_COMPLETE
        );
    }

    public void eventImpressionCourierSelectionImpressionOnPopUpKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_ON_POP_UP_KUPON,
                ConstantTransactionAnalytics.EventLabel.KUOTA_PENUKARAN
        );
    }

    public void eventClickCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCourierSelectionClickSelectCourier() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_SELECT_COURIER,
                ""
        );
    }

    public void eventViewCourierSelectionClickCourierOption(String agent, String service) {
        analyticTracker.sendEventTracking(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_COURIER_OPTION,
                agent + " - " + service
        );
    }
}
