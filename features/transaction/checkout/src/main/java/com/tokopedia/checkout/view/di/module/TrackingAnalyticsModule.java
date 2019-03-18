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
    CheckoutAnalyticsCart checkoutAnalyticsCartPage() {
        return new CheckoutAnalyticsCart();
    }

    @Provides
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress() {
        return new CheckoutAnalyticsChangeAddress();
    }

    @Provides
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress() {
        return new CheckoutAnalyticsMultipleAddress();
    }

    @Provides
    CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart() {
        return new CheckoutAnalyticsAddToCart();
    }

    @Provides
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection() {
        return new CheckoutAnalyticsCourierSelection();
    }

    @Provides
    OrderAnalyticsOrderTracking orderAnalyticsOrderTracking() {
        return new OrderAnalyticsOrderTracking();
    }

    @Provides
    CheckoutAnalyticsPurchaseProtection providePurchaseProtectionAnalytics() {
        return new CheckoutAnalyticsPurchaseProtection();
    }

    @Provides
    CodAnalytics provideCodAnalytics() {
        return new CodAnalytics();
    }

    @Provides
    CornerAnalytics provideCornerAnalytics() {
        return new CornerAnalytics();
    }

}
