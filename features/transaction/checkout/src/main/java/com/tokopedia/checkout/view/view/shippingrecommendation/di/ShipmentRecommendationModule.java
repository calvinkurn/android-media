package com.tokopedia.checkout.view.view.shippingrecommendation.di;

import com.tokopedia.checkout.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.view.shippingrecommendation.ShipmentRecommendationContract;
import com.tokopedia.checkout.view.view.shippingrecommendation.ShipmentRecommendationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 07/08/18.
 */

@Module(includes = TrackingAnalyticsModule.class)
public class ShipmentRecommendationModule {

    @Provides
    @ShipmentRecommendationScope
    ShipmentRecommendationContract.Presenter providePresenter(GetCourierRecommendationUseCase getCourierRecommendationUseCase) {
        return new ShipmentRecommendationPresenter(getCourierRecommendationUseCase);
    }

}
