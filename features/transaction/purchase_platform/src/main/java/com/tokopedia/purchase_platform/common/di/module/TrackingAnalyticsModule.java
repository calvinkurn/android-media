package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.logisticdata.data.analytics.CodAnalytics;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.purchase_platform.checkout.analytics.CheckoutAnalyticsPurchaseProtection;
import com.tokopedia.purchase_platform.checkout.analytics.CornerAnalytics;

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
    CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection() {
        return new CheckoutAnalyticsCourierSelection();
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
