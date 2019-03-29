package com.tokopedia.checkout.view.di.module;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.logisticanalytics.CodAnalytics;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsAddToCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.transactionanalytics.CornerAnalytics;
import com.tokopedia.transactionanalytics.OrderAnalyticsOrderTracking;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 14/05/18.
 */
@Module
public class TrackingAnalyticsModule {

    @Provides
    CheckoutAnalyticsCart checkoutAnalyticsCartPage(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsCart(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsChangeAddress(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsMultipleAddress(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsAddToCart(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsCourierSelection(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    OrderAnalyticsOrderTracking orderAnalyticsOrderTracking(AbstractionRouter abstractionRouter) {
        return new OrderAnalyticsOrderTracking(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CheckoutAnalyticsPurchaseProtection providePurchaseProtectionAnalytics(AbstractionRouter abstractionRouter) {
        return new CheckoutAnalyticsPurchaseProtection(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CodAnalytics provideCodAnalytics(AbstractionRouter abstractionRouter) {
        return new CodAnalytics(abstractionRouter.getAnalyticTracker());
    }

    @Provides
    CornerAnalytics provideCornerAnalytics(AbstractionRouter abstractionRouter) {
        return new CornerAnalytics(abstractionRouter.getAnalyticTracker());
    }

}
