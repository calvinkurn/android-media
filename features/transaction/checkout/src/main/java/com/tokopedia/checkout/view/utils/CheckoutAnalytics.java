package com.tokopedia.checkout.view.utils;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
public class CheckoutAnalytics {

    public interface EventName {
        String CLICK_ATC = "clickATC";
        String VIEW_ATC = "viewATC";
    }

    public interface EventCategory {
        String CART = "Cart";
    }

    public interface EventAction {
        String CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode promo atau kupon";
        String CLICK_HAPUS_ON_TOP_RIGHT_CORNER = "click hapus on top right corner";
        String CLICK_SHOP_NAME = "click shop name";
        String CLICK_PRODUCT_NAME = "click product name";
        String CLICK_BUTTON_PLUS = "click button +";
        String CLICK_BUTTON_MIN = "click button -";
        String CLICK_TRASH_BIN = "click trash bin";
        String CLICK_ARROW_BACK = "click arrow back";
        String CLICK_X_ON_BANNER_PROMO_CODE = "click x on banner promo code";
        String CLICK_BELANJA_SEKARANG_ON_EMPTY_CART = "click belanja sekarang on empty cart";

        String VIEW_IMPRESSION_CART_EMPTY = "impression cart empty";
    }

    public interface EventLabel {

    }

    private final AnalyticTracker analyticTracker;

    @Inject
    public CheckoutAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
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


}
