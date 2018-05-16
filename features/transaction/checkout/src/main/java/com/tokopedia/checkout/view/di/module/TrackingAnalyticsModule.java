package com.tokopedia.checkout.view.di.module;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.checkout.view.utils.CheckoutAnalytics;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
@Module
public class TrackingAnalyticsModule {
    @Provides
    CheckoutAnalytics checkoutAnalytics(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalytics(abstractionRouter.getAnalyticTracker());
    }
}
