package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.data.repository.RatesDataStore;
import com.tokopedia.checkout.data.repository.RatesRepository;
import com.tokopedia.checkout.data.service.RatesService;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.checkout.view.di.scope.ShipmentDetailScope;
import com.tokopedia.checkout.view.view.shippingoptions.IShipmentDetailPresenter;
import com.tokopedia.checkout.view.view.shippingoptions.ShipmentDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */
@Module
public class ShipmentDetailModule {

    private static final int RETRY_COUNT = 0;

    @Provides
    @ShipmentDetailScope
    RatesService provideRatesService() {
        return new RatesService();
    }

    @Provides
    @ShipmentDetailScope
    RatesDataStore provideRatesDataStore(RatesService service) {
        return new RatesDataStore(service);
    }

    @Provides
    @ShipmentDetailScope
    ShipmentRatesDataMapper provideShipmentRatesDatamapper() {
        return new ShipmentRatesDataMapper();
    }

    @Provides
    @ShipmentDetailScope
    RatesRepository provideRatesRepository(RatesDataStore ratesDataStore,
                                           ShipmentRatesDataMapper shipmentRatesDataMapper) {
        return new RatesRepository(ratesDataStore, shipmentRatesDataMapper);
    }

    @Provides
    @ShipmentDetailScope
    GetRatesUseCase provideGetRatesUseCase(RatesRepository ratesRepository) {
        return new GetRatesUseCase(ratesRepository);
    }

    @Provides
    @ShipmentDetailScope
    IShipmentDetailPresenter provideShipmentDetailPresenter(GetRatesUseCase getRatesUseCase) {
        return new ShipmentDetailPresenter(getRatesUseCase);
    }

    @Provides
    @ShipmentDetailScope
    CourierChoiceAdapter provideCourierChoiceAdapter() {
        return new CourierChoiceAdapter();
    }
}
