package com.tokopedia.purchase_platform.common.analytics;

import android.text.TextUtils;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey;


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

    public void eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT
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

    @Deprecated
    public void eventClickAtcCourierSelectionClickKuponFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
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

    public void sendEnhancedECommerceCheckout(Map<String, Object> cartMap,
                                              Map<String, String> tradeInCustomDimension,
                                              String transactionId,
                                              String eventCategory,
                                              String eventAction,
                                              String eventLabel) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                ConstantTransactionAnalytics.Key.EVENT, EventName.CHECKOUT,
                ConstantTransactionAnalytics.Key.EVENT_CATEGORY, eventCategory,
                ConstantTransactionAnalytics.Key.EVENT_ACTION, eventAction,
                ConstantTransactionAnalytics.Key.EVENT_LABEL, eventLabel,
                ConstantTransactionAnalytics.Key.E_COMMERCE, cartMap,
                ConstantTransactionAnalytics.Key.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        );
        if (tradeInCustomDimension != null && tradeInCustomDimension.size() > 0) {
            dataLayer.putAll(tradeInCustomDimension);
        }
        if (!TextUtils.isEmpty(transactionId)) {
            dataLayer.put(ConstantTransactionAnalytics.Key.PAYMENT_ID, transactionId);
        }
        sendEnhancedEcommerce(dataLayer);
    }

    public void flushEnhancedECommerceCheckout() {
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

    //Robin Hood
    public void eventClickCourierCourierSelectionClickXPadaDurasiPengiriman() {
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

    public void eventViewCourierCourierSelectionViewPreselectedCourierOption(String eventLabelPreselectedCourierPartner) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PRESELECTED_COURIER_OPTION,
                eventLabelPreselectedCourierPartner
        );
    }

    public void eventClickCourierCourierSelectionClickUbahKurir(String label) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_UBAH_KURIR,
                label
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

    //on success use merchant voucher from list
    public void eventClickUseMerchantVoucherSuccess(String promoCode, String promoId, Boolean isFromList) {
        String eventAction = isFromList ? EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER : EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER;
        Map<String, Object> eventMap = createEventMap(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                eventAction,
                EventLabel.SUCCESS + " - " + promoCode
        );
        eventMap.put(ConstantTransactionAnalytics.Key.PROMO_ID, promoId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    //impression on user merchant voucher list
    public void eventImpressionUseMerchantVoucher(String voucherId, Map<String, Object> ecommerceMap) {
        Map<String, Object> eventMap = createEventMap(
                EventName.PROMO_VIEW,
                EventCategory.COURIER_SELECTION,
                EventAction.IMPRESSION_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
                ""
        );
        eventMap.put(ConstantTransactionAnalytics.Key.PROMO_ID, voucherId);
        eventMap.put(ConstantTransactionAnalytics.Key.E_COMMERCE, ecommerceMap);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    //on merchant voucher click detail
    public void eventClickDetailMerchantVoucher(Map<String, Object> ecommerceMap, String voucherId, String promoCode) {
        Map<String, Object> eventMap = createEventMap(
                EventName.PROMO_CLICK,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
                promoCode
        );
        eventMap.put(ConstantTransactionAnalytics.Key.PROMO_ID, voucherId);
        eventMap.put(ConstantTransactionAnalytics.Key.E_COMMERCE, ecommerceMap);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    //on error use merchant voucher
    public void eventClickUseMerchantVoucherError(String errorMsg, String promoId, Boolean isFromList) {
        String eventAction = isFromList ? EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER : EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER;
        Map<String, Object> eventMap = createEventMap(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                eventAction,
                EventLabel.ERROR + " - " + errorMsg
        );
        eventMap.put(ConstantTransactionAnalytics.Key.PROMO_ID, promoId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    private Map<String, Object> createEventMap(String event, String category, String action, String label) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(ConstantTransactionAnalytics.Key.EVENT, event);
        eventMap.put(ConstantTransactionAnalytics.Key.EVENT_CATEGORY, category);
        eventMap.put(ConstantTransactionAnalytics.Key.EVENT_ACTION, action);
        eventMap.put(ConstantTransactionAnalytics.Key.EVENT_LABEL, label);
        return eventMap;
    }

    public void eventCancelPromoStackingLogistic() {
        sendEventCategoryAction(
                "",
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_X_ON_PROMO_STACKING_LOGISTIC
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

    public void eventViewPromoLogisticTicker(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PROMO_LOGISTIC_TICKER,
                promoCode
        );
    }

    public void eventViewCourierImpressionErrorCourierNoAvailable() {
        sendEventCategoryAction(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.IMPRESSION_ERROR_COURIER_NO_AVAILABLE
        );
    }

    public void eventViewHelpPopUpAfterErrorInCheckout() {
        sendEventCategoryAction(
                EventName.VIEW_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_HELP_POP_UP_AFTER_ERROR_IN_CHECKOUT
        );
    }

    public void eventClickReportOnHelpPopUpInCheckout() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_REPORT_ON_HELP_POP_UP_IN_CHECKOUT
        );
    }

    public void eventClickCloseOnHelpPopUpInCheckout() {
        sendEventCategoryAction(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CLOSE_ON_HELP_POP_UP_IN_CHECKOUT
        );
    }

    public void eventViewInformationAndWarningTickerInCheckout(String tickerId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_INFORMATION_AND_WARNING_TICKER_IN_CHECKOUT,
                tickerId
        );
    }

    public void eventViewPromoAfterAdjustItem(String msg) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PROMO_MESSAGE,
                msg
        );
    }

    public void eventViewPromoLogisticTickerDisable(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_PROMO_LOGISTIC_TICKER_DISABLE,
                promoCode
        );
    }

    public void eventViewPopupPriceIncrease(String eventLabel) {
        sendEventCategoryActionLabel(EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_POP_UP_PRICE_INCREASE,
                eventLabel);
    }

    public void eventClickCheckboxDonation(boolean check) {
        sendEventCategoryActionLabel(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_DONATION,
                check ? "check" : "uncheck"
        );
    }

    public void eventViewAutoCheckDonation(String userId) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_AUTO_CHECK_ON_DONATION,
                ""
        );
        gtmMap.put(ExtraKey.USER_ID, userId);
        sendGeneralEvent(gtmMap);
    }

    public void eventViewCampaignDialog(long productId, String userId) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_POP_UP_MESSAGE_TIMER,
                String.valueOf(productId)
        );
        gtmMap.put(ExtraKey.USER_ID, userId);
        sendGeneralEvent(gtmMap);
    }

    public void eventClickBelanjaLagiOnDialog(long productId, String userId) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_BELANJA_LAGI_ON_POP_UP,
                String.valueOf(productId)
        );
        gtmMap.put(ExtraKey.USER_ID, userId);
        sendGeneralEvent(gtmMap);
    }

    public void eventViewSummaryTransactionTickerCourierNotComplete(String userId) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE,
                ""
        );
        gtmMap.put(ExtraKey.USER_ID, userId);
        sendGeneralEvent(gtmMap);
    }

    public void clickCekOnSummaryTransactionTickerCourierNotComplete(String userId) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_CEK_ON_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE,
                ""
        );
        gtmMap.put(ExtraKey.USER_ID, userId);
        sendGeneralEvent(gtmMap);
    }

    public void eventViewTickerProductLevelErrorInCheckoutPage(String shopId, String errorMessage) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_TICKER_PRODUCT_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                shopId + " - " + errorMessage
        );
        gtmMap.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmMap.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        sendGeneralEvent(gtmMap);
    }

    public void eventViewTickerOrderLevelErrorInCheckoutPage(String shopId, String errorMessage) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_TICKER_ORDER_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                shopId + " - " + errorMessage
        );
        gtmMap.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmMap.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        sendGeneralEvent(gtmMap);
    }

    public void eventClickLihatOnTickerErrorOrderLevelErrorInCheckoutPage(String shopId, String errorMessage) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_LIHAT_ON_TICKER_ORDER_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                shopId + " - " + errorMessage
        );
        gtmMap.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmMap.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        sendGeneralEvent(gtmMap);
    }

    public void eventClickRefreshWhenErrorLoadCourier() {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_REFRESH_WHEN_ERROR_LOAD_COURIER,
                ""
        );
        gtmMap.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmMap.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        sendGeneralEvent(gtmMap);
    }

    public void eventViewErrorInCourierSection(String errorMessage) {
        Map<String, Object> gtmMap = TrackAppUtils.gtmData(
                EventName.VIEW_COURIER_IRIS,
                EventCategory.COURIER_SELECTION,
                EventAction.VIEW_ERROR_IN_COURIER_SECTION,
                errorMessage
        );
        gtmMap.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmMap.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        sendGeneralEvent(gtmMap);
    }
}
