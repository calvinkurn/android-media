package com.tokopedia.transactionanalytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventAction;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventCategory;
import static com.tokopedia.transactionanalytics.ConstantTransactionAnalytics.EventName;


/**
 * @author anggaprasetiyo on 18/05/18.
 */
public class CheckoutAnalyticsCartPage extends CheckoutAnalytics {

    @Inject
    public CheckoutAnalyticsCartPage(AnalyticTracker analyticTracker) {
        super(analyticTracker);
    }

    public void eventClickCartClickKuponFromGunakanPromoAtauKupon() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCartClickKodePromoFromGunakanPromoAtauKupon() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCartClickKuponSayaFromGunakanPromoAtauKupon() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCartClickGunakanKodeFormGunakanKodePromoAtauKupon() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCartClickPilihSemuaFormHapus() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PILIH_SEMUA_FORM_HAPUS,
                ""
        );
    }

    public void eventClickCartClickChecklistBoxFormHapus() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_CHECKLIST_BOX_FORM_HAPUS,
                ""
        );
    }

    public void eventClickCartClickHapusFormHapus() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_FORM_HAPUS,
                ""
        );
    }

    public void eventClickCartClickGunakanKodePromoAatauKupon() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }

    public void eventClickCartClickHapusOnTopRightCorner() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_HAPUS_ON_TOP_RIGHT_CORNER,
                ""
        );
    }

    public void eventClickCartClickShopName(String shopName) {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_SHOP_NAME,
                shopName
        );
    }

    public void eventClickCartClickProductName(String productName) {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_PRODUCT_NAME,
                productName
        );
    }

    public void eventClickCartClickButtonPlus() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BUTTON_PLUS,
                ""
        );
    }

    public void eventClickCartClickButtonMinus() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BUTTON_MIN,
                ""
        );
    }

    public void eventClickCartClickTrashBin() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_TRASH_BIN,
                ""
        );
    }

    public void eventClickCartClickArrowBack() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_ARROW_BACK,
                ""
        );
    }

    public void eventClickCartClickXOnBannerPromoCode() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_X_ON_BANNER_PROMO_CODE,
                ""
        );
    }

    public void eventClickCartClickBelanjaSekarangOnEmptyCart() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_BELANJA_SEKARANG_ON_EMPTY_CART,
                ""
        );
    }

    public void eventViewCartViewImpressionCartEmpty() {
        analyticTracker.sendEventTracking(EventName.VIEW_ATC,
                EventCategory.CART,
                EventAction.VIEW_IMPRESSION_CART_EMPTY,
                "");
    }

    public void enhancedECommerceRemoveCartAddWishList(Map<String, Object> cartMap) {
        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", EventName.REMOVE_FORM_CART,
                        "eventCategory", "",
                        "eventAction", EventCategory.CART,
                        "eventLabel", ConstantTransactionAnalytics.EventLabel.CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_TRASH_BIN,
                        "ecommerce", cartMap)
        );
    }

    public void enhancedECommerceRemoveCartNotWishList(Map<String, Object> cartMap) {
        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", EventName.REMOVE_FORM_CART,
                        "eventCategory", "",
                        "eventAction", EventCategory.CART,
                        "eventLabel", ConstantTransactionAnalytics.EventLabel.CLICK_HAPUS_FROM_TRASH_BIN,
                        "ecommerce", cartMap)
        );
    }

    public void enhancedECommerceCartHapusProdukBerkendala(Map<String, Object> cartMap) {
        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", EventName.REMOVE_FORM_CART,
                        "eventCategory", "",
                        "eventAction", EventCategory.CART,
                        "eventLabel", ConstantTransactionAnalytics.EventLabel.CLICK_HAPUS_PRODUK_BERKENDALA,
                        "ecommerce", cartMap)
        );
    }
}
