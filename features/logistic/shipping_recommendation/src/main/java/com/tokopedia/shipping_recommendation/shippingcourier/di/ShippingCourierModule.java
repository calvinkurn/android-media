package com.tokopedia.shipping_recommendation.shippingcourier.di;

import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierAdapter;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierContract;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierPresenter;

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
