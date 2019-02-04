package com.tokopedia.digital.newcart.di;

import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.di.DigitalComponent;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.presentation.activity.DigitalCartActivity;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDefaultFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartMyBillsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalDealCheckoutFragment;

import org.jetbrains.annotations.NotNull;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Rizky on 28/08/18.
 */
@DigitalCartScope
@Component(dependencies = DigitalComponent.class, modules = DigitalCartModule.class)
public interface DigitalCartComponent {
    DigitalAnalytics digitalAnalytics();

    HttpLoggingInterceptor httpLoggingInterceptor();

    DigitalModuleRouter digitalModuleRouter();

    void inject(DigitalCartActivity digitalCartActivity);

    void inject(DigitalCartDefaultFragment digitalCartDefaultFragment);

    void inject(DigitalDealCheckoutFragment digitalDealCheckoutFragment);

    DigitalPostPaidLocalCache digitalPostPaidLocalCache();

    void inject(@NotNull DigitalCartMyBillsFragment digitalCartMyBillsFragment);
}