package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.repository.RatesRepository;
import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 08/05/18.
 */
@Module
public class ShipmentUseCaseModule {

    @Provides
    GetRatesUseCase getRatesUseCase(RatesRepository ratesRepository) {
        return new GetRatesUseCase(ratesRepository);
    }
}
