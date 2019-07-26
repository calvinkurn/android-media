package com.tokopedia.logisticcart.shippingduration.di;

import com.tokopedia.logisticcart.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.logisticcart.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shippingduration.view.ShippingDurationAdapter;
import com.tokopedia.logisticcart.shippingduration.view.ShippingDurationContract;
import com.tokopedia.logisticcart.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.logisticcart.shippingduration.view.ShippingDurationPresenter;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;

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
    ShippingDurationContract.Presenter provideShippingDurationPresenter(GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                                                        ShippingCourierConverter shippingCourierConverter) {
        return new ShippingDurationPresenter(getCourierRecommendationUseCase, shippingCourierConverter);
    }

    @Provides
    @ShippingDurationScope
    GetCourierRecommendationUseCase getCourierRecommendationUseCase(ShippingDurationConverter shippingDurationConverter) {
        return new GetCourierRecommendationUseCase(shippingDurationConverter);
    }

    @Provides
    @ShippingDurationScope
    CheckoutAnalyticsCourierSelection getAnalytics() {
        return new CheckoutAnalyticsCourierSelection();
    }

}
