package com.tokopedia.digital.newcart.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common_digital.common.RechargeAnalytics;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.di.DigitalComponent;
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

    RechargeAnalytics rechargeAnalytics();

    HttpLoggingInterceptor httpLoggingInterceptor();

    @ApplicationContext
    Context context();

    void inject(DigitalCartActivity digitalCartActivity);

    void inject(DigitalCartDefaultFragment digitalCartDefaultFragment);

    void inject(DigitalDealCheckoutFragment digitalDealCheckoutFragment);

    void inject(@NotNull DigitalCartMyBillsFragment digitalCartMyBillsFragment);
}