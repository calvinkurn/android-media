package com.tokopedia.common_digital.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.cart.view.activity.InstantCheckoutActivity;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Rizky on 13/08/18.
 */
@DigitalScope
@Component(modules = DigitalModule.class, dependencies = BaseAppComponent.class)
public interface DigitalComponent {

    @ApplicationContext
    Context context();

    @DigitalRestApiRetrofit
    Retrofit digitalRestApiRetrofit();

    DigitalRestApi digitalApi();

    AbstractionRouter abstractionRouter();

    DigitalAddToCartUseCase digitalAddToCartUseCase();

    DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase();

    DigitalRouter digitalRouter();

    CacheManager globalCacheManager();

    void inject(InstantCheckoutActivity instantCheckoutActivity);

    HttpLoggingInterceptor httpLoggingInterceptor();

}

