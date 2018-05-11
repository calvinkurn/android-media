package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.mapper.ShipmentRatesDataMapper;
import com.tokopedia.checkout.view.view.shipment.converter.RatesDataConverter;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 08/05/18.
 */
@Module
public class ShipmentUseCaseModule {

    @Provides
    GetRatesUseCase getRatesUseCase(RatesRepository ratesRepository,
                                    RatesDataConverter ratesDataConverter) {
        return new GetRatesUseCase(ratesRepository, ratesDataConverter);
    }
}
