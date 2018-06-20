package com.tokopedia.checkout.view.di.module;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartPage;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartShipmentPage;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
@Module
public class TrackingAnalyticsModule {

    @Provides
    CheckoutAnalyticsCartPage checkoutAnalyticsCartPage(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsCartPage(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CheckoutAnalyticsCartShipmentPage checkoutAnalyticsCartShipmentPage(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsCartShipmentPage(abstractionRouter.getAnalyticTracker());
    }
}
