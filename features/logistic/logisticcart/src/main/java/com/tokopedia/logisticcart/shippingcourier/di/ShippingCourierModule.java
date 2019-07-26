package com.tokopedia.logisticcart.shippingcourier.di;

import com.tokopedia.logisticcart.shippingcourier.view.ShippingCourierAdapter;
import com.tokopedia.logisticcart.shippingcourier.view.ShippingCourierContract;
import com.tokopedia.logisticcart.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.logisticcart.shippingcourier.view.ShippingCourierPresenter;

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
