package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.Key;


/**
 * @author anggaprasetiyo on 18/05/18.
 */
public class CheckoutAnalyticsCart extends TransactionAnalytics {

    @Inject
    public CheckoutAnalyticsCart(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

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

    public void eventViewAtcCartImpressionCartEmpty() {
        sendEventCategoryAction(
                EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.IMPRESSION_CART_EMPTY
        );
    }


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
}
