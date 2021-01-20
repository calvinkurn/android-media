package com.tokopedia.purchase_platform.common.analytics;

import android.content.Context;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.iris.util.ConstantKt;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.Key;


/**
 * @author anggaprasetiyo on 18/05/18.
 */
public class CheckoutAnalyticsCart extends TransactionAnalytics {
    IrisSession irisSession;

    public CheckoutAnalyticsCart(Context context) {
        irisSession = new IrisSession(context);
    }

    @Deprecated
    public void eventClickAtcCartClickKuponFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCartClickKodePromoFromGunakanPromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCartClickKuponSayaFromGunakanPromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    @Deprecated
    public void eventClickAtcCartClickGunakanKodeFormGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCartClickPilihSemuaFromHapus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PILIH_SEMUA_FROM_HAPUS
        );
    }

    public void eventClickAtcCartClickChecklistBoxFromHapus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_CHECKLIST_BOX_FROM_HAPUS
        );
    }

    public void eventClickAtcCartClickHapusFromHapus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_FROM_HAPUS
        );
    }

    public void eventClickAtcCartClickGunakanKodePromoAatauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventClickAtcCartClickHapusOnTopRightCorner() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_ON_TOP_RIGHT_CORNER
        );
    }

    public void eventClickAtcCartClickShop(String shopId, String shopName) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_SHOP,
                shopId + " - " + shopName
        );
    }

    public void eventClickAtcCartClickProductName(String productName) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PRODUCT_NAME,
                productName
        );
    }

    public void eventClickAtcCartClickButtonPlus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BUTTON_PLUS
        );
    }

    public void eventClickAtcCartClickButtonMinus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BUTTON_MIN
        );
    }

    public void eventClickAtcCartClickTrashBin() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_TRASH_BIN
        );
    }

    public void eventClickAtcCartClickArrowBack() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ARROW_BACK
        );
    }

    public void eventClickAtcCartClickXOnBannerPromoCode() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_X_ON_BANNER_PROMO_CODE
        );
    }

    public void eventClickAtcCartClickBelanjaSekarangOnEmptyCart() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BELANJA_SEKARANG_ON_EMPTY_CART
        );
    }

    public void eventClickAtcCartClickAddFromWishlistOnEmptyCart() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ADD_FROM_WISHLIST_ON_EMPTY_CART
        );
    }

    public void eventViewAtcCartImpressionCartEmpty() {
        sendEventCategoryAction(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.IMPRESSION_CART_EMPTY
        );
    }

    @Deprecated
    public void eventClickAtcCartClickArrowBackFromHapus() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ARROW_BACK_FROM_HAPUS
        );
    }

    public void eventClickAtcCartClickTulisCatatan() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_TULIS_CATATAN
        );
    }

    public void eventClickAtcCartClickInputQuantity(String qty) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_INPUT_QUANTITY,
                qty
        );
    }

    @Deprecated
    public void eventClickAtcCartClickXFromGunakanKodePromoAtauKupon() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        );
    }

    public void eventViewAtcCartImpressionOnPopUpKupon() {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.IMPRESSION_ON_POP_UP_KUPON,
                EventLabel.KUOTA_PENUKARAN
        );
    }

    public void enhanceECommerceRemoveFromCartClickHapusFromClickHapus(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_FROM_CLICK_HAPUS,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhanceECommerceRemoveFromCartClickHapusDanTambahWishlistFromClickHapus(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_CLICK_HAPUS,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromTrashBin(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_TRASH_BIN,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceRemoveFromCartClickHapusFromTrashBin(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_FROM_TRASH_BIN,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_PRODUK_BERKENDALA,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceRemoveFromCartClickHapusDanTambahWishlistFromHapusProdukBerkendala(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_HAPUS_PRODUK_BERKENDALA,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.REMOVE_FROM_CART,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_HAPUS_FROM_HAPUS_PRODUK_BERKENDALA,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    @Deprecated
    public void sendEnhancedECommerce(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }


    private void sendEnhancedECommerce(int step, Map<String, Object> cartMap, String eventLabel) {

        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, step == 0 ? EventAction.VIEW_CART_PAGE : EventAction.CLICK_CHECKOUT,
                Key.EVENT_LABEL, eventLabel,
                Key.E_COMMERCE, cartMap,
                Key.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        );
        dataLayer.put(ConstantKt.KEY_SESSION_IRIS, irisSession.getSessionId());

        sendEnhancedEcommerce(dataLayer);
    }

    private void flushEnhancedECommerce() {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.E_COMMERCE, null,
                Key.CURRENT_SITE, null
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceCartLoadedStep0(Map<String, Object> cartMap) {
        sendEnhancedECommerce(0, cartMap, "");
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessDefault(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_DEFAULT);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessCheckAll(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShop(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(1, cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT);
        }
        flushEnhancedECommerce();
    }

    //PHASE 2
    public void eventClickCheckoutCartClickCheckoutFailed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT,
                EventCategory.CART,
                EventAction.CLICK_CHECKOUT,
                EventLabel.FAILED
        );
    }

    public void eventClickCheckoutCartClickPilihSemuaProdukChecklist() {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT,
                EventCategory.CART,
                EventAction.CLICK_PILIH_SEMUA_PRODUK,
                EventLabel.CHECKLIST
        );
    }

    public void eventClickCheckoutCartClickPilihSemuaProdukUnChecklist() {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT,
                EventCategory.CART,
                EventAction.CLICK_PILIH_SEMUA_PRODUK,
                EventLabel.UN_CHECKLIST
        );
    }

    public void eventClickCouponCartClickGunakanKodeFormGunakanKodePromoAtauKuponSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.SUCCESS
        );
    }

    public void eventClickCouponCartClickGunakanKodeFormGunakanKodePromoAtauKuponFailed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.FAILED
        );
    }

    public void eventClickCouponCartClickKuponFromGunakanKodePromoAtauKuponSuccess() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.CART,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.SUCCESS
        );
    }

    public void eventClickCouponCartClickKuponFromGunakanKodePromoAtauKuponFailed() {
        sendEventCategoryActionLabel(
                EventName.CLICK_COUPON,
                EventCategory.CART,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                EventLabel.FAILED
        );
    }

    public void eventViewPromoAutoApply() {
        sendEventCategoryActionLabel(
                EventName.VIEW_PROMO,
                EventCategory.CART,
                EventAction.VIEW_PROMO_ELIGBLE_APPLY,
                EventLabel.CHECKOUT_COUPON_AUTO_APPLY
        );
    }

    public void eventViewPromoManualApply(String type) {
        sendEventCategoryActionLabel(
                EventName.VIEW_PROMO,
                EventCategory.CART,
                EventAction.VIEW_PROMO_ELIGBLE_APPLY,
                String.format(EventLabel.CHECKOUT_COUPON_OR_PROMO_MANUAL_APPLY, type)
        );
    }

    // Empty cart
    public void eventClickLihatLainnya() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_LIHAT_LAINNYA,
                ""
        );
    }

    public void eventClickLihatSemuaWishlist() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_LIHAT_SEMUA_WISHLIST,
                ""
        );
    }

    public void eventClickLihatSemuaLastSeen() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_LIHAT_SEMUA_LAST_SEEN,
                ""
        );
    }

    public void enhancedEcommerceProductViewWishList(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_VIEW,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.VIEW_PRODUCT,
                Key.EVENT_LABEL, EventLabel.PRODUCT_WISHLIST,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceProductViewLastSeen(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_VIEW,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.VIEW_PRODUCT,
                Key.EVENT_LABEL, EventLabel.PRODUCT_LAST_SEEN,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceProductViewRecommendationOnEmptyCart(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_VIEW,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.VIEW_PRODUCT,
                Key.EVENT_LABEL, EventLabel.PRODUCT_RECOMMENDATION,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceClickProductWishListOnEmptyCart(String position, Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_CLICK,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_PRODUCT_WISHLIST,
                Key.EVENT_LABEL, position,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceClickProductWishListOnCartList(String position, Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_CLICK,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_PRODUCT_WISHLIST_ON_CART_LIST,
                Key.EVENT_LABEL, position,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceClickProductLastSeenOnEmptyCart(String position, Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_CLICK,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_PRODUCT_LAST_SEEN,
                Key.EVENT_LABEL, position,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceClickProductLastSeenOnCartList(String position, Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_CLICK,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_PRODUCT_LAST_SEEN_ON_CART_LIST,
                Key.EVENT_LABEL, position,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceClickProductRecommendationOnEmptyCart(String position, Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_CLICK,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_PRODUCT_RECOMMENDATION,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void eventViewCartListFinishRender() {
        sendEventCategoryAction(
                EventName.VIEW_CART,
                EventCategory.CART,
                EventAction.VIEW_CART_LIST
        );
    }

    // PROMO STACKING
    public void eventClickPilihMerchantVoucher() {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PILIH_MERCHANT_VOUCHER,
                ""
        );
    }

    //impression on user merchant voucher list
    public void eventImpressionUseMerchantVoucher(String voucherId, Map<String, Object> ecommerceMap) {
        Map<String, Object> eventMap = createEventMap(
                EventName.PROMO_VIEW,
                EventCategory.CART,
                EventAction.IMPRESSION_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
                ""
        );
        eventMap.put(Key.PROMO_ID, voucherId);
        eventMap.put(Key.E_COMMERCE, ecommerceMap);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    //on success use merchant voucher from list
    public void eventClickUseMerchantVoucherSuccess(String promoCode, String promoId, Boolean isFromList) {
        String eventAction = isFromList ? EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER : EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER;
        Map<String, Object> eventMap = createEventMap(
                EventName.CLICK_ATC,
                EventCategory.CART,
                eventAction,
                EventLabel.SUCCESS + " - " + promoCode
        );
        eventMap.put(Key.PROMO_ID, promoId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    //on error use merchant voucher
    public void eventClickUseMerchantVoucherFailed(String errorMessage, String promoId, Boolean isFromList) {
        String eventAction = isFromList ? EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER : EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER;
        Map<String, Object> eventMap = createEventMap(
                EventName.CLICK_ATC,
                EventCategory.CART,
                eventAction,
                EventLabel.ERROR + " - " + errorMessage
        );
        eventMap.put(Key.PROMO_ID, promoId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    //on merchant voucher click detail
    public void eventClickDetailMerchantVoucher(Map<String, Object> ecommerceMap, String voucherId, String promoCode) {
        Map<String, Object> eventMap = createEventMap(
                EventName.PROMO_CLICK,
                EventCategory.CART,
                EventAction.CLICK_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
                promoCode
        );
        eventMap.put(Key.PROMO_ID, voucherId);
        eventMap.put(Key.E_COMMERCE, ecommerceMap);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    private Map<String, Object> createEventMap(String event, String category, String action, String label) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put(Key.EVENT, event);
        eventMap.put(Key.EVENT_CATEGORY, category);
        eventMap.put(Key.EVENT_ACTION, action);
        eventMap.put(Key.EVENT_LABEL, label);
        return eventMap;
    }

    public void eventClickTickerMerchantVoucher(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_TICKER_MERCHANT_VOUCHER,
                promoCode
        );
    }

    public void eventClickHapusPromoXOnTicker(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_PROMO_X_ON_TICKER,
                promoCode
        );
    }

    public void eventViewDetailMerchantVoucher(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_DETAIL_MERCHANT_VOUCHER,
                promoCode
        );
    }

    public void eventClickLihatPromoLainnyaOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_LIHAT_PROMO_LAINNYA_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventClickBatalkanPromoOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BATALKAN_PROMO_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventClickCaraPakaiOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_CARA_PAKAI_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventClickKetentuanOnVoucherDetail(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KETENTUAN_ON_VOUCHER_DETAIL,
                promoCode
        );
    }

    public void eventSelectPromoPromoKonflik(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.SELECT_PROMO_PROMO_KONFLIK,
                promoCode
        );
    }

    public void eventClickSubmitPromoKonflik(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_SUBMIT_PROMO_CONFLICT,
                promoCode
        );
    }

    public void eventViewPopupPromoDisable() {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.CLICK_SUBMIT_PROMO_CONFLICT,
                ""
        );
    }

    public void eventViewTickerPriceDecrease(String productId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_TICKER_PRICE_DECREASE,
                productId
        );
    }

    public void eventViewTickerStockDecreaseAndAlreadyAtcByOtherUser(String productId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_TICKER_STOCK_DECREASE_AND_ALREADY_ATC_BY_OTHER_USER,
                productId
        );
    }

    public void enhancedEcommerceViewRecommendationOnCart(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_VIEW,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.IMPRESSION_ON_PRODUCT_RECOMMENDATION,
                Key.EVENT_LABEL, "",
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void eventViewTickerOutOfStock(String productId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_TICKER_OUT_OF_STOCK,
                productId
        );
    }

    public void eventClickMoreLikeThis() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_MORE_LIKE_THIS
        );
    }

    public void eventViewErrorWhenCheckout(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_ERROR_ON_CHECKOUT,
                EventLabel.NOT_SUCCESS + " - " + errorMessage
        );
    }

    public void eventViewInformationAndWarningTickerInCart(String tickerId) {
        sendEventCategoryActionLabel(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_INFORMATION_AND_WARNING_TICKER_IN_CART,
                tickerId
        );
    }

    public void eventClickAddWishlistOnProductRecommendation() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION
        );
    }

    public void eventClickAddWishlistOnProductRecommendationEmptyCart() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART
        );
    }

    public void eventClickRemoveWishlistOnProductRecommendation() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION
        );
    }

    public void eventClickRemoveWishlistOnProductRecommendationEmptyCart() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART
        );
    }

    public void sendEnhancedECommerceAddToCart(Map<String, Object> atcMap,
                                               String eventCategory,
                                               String eventAction,
                                               String eventLabel) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                ConstantTransactionAnalytics.Key.EVENT, EventName.ADD_TO_CART,
                ConstantTransactionAnalytics.Key.EVENT_CATEGORY, eventCategory,
                ConstantTransactionAnalytics.Key.EVENT_ACTION, eventAction,
                ConstantTransactionAnalytics.Key.EVENT_LABEL, eventLabel,
                ConstantTransactionAnalytics.Key.E_COMMERCE, atcMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void eventAddWishlistAvailableSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.ADD_WISHLIST_AVAILABLE_SECTION,
                productId
        );
    }

    public void eventRemoveWishlistAvailableSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.REMOVE_WISHLIST_AVAILABLE_SECTION,
                productId
        );
    }

    public void eventAddWishlistUnavailableSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.ADD_WISHLIST_UNAVAILABLE_SECTION,
                productId
        );
    }

    public void eventRemoveWishlistUnvailableSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.REMOVE_WISHLIST_UNAVAILABLE_SECTION,
                productId
        );
    }

    public void eventAddWishlistLastSeenSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.ADD_WISHLIST_LAST_SEEN,
                productId
        );
    }

    public void eventRemoveWishlistLastSeenSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.REMOVE_WISHLIST_LAST_SEEN,
                productId
        );
    }

    public void eventAddWishlistWishlistsSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.ADD_WISHLIST_WISHLIST,
                productId
        );
    }

    public void eventRemoveWishlistWishlistsSection(boolean isEmptyCart, String productId) {
        sendEventCategoryActionLabel(
                EventName.CART,
                isEmptyCart ? EventCategory.EMPTY_CART : EventCategory.CART,
                EventAction.REMOVE_WISHLIST_WISHLIST,
                productId
        );
    }

    public void sendEventDeleteInsurance(String insuranceTitle) {

        sendEventCategoryActionLabel(
                "",
                EventCategory.FIN_INSURANCE_CART,
                EventAction.FIN_INSURANCE_CART_DELETE,
                String.format("cart page - %s", insuranceTitle)
        );
    }

    public void sendEventInsuranceImpression(String title) {
        sendEventCategoryActionLabel(
                "",
                EventCategory.FIN_INSURANCE_CART,
                EventAction.FIN_INSURANCE_CART_IMPRESSION,
                String.format("cart - %s", title)
        );
    }

    public void sendEventChangeInsuranceState(boolean isChecked, String title) {
        String eventLabel = "";
        if (isChecked) {
            eventLabel = String.format("cart page - tick %s", title);
        } else {
            eventLabel = String.format("cart page - untick %s", title);
        }
        sendEventCategoryActionLabel(
                "",
                EventCategory.FIN_INSURANCE_CART,
                EventAction.FIN_INSURANCE_STATE_CHANGE,
                eventLabel
        );
    }

    public void sendEventPurchaseInsurance(String userID, String productId, String title) {
        Map<String, Object> mapEvent = TrackAppUtils.gtmData(
                "",
                EventCategory.FIN_INSURANCE_CART,
                EventAction.FIN_INSURANCE_CLICK_BUY,
                String.format("cart page - %s", title)
        );
        mapEvent.put("userId", userID);
        mapEvent.put("productId", productId);
        sendGeneralEvent(mapEvent);
    }

    public void eventClickBrowseButtonOnTickerProductContainTobacco() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BROWSE_BUTTON_ON_TICKER_PRODUCT_CONTAIN_TOBACCO
        );
    }

    public void eventViewTickerProductContainTobacco() {
        sendEventCategoryAction(
                EventName.VIEW_ATC_IRIS,
                EventCategory.CART,
                EventAction.VIEW_TICKER_PRODUCT_CONTAIN_TOBACCO
        );
    }

    public void eventClickHapusButtonOnProductContainTobacco() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO
        );
    }

    public void eventClickTrashIconButtonOnProductContainTobacco() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_TRASH_ICON_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO
        );
    }

    // Cart Revamp
    public void eventViewRemainingStockInfo(String userId, String productId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.VIEW_ATC_IRIS,
                EventCategory.CART,
                EventAction.VIEW_REMAINING_STOCK_INFO,
                productId
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventViewInformationLabelInProductCard(String userId, String productId, String informationLabel) {
        Map<String, Object> gtmData = getGtmData(
                EventName.VIEW_ATC_IRIS,
                EventCategory.CART,
                EventAction.VIEW_INFORMATION_LABEL_IN_PRODUCT_CARD,
                productId + " - " + informationLabel
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickDetailTagihan(String userId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_DETAIL_TAGIHAN,
                ""
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickMoveToWishlistOnAvailableSection(String userId, String productId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.ADD_WISHLIST_CART_LOGIN,
                productId + " - available section"
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickMoveToWishlistOnUnavailableSection(String userId, String productId, String errorType) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.ADD_WISHLIST_CART_LOGIN,
                productId + " - " + errorType + " - unavailable section"
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickDeleteProductOnUnavailableSection(String userId, String productId, String errorType) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_DELETE_PRODUCT_ON_UNAVAILABLE_SECTION,
                productId + " - " + errorType
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickSeeOtherProductOnUnavailableSection(String userId, String productId, String errorType) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_LIHAT_PRODUK_SERUPA_ON_UNAVAILABLE_SECTION,
                productId + " - " + errorType
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickCheckoutMelaluiBrowserOnUnavailableSection(String userId, String productId, String errorType) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_CHECKOUT_MELALUI_BROWSER_ON_UNAVAILABLE_SECTION,
                productId + " - " + errorType
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickDeleteAllUnavailableProduct(String userId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_DELETE_ALL_UNAVAILABLE_PRODUCT,
                ""
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickAccordionButtonOnUnavailableProduct(String userId, String buttonWording) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ACCORDION_ON_UNAVAILABLE_PRODUCT.replace("%s", buttonWording.toLowerCase(Locale.getDefault())),
                ""
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickUndoAfterDeleteProduct(String userId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_UNDO_AFTER_DELETE_PRODUCT,
                ""
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventViewErrorPageWhenLoadCart(String userId, String errorType) {
        Map<String, Object> gtmData = getGtmData(
                EventName.VIEW_ATC_IRIS,
                EventCategory.CART,
                EventAction.VIEW_ERROR_PAGE_WHEN_LOAD_CART,
                ""
        );
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickWishlistIcon(String userId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_WISHLIST,
                EventCategory.CART,
                EventAction.CLICK_WISHLIST_ICON_IN_CART_PAGE,
                ""
        );
        gtmData.put(ExtraKey.USER_ID, userId);
        gtmData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);

        sendGeneralEvent(gtmData);
    }

    public void eventClickRemoveWishlist(String userId, String productId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_WISHLIST,
                EventCategory.CART,
                EventAction.REMOVE_WISHLIST_CART_LOGIN,
                productId + " - wishlist section"
        );
        gtmData.put(ExtraKey.USER_ID, userId);
        gtmData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);

        sendGeneralEvent(gtmData);
    }

    public void eventClickFollowShop(String userId, String errorType, String shopId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_FOLLOW_SHOP_ON_UNAVAILABLE_SECTION,
                errorType + " - " + shopId
        );
        gtmData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM);
        gtmData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        gtmData.put(ExtraKey.USER_ID, userId);

        sendGeneralEvent(gtmData);
    }

    public void eventClickBackNavToolbar(String userId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_NAVIGATION_DRAWER,
                EventCategory.CART,
                EventAction.CLICK_BACK_BUTTON_NAV,
                ""
        );
        gtmData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        gtmData.put(ExtraKey.USER_ID, userId);
        gtmData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE);
        gtmData.put(ExtraKey.PAGE_TYPE, "");
        gtmData.put(ExtraKey.PAGE_PATH, "");

        sendGeneralEvent(gtmData);
    }

    public void eventClickTopNavMenuNavToolbar(String userId) {
        Map<String, Object> gtmData = getGtmData(
                EventName.CLICK_NAVIGATION_DRAWER,
                EventCategory.CART,
                EventAction.CLICK_GLOBAL_MENU_NAV,
                ""
        );
        gtmData.put(ExtraKey.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE);
        gtmData.put(ExtraKey.USER_ID, userId);
        gtmData.put(ExtraKey.BUSINESS_UNIT, CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE);
        gtmData.put(ExtraKey.PAGE_TYPE, "");
        gtmData.put(ExtraKey.PAGE_PATH, "");

        sendGeneralEvent(gtmData);
    }


    // Global checkbox resurrection

    public void eventCheckUncheckGlobalCheckbox(boolean isCheck) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT,
                EventCategory.CART,
                EventAction.CLICK_PILIH_SEMUA_PRODUK,
                isCheck ? EventLabel.CHECKLIST : EventLabel.UN_CHECKLIST
        );
    }

    public void eventClickGlobalDelete() {
        sendEventCategoryAction(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_ON_TOP_RIGHT_CORNER
        );
    }

}
