package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class CheckoutAnalyticsMultipleAddress extends CheckoutAnalytics {
    @Inject
    public CheckoutAnalyticsMultipleAddress(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickMultipleAddressClickBackArrowFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                "");
    }

    public void eventClickMultipleAddressClickKembaliDanHapusPerubahanFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickTetapDiHalamanIniFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TETAP_DI_HALAMAN_INI_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickTambahPengirimanBaruFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TAMBAH_PENGIRIMAN_BARU_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickEditFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_EDIT_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickPilihKurirPengirimanFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_KURIR_PENGIRIMAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickXFromUbahFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_X_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickMinFromUbahFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_MIN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickPlusFromUbahFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_PLUS_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickInputQuantityFromUbahFromKirimKeBeberapaAlamat(String productQuantity) {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_INPUT_QUANTITY_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                productQuantity
        );
    }

    public void eventClickMultipleAddressClickTulisCatatanFromUbahFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_TULIS_CATATAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }

    public void eventClickMultipleAddressClickSimpanFromUbahFromKirimKeBeberapaAlamat() {
        analyticTracker.sendEventTracking(ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.CART_MULTIPLE_ADDRESS,
                ConstantTransactionAnalytics.EventAction.CLICK_SIMPAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                ""
        );
    }
}
