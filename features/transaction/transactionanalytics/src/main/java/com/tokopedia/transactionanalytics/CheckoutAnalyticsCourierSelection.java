package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.Map;

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
    public CheckoutAnalyticsCourierSelection() {
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

    @Deprecated
    public void eventClickAtcCourierSelectionClickXFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    @Deprecated
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

    public void eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                EventLabel.NOT_SUCCESS + " - " + errorMessage
        );
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
    public void eventClickCourierSelectionClickSelectCourierOnCart() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_SELECT_COURIER_ON_CART
        );
    }


    public void enhancedECommerceGoToCheckoutStep2(Map<String, Object> cartMap, String transactionId, boolean isTradeIn) {
        String eventCategory = EventCategory.COURIER_SELECTION;
        if (isTradeIn) {
            eventCategory = EventCategory.COURIER_SELECTION_TRADE_IN;
        }
        Map<String, Object> dataLayer = DataLayer.mapOf(
                ConstantTransactionAnalytics.Key.EVENT, EventName.CHECKOUT,
                ConstantTransactionAnalytics.Key.EVENT_CATEGORY, eventCategory,
                ConstantTransactionAnalytics.Key.EVENT_ACTION, EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.Key.EVENT_LABEL, EventLabel.SUCCESS,
                ConstantTransactionAnalytics.Key.PAYMENT_ID, transactionId,
                ConstantTransactionAnalytics.Key.E_COMMERCE, cartMap,
                ConstantTransactionAnalytics.Key.CURRENT_SITE, ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void flushEnhancedECommerceGoToCheckoutStep2() {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                ConstantTransactionAnalytics.Key.E_COMMERCE, null,
                ConstantTransactionAnalytics.Key.CURRENT_SITE, null
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void eventClickCourierSelectionClickPilihAlamatLain() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_ALAMAT_LAIN
        );
    }

    public void eventClickCourierSelectionClickKirimKeBanyakAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KIRIM_KE_BANYAK_ALAMAT
        );
    }

    public void eventClickCourierSelectionClickTopDonasi() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_TOP_DONASI
        );
    }

    //PHASE 2

    public void eventClickCouponCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKuponSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.SUCCESS
        );
    }

    public void eventClickCouponCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKuponFailed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.FAILED
        );
    }

    public void eventClickCouponCourierSelectionClickKuponFromGunakanKodePromoAtauKuponSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.SUCCESS
        );
    }

    public void eventClickCouponCourierSelectionClickKuponFromGunakanKodePromoAtauKuponFailed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.FAILED
        );
    }

    public void eventClickBuyCourierSelectionClickPilihMetodePembayaranCourierNotComplete() {
        sendEventCategoryActionLabel(
                EventName.CLICK_BUY,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                EventLabel.COURIER_NOT_COMPLETE
        );
    }

    public void eventClickBuyPromoRedState() {
        sendEventCategoryActionLabel(
                EventName.CLICK_BUY,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                EventLabel.NOT_SUCCESS + " - " + EventLabel.PROMO_RED_STATE
        );
    }

    public void eventClickBuyCourierSelectionClickBayarFailedDropshipper() {
        sendEventCategoryActionLabel(
                EventName.CLICK_BUY,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BAYAR,
                EventLabel.FAILED_DROPSHIPPER
        );
    }

    public void eventClickCourierCourierSelectionClickUbahKurirAgentService(String agent, String service) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_UBAH_KURIR,
                agent + " - " + service
        );
    }

    //Robin Hood
    public void eventClickCourierCourierSelectionClickButtonDurasiPengiriman(String isBlackbox) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BUTTON_DURASI_PENGIRIMAN,
                isBlackbox
        );
    }

    public void eventClickCourierCourierSelectionClickXPadaDurasiPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_PADA_DURASI_PENGIRIMAN
        );
    }

    public void eventClickCourierCourierSelectionClickXPadaDurasiPengiriman(String eventLabelDuration) {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_PADA_DURASI_PENGIRIMAN
        );
    }

    public void eventClickCourierCourierSelectionClickChecklistPilihDurasiPengiriman(String eventLabelDuration) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CHECKLIST_PILIH_DURASI_PENGIRIMAN,
                eventLabelDuration
        );
    }

    // Implementation method has been removed due to absence of usage
    public void eventClickCourierCourierSelectionClickCtaButton() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CTA_BUTTON
        );
    }

    public void eventViewCourierCourierSelectionViewPreselectedCourierOption(String eventLabelPreselectedCourierPartner) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PRESELECTED_COURIER_OPTION,
                eventLabelPreselectedCourierPartner
        );
    }

    public void eventClickCourierCourierSelectionClickUbahKurir() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_UBAH_KURIR
        );
    }

    public void eventClickCourierCourierSelectionClickChangeCourierOption(String eventLabelPreselectedCourierPartner) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CHANGE_COURIER_OPTION,
                eventLabelPreselectedCourierPartner
        );
    }

    public void eventClickCourierCourierSelectionClickXPadaKurirPengiriman() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_PADA_KURIR_PENGIRIMAN
        );
    }

    public void eventClickCourierCourierSelectionClickUbahDurasi() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_UBAH_DURASI
        );
    }

    public void eventViewPromoAutoApply() {
        sendEventCategoryActionLabel(
                EventName.VIEW_PROMO,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PROMO_ELIGBLE_APPLY,
                EventLabel.CHECKOUT_COUPON_AUTO_APPLY
        );
    }

    public void eventViewPromoManualApply(String type) {
        sendEventCategoryActionLabel(
                EventName.VIEW_PROMO,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PROMO_ELIGBLE_APPLY,
                String.format(EventLabel.CHECKOUT_COUPON_OR_PROMO_MANUAL_APPLY, type)
        );
    }

    // Year End Promo
    public void eventViewPreselectedCourierOption(int shippingProductId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PRESELECTED_COURIER_OPTION,
                String.valueOf(shippingProductId)
        );
    }

    public void eventViewDuration(boolean isCourierPromo, String duration) {
        String eventLabel = isCourierPromo ? EventLabel.PROMO + EventLabel.SEPARATOR + duration
                : EventLabel.NON_PROMO + EventLabel.SEPARATOR + duration;
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_DURATION,
                eventLabel
        );
    }

    public void eventViewCourierOption(boolean isCourierPromo, int shippingProductId) {
        String eventLabel = isCourierPromo ? EventLabel.PROMO + EventLabel.SEPARATOR + shippingProductId
                : EventLabel.NON_PROMO + EventLabel.SEPARATOR + shippingProductId;
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_COURIER_OPTION,
                eventLabel
        );
    }

    public void eventClickChecklistPilihDurasiPengiriman(boolean isCourierPromo, String duration, boolean isCod, String shippingPriceMin, String shippingPriceHigh) {
        String eventLabel = (isCourierPromo ? EventLabel.PROMO : EventLabel.NON_PROMO)
                + EventLabel.SEPARATOR + duration
                + EventLabel.SEPARATOR + (isCod ? EventLabel.COD : "")
                + EventLabel.SEPARATOR + shippingPriceMin
                + EventLabel.SEPARATOR + shippingPriceHigh;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CHECKLIST_PILIH_DURASI_PENGIRIMAN,
                eventLabel
        );
    }

    public void eventClickChangeCourierOption(boolean isCourierPromo, int shippingProductId, boolean isCod) {
        String eventLabel = (isCourierPromo ? EventLabel.PROMO : EventLabel.NON_PROMO)
                + EventLabel.SEPARATOR + shippingProductId
                + EventLabel.SEPARATOR + (isCod ? EventLabel.COD : "");
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CHANGE_COURIER_OPTION,
                eventLabel
        );
    }

    // Trade In
    public void eventViewCheckoutPageTradeIn() {
        sendEventCategoryAction("",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.VIEW_CHECKOUYT_PAGE_TRADE_IN
        );
    }

    public void eventClickGantiNomor() {
        sendEventCategoryAction("",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_GANTI_NOMOR
        );
    }

    public void eventClickButtonPilihDurasi() {
        sendEventCategoryAction("",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_BUTTON_PILIH_DURASI
        );
    }

    public void eventClickKurirTradeIn(String label) {
        sendEventCategoryActionLabel("",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_KURIR_TRADE_IN,
                label
        );
    }

    public void eventClickBayarTradeInFailed() {
        sendEventCategoryActionLabel("",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_BAYAR,
                EventLabel.FAILED
        );
    }

    public void eventClickBayarCourierNotComplete() {
        sendEventCategoryActionLabel("",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_BAYAR,
                EventLabel.COURIER_NOT_COMPLETE
        );
    }

    public void eventClickShowMerchantVoucherList() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PILIH_MERCHANT_VOUCHER);
    }

    public void eventClickPakaiMerchantVoucherManualInputSuccess(String promoCode) {
        String label = EventLabel.SUCCESS + " - " + promoCode;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER_MANUAL_INPUT,
                label
        );
    }

    public void eventClickPakaiMerchantVoucherManualInputError(String errorMsg) {
        String label = EventLabel.ERROR + " - " + errorMsg;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER_MANUAL_INPUT,
                label
        );
    }

    public void eventClickPakaiMerchantVoucherSuccess(String promoCode) {
        String label = EventLabel.SUCCESS + " - " + promoCode;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER,
                label
        );
    }

    public void eventClickPakaiMerchantVoucherError(String errorMsg) {
        String label = EventLabel.ERROR + " - " + errorMsg;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER,
                label
        );
    }

    public void eventClickDetailMerchantVoucher(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_DETAIL_MERCHANT_VOUCHER,
                promoCode
        );
    }

    public void eventClickTickerMerchantVoucher(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_TICKER_MERCHANT_VOUCHER,
                promoCode
        );
    }

    public void eventClickHapusPromoXOnTicker(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_HAPUS_PROMO_X_ON_TICKER,
                promoCode
        );
    }

    public void eventViewDetailMerchantVoucher(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_DETAIL_MERCHANT_VOUCHER,
                promoCode
        );
    }

    public void eventClickOtherPromoOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_LIHAT_PROMO_LAINNYA_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventClickCancelPromoOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BATALKAN_PROMO_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventClickCaraPakaiPromoOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CARA_PAKAI_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventClickKetentuanOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KETENTUAN_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventViewPopUpPromoDisable() {
        sendEventCategoryAction(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_POPUP_PROMO_DISABLE
        );
    }

    // Logistic Promo
    public void eventClickPromoLogisticTicker(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_PROMO_LOGISTIC_TICKER,
                promoCode
        );
    }

    public void eventClickLanjutkanTerapkanPromoSuccess(String promoCode) {
        String label = EventLabel.SUCCESS + " - " + promoCode;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_LANJUTKAN_TERAPKAN_PROMO,
                label
        );
    }

    public void eventClickLanjutkanTerapkanPromoError(String errorMsg) {
        String label = EventLabel.ERROR + " - " + errorMsg;
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_LANJUTKAN_TERAPKAN_PROMO,
                label
        );
    }

    public void eventClickBatalTerapkanPromo(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BATAL_TERAPKAN_PROMO,
                promoCode
        );
    }

    public void eventSelectPromoConflict(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.SELECT_PROMO_PROMO_KONFLIK,
                promoCode
        );
    }

    public void eventSubmitPromoConflict(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_SUBMIT_PROMO_CONFLICT,
                promoCode
        );
    }

    // Promo not eligible bottomsheet
    public void eventClickLanjutkanOnErrorPromoConfirmation() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_LANJUTKAN_ON_ERROR_PROMO_CONFIRMATION
        );
    }

    public void eventClickBatalOnErrorPromoConfirmation() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BATAL_ON_ERROR_PROMO_CONFIRMATION
        );
    }

    public void eventViewPopupErrorPromoConfirmation() {
        sendEventCategoryAction(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_POP_UP_ERROR_PROMO_CONFIRMATION
        );
    }

}
