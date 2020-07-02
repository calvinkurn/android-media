package com.tokopedia.manageaddress.data.analytics;

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;


/**
 * @author anggaprasetiyo on 05/06/18.
 */
public class CheckoutAnalyticsMultipleAddress extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsMultipleAddress() {
    }

    public void eventClickAtcCartMultipleAddressClickBackArrowFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BACK_ARROW_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickKembaliDanHapusPerubahanFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickTetapDiHalamanIniFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_TETAP_DI_HALAMAN_INI_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }



    public void eventClickAtcCartMultipleAddressClickPilihKurirPengirimanFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_PILIH_KURIR_PENGIRIMAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickXFromUbahFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_X_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }



    public void eventClickAtcCartMultipleAddressClickInputQuantityFromUbahFromKirimKeBeberapaAlamat(String productQuantity) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_INPUT_QUANTITY_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT,
                productQuantity
        );
    }

    public void eventClickAtcCartMultipleAddressClickTulisCatatanFromUbahFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_TULIS_CATATAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickSimpanFromUbahFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_SIMPAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }


    public void eventClickAtcCartMultipleAddressClickEditFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_EDIT_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickTambahPengirimanBaruFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_TAMBAH_PENGIRIMAN_BARU_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );

        sendEventCategoryAction(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_TAMBAH_PENGIRIMAN_BARU_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickMinFromUbahFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_MIN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCartMultipleAddressClickPlusFromUbahFromKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_PLUS_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }


    public void eventClickAddressCartMultipleAddressClickPlusFromMultiple() {
        sendEventCategoryAction(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_PLUS_FROM_MULTIPLE
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanNotSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanFromEditSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        );
    }

    public void eventClickAddressCartMultipleAddressClickButtonSimpanFromEditNotSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ADDRESS,
                EventCategory.CART_MULTIPLE_ADDRESS,
                EventAction.CLICK_BUTTON_SIMPAN_FROM_EDIT,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS
        );
    }
}
