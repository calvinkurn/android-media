package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.di;

import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierAdapter;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierContract;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierPresenter;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.di.ShippingDurationScope;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view.ShippingDurationAdapter;
import com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view.ShippingDurationConverter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

@Module(includes = TrackingAnalyticsModule.class)
public class ShippingCourierModule {

    @Provides
    @ShippingCourierScope
    ShippingCourierAdapter provideShippingCourierAdapter() {
        return new ShippingCourierAdapter();
    }

    @Provides
    @ShippingCourierScope
    ShippingCourierConverter provideShippingCourierConverter() {
        return new ShippingCourierConverter();
    }

    @Provides
    @ShippingCourierScope
    ShippingCourierContract.Presenter provideShippingCourierPresenter(ShippingCourierConverter shippingCourierConverter) {
        return new ShippingCourierPresenter(shippingCourierConverter);
    }

}
