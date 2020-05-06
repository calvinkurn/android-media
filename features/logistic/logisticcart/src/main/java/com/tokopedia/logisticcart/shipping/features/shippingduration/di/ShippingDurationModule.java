package com.tokopedia.logisticcart.shipping.features.shippingduration.di;

import com.tokopedia.logisticcart.domain.executor.MainScheduler;
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationContract;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationPresenter;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase;
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

@Module
public class ShippingDurationModule {

    @Provides
    @ShippingDurationScope
    ShippingDurationAdapter provideShippingDurationAdapter() {
        return new ShippingDurationAdapter();
    }

    @Provides
    @ShippingDurationScope
    ShippingDurationConverter provideShippingDurationConverter() {
        return new ShippingDurationConverter();
    }

    @Provides
    @ShippingDurationScope
    ShippingCourierConverter provideShippingCourierConverter() {
        return new ShippingCourierConverter();
    }

    @Provides
    @ShippingDurationScope
    ShippingDurationContract.Presenter provideShippingDurationPresenter(
            GetRatesUseCase ratesUseCase,
            GetRatesApiUseCase ratesApiUseCase,
            RatesResponseStateConverter stateConverter,
            ShippingCourierConverter shippingCourierConverter) {
        return new ShippingDurationPresenter(ratesUseCase, ratesApiUseCase, stateConverter,
                shippingCourierConverter);
    }

    @Provides
    @ShippingDurationScope
    CheckoutAnalyticsCourierSelection getAnalytics() {
        return new CheckoutAnalyticsCourierSelection();
    }

    @Provides
    @ShippingDurationScope
    SchedulerProvider provideScheduler() {
        return new MainScheduler();
    }

}
