package com.tokopedia.checkout.view.utils;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
public class CheckoutAnalytics {

    public interface EventName {
        String CLICK_ATC = "clickATC";

    }

    public interface EventCategory {
        String CART = "Cart";
    }

    public interface EventAction {
        String CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode promo atau kupon";
    }

    public interface EventLabel {

    }

    private final AnalyticTracker analyticTracker;

    @Inject
    public CheckoutAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickCartGunakanKodePromoAatauKupon() {
        analyticTracker.sendEventTracking(EventName.CLICK_ATC,
                EventCategory.CART,
                EventAction.CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ""
        );
    }
}
