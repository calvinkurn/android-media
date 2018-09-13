package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.view.feature.shipment.converter.RatesDataConverter;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.transactiondata.repository.ICartRepository;

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

    @Provides
    ChangeShippingAddressUseCase getetShippingAddressUseCase(ICartRepository iCartRepository) {
        return new ChangeShippingAddressUseCase(iCartRepository);
    }

}
