package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.di;

import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierAdapter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierContract;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

@Module
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
