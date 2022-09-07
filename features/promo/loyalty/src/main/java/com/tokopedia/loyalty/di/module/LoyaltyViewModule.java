package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 30/11/17.
 */

@Module
public class LoyaltyViewModule {


    @Provides
    LoyaltyModuleRouter provideLoyaltyViewModule(@ApplicationContext Context context) {
        if (context instanceof LoyaltyModuleRouter) {
            return (LoyaltyModuleRouter) context;
        }
        throw new RuntimeException("Applicaton should implement LoyaltyModuleRouter");
    }

    @Provides
    CheckoutAnalyticsCart provideCheckoutAnalyticsCartPage(@ApplicationContext Context context) {
        return new CheckoutAnalyticsCart(context);
    }

    @Provides
    CheckoutAnalyticsCourierSelection provideCheckoutAnalyticsCourierSelection() {
        return new CheckoutAnalyticsCourierSelection();
    }

}
