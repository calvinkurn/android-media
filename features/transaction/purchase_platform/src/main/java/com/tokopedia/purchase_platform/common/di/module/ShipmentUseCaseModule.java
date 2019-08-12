package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.purchase_platform.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.transactiondata.repository.ICartRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 08/05/18.
 */
@Module
public class ShipmentUseCaseModule {

    @Provides
    ChangeShippingAddressUseCase getetShippingAddressUseCase(ICartRepository iCartRepository) {
        return new ChangeShippingAddressUseCase(iCartRepository);
    }

}
