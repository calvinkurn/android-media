package com.tokopedia.transactionanalytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventLabel;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;


/**
 * @author anggaprasetiyo on 06/06/18.
 */
public class CheckoutAnalyticsCourierSelection extends TransactionAnalytics {
    @Inject
    public CheckoutAnalyticsCourierSelection(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickAtcCourierSelectionClickBackArrow() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BACK_ARROW
        );
    }

    public void eventClickAtcCourierSelectionClickKembaliDanHapusPerubahanFromBackArrow() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_BACK_ARROW
        );
    }

    public void eventClickAtcCourierSelectionClickTetapDiHalamanIniFromBackArrow() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_TETAP_DI_HALAMAN_INI_FROM_BACK_ARROW
        );
    }

    public void eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT
        );
    }

    public void eventClickAtcCourierSelectionClickGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCourierSelectionClickKuponSayaFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCourierSelectionClickKodePromoFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCourierSelectionClickSubtotal() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_SUBTOTAL
        );
    }

    public void eventClickAtcCourierSelectionClickCobaLagiWhenGagalDiskonVoucher() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_COBA_LAGI_WHEN_GAGAL_DISKON_VOUCHER
        );
    }

    public void eventClickAtcCourierSelectionClickHapusWhenGagalDiskonVoucher() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_HAPUS_WHEN_GAGAL_DISKON_VOUCHER
        );
    }

    public void eventClickAtcCourierSelectionClickHapusWhenSuksesDiskonVoucher() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_HAPUS_WHEN_SUKSES_DISKON_VOUCHER
        );
    }

    public void eventClickAtcCourierSelectionClickXOnBannerPromoCodeCode() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_ON_BANNER_PROMO_CODE
        );
    }

    public void eventClickAtcCourierSelectionClickXOnCourierOption() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_ON_COURIER_OPTION
        );
    }

    public void eventClickAtcCourierSelectionClickDropdownJenisPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_DROPDOWN_JENIS_PENGIRIMAN
        );
    }

    public void eventClickAtcCourierSelectionClickLogisticAgent(String shippingAgent) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_LOGISTIC_AGENT,
                shippingAgent
        );
    }

    public void eventClickAtcCourierSelectionClickPilihLokasiPeta() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_LOKASI_PETA
        );
    }

    public void eventViewAtcCourierSelectionImpressionCourierSelection() {
        sendEventCategoryAction(
                EventName.VIEW_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.IMPRESSION_COURIER_SELECTION
        );
    }

    public void eventClickAtcCourierSelectionClickAsuransiPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_ASURANSI_PENGIRIMAN
        );
    }

    public void eventClickAtcCourierSelectionClickDropship() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_DROPSHIP
        );
    }

    public void eventClickAtcCourierSelectionClickTerimaSebagian() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_TERIMA_SEBAGIAN
        );
    }

    public void eventClickAtcCourierSelectionClickXFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCourierSelectionClickKuponFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCourierSelectionClickPilihMetodePembayaranSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                EventLabel.SUCCESS
        );
    }

    public void eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                EventLabel.NOT_SUCCESS
        );
    }

    public void eventClickAtcCourierSelectionClickPilihMetodePembayaranCourierNotComplete() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                EventLabel.COURIER_NOT_COMPLETE
        );
    }

    public void eventViewAtcCourierSelectionImpressionOnPopUpKupon() {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.IMPRESSION_ON_POP_UP_KUPON,
                EventLabel.KUOTA_PENUKARAN
        );
    }

    public void eventClickAtcCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickCourierSelectionClickSelectCourier() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_SELECT_COURIER
        );
    }

    public void eventViewCourierSelectionClickCourierOption(String agent, String service) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_COURIER_OPTION,
                agent + " - " + service
        );
    }

    public void eventViewCourierSelectionImpressionCourierOption(String agent, String service) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.IMPRESSION_COURIER_OPTION,
                agent + " - " + service
        );
    }

    public void eventViewPromoCourierSelectionValidationErrorVoucherPromoFromGunakanKodePromoAtauKupon(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.VIEW_PROMO,
                EventCategory.COURIER_SELECTION,
                EventAction.VALIDATION_ERROR_VOUCHER_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                errorMessage
        );
    }

    public void eventClickCouponCourierSelectionClickKuponFromKuponSaya(String couponName) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KUPON_FROM_KUPON_SAYA,
                couponName
        );
    }

    public void eventClickCourierSelectionClickBackArrowFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BACK_ARROW_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickCourierSelectionClickSelectCourierOnCart() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_SELECT_COURIER_ON_CART
        );
    }


}
