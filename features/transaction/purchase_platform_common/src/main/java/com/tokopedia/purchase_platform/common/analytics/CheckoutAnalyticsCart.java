package com.tokopedia.purchase_platform.common.analytics;

import android.os.Bundle;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.Map;

import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.CustomDimension;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventLabel;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName;
import static com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.Key;


/**
 * @author anggaprasetiyo on 18/05/18.
 */
public class CheckoutAnalyticsCart extends TransactionAnalytics {

    public CheckoutAnalyticsCart() {

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

    public void eventClickAtcCartClickShopName(String shopName) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_SHOP_NAME,
                shopName
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


    private void sendEnhancedECommerce(Map<String, Object> cartMap, String eventLabel) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.VIEW_CART_PAGE,
                Key.EVENT_LABEL, eventLabel,
                Key.E_COMMERCE, cartMap,
                Key.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        );
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
        sendEnhancedECommerce(cartMap, "");
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessDefault(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_DEFAULT);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessCheckAll(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShop(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT);
        }
        flushEnhancedECommerce();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(Map<String, Object> cartMap, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT);
        }
        flushEnhancedECommerce();
    }

    // GTM v5 EE Step 1
    private void sendEnhancedECommerce(Bundle eCommerceBundle, String eventLabel) {

    }

    public void enhancedECommerceGoToCheckoutStep1SuccessDefault(Bundle eCommerceBundle, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_DEFAULT);
        }
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessCheckAll(Bundle eCommerceBundle, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL);
        }
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShop(Bundle eCommerceBundle, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP);
        }
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(Bundle eCommerceBundle, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT);
        }
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(Bundle eCommerceBundle, boolean eligibleCod) {
        if (eligibleCod) {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD);
        } else {
            sendEnhancedECommerce(eCommerceBundle, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT);
        }
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

    public void eventClickPakaiMerchantVoucherManualInputSuccess(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER_MANUAL_INPUT,
                "success - " + promoCode
        );
    }

    public void eventClickPakaiMerchantVoucherManualInputFailed(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER_MANUAL_INPUT,
                "error - " + errorMessage
        );
    }

    public void eventClickPakaiMerchantVoucherSuccess(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER,
                "success - " + promoCode
        );
    }

    public void eventClickPakaiMerchantVoucherFailed(String errorMessage) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PAKAI_MERCHANT_VOUCHER,
                "failed - " + errorMessage
        );
    }

    public void eventClickDetailMerchantVoucher(String promoCode) {
        sendEventCategoryActionLabel(
                EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_DETAIL_MERCHANT_VOUCHER,
                promoCode
        );
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

    public void sendEventDeleteInsurance() {

        sendEventCategoryActionLabel(
                "",
                "fin - cart page",
                "ins - click delete from cart",
                "cart page - macro insurance"
        );
    }

    public void sendEventChangeInsuranceState(boolean isChecked) {

        String eventLabel = "";

        if (isChecked) {
            eventLabel = "cart page - tick macro insurance";
        } else {
            eventLabel =  "cart page - untick macro insurance";
        }

        String eventAction = "";

        if (isChecked) {
            eventAction = "ins - click tick insurance for payment";
        } else {
            eventAction = "ins - click untick insurance for payment";
        }

        sendEventCategoryActionLabel(
                "",
                "fin - cart page",
                eventAction,
                eventLabel
        );
    }

}
