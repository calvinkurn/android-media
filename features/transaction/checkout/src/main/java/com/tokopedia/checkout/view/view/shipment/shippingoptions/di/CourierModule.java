package com.tokopedia.checkout.view.view.shipment.shippingoptions.di;

import com.tokopedia.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.data.repository.RatesRepository;
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
    ShipmentRatesDataMapper provideShipmentRatesDatamapper() {
        return new ShipmentRatesDataMapper();
    }

    @Provides
    @CourierScope
    GetRatesUseCase provideGetRatesUseCase(RatesRepository ratesRepository) {
        return new GetRatesUseCase(ratesRepository);
    }

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
