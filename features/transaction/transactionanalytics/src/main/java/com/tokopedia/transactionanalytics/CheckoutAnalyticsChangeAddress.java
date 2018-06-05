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

    public void eventClickChangeAddressClickPilihAlamatLainyaFromGAntiAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_ALAMAT_LAINNYA_FROM_GANTI_ALAMAT,
                "");
    }

    public void eventClickChangeAddressClickTambahAlamatBaruFromGantiAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT,
                "");
    }

    public void eventClickChangeAddressClickBackArrowFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikTombolKembaliDanHapusPerubahan() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressTetapDiHalamanIni() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TETAP_DI_HALAMAN_INI_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikTambahAlamatBaru() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_PENGIRIMAN_BARU_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikEdit() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_EDIT_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikPilihKurirPengiriman() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_KURIR_PENGIRIMAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikTombolX() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_X_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikTombolMinus() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_MIN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikTombolPlus() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PLUS_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikAngka() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_INPUT_QUANTITY_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikTulisCatatan() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TULIS_CATATAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventMultipleAddressKlikSimpan() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_SIMPAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }
}
