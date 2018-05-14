package com.tokopedia.checkout.view.view.shipment.shippingoptions.di;

import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.CourierAdapter;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.CourierContract;
import com.tokopedia.checkout.view.view.shipment.shippingoptions.CourierPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

@Module
public class CourierModule {

    @Provides
    @CourierScope
    CourierContract.Presenter providePresenter(GetRatesUseCase getRatesUseCase) {
        return new CourierPresenter(getRatesUseCase);
    }

    @Provides
    @CourierScope
    CourierAdapter provideCourierAdapter() {
        return new CourierAdapter();
    }
}
