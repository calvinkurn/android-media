package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventLabel;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.Key;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.CustomDimension;


/**
 * @author anggaprasetiyo on 18/05/18.
 */
public class CheckoutAnalyticsCart extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsCart(AnalyticTracker analyticTracker) {
        super(analyticTracker);
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
    public void enhancedECommerceGoToCheckoutStep1(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }


    private void enhancedECommerceGoToCheckoutStep1(Map<String, Object> cartMap, String eventLabel) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.CHECKOUT,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_CHECKOUT,
                Key.EVENT_LABEL, eventLabel,
                Key.E_COMMERCE, cartMap,
                Key.CURRENT_SITE, CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        );
        sendEnhancedEcommerce(dataLayer);
    }

    private void flushEnhancedECommerceGoToCheckoutStep1() {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.E_COMMERCE, null,
                Key.CURRENT_SITE, null
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessDefault(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_DEFAULT);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessCheckAll(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShop(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT);
        flushEnhancedECommerceGoToCheckoutStep1();
    }



    public void enhancedECommerceGoToCheckoutStep1SuccessDefaultEligibleCod(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessCheckAllEligibleCod(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShopEligibleCod(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialProductEligibleCod(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD);
        flushEnhancedECommerceGoToCheckoutStep1();
    }

    public void enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProductEligibleCod(Map<String, Object> cartMap) {
        enhancedECommerceGoToCheckoutStep1(cartMap, EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD);
        flushEnhancedECommerceGoToCheckoutStep1();
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

    public void enhancedEcommerceProductViewWishListOnEmptyCart(Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_VIEW,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.VIEW_PRODUCT,
                Key.EVENT_LABEL, EventLabel.PRODUCT_WISHLIST,
                Key.E_COMMERCE, cartMap
        );
        sendEnhancedEcommerce(dataLayer);
    }

    public void enhancedEcommerceProductViewLastSeenOnEmptyCart(Map<String, Object> cartMap) {
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

    public void enhancedEcommerceClickProductRecommendationOnEmptyCart(String position, Map<String, Object> cartMap) {
        Map<String, Object> dataLayer = DataLayer.mapOf(
                Key.EVENT, EventName.PRODUCT_CLICK,
                Key.EVENT_CATEGORY, EventCategory.CART,
                Key.EVENT_ACTION, EventAction.CLICK_PRODUCT_RECOMMENDATION,
                Key.EVENT_LABEL, position,
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


}
