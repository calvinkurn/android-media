package com.tokopedia.digital.cart.di;

import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.digital.cart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.cart.presentation.activity.CartDigitalActivity;
import com.tokopedia.digital.cart.presentation.fragment.CartDigitalFragment;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.util.DigitalAnalytics;
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

    void inject(CartDigitalFragment cartDigitalFragment);

    void inject(DigitalCartActivity digitalCartActivity);

    void inject(DigitalCartDefaultFragment digitalCartDefaultFragment);

    void inject(DigitalDealCheckoutFragment digitalDealCheckoutFragment);

    void inject(CartDigitalActivity cartDigitalActivity);

    DigitalPostPaidLocalCache digitalPostPaidLocalCache();

    void inject(@NotNull DigitalCartMyBillsFragment digitalCartMyBillsFragment);
}