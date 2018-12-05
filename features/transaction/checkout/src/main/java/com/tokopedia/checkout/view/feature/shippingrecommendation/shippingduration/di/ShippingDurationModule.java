package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.di;

import com.tokopedia.checkout.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationAdapter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationContract;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationConverter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationPresenter;

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
                                                                        ShippingDurationConverter shippingDurationConverter,
                                                                        ShippingCourierConverter shippingCourierConverter) {
        return new ShippingDurationPresenter(getCourierRecommendationUseCase, shippingDurationConverter, shippingCourierConverter);
    }

    @Provides
    @ShippingDurationScope
    GetCourierRecommendationUseCase getCourierRecommendationUseCase(ShippingDurationConverter shippingDurationConverter) {
        return new GetCourierRecommendationUseCase(shippingDurationConverter);
    }

}
