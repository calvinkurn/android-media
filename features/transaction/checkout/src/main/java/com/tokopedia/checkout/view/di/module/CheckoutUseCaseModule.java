package com.tokopedia.checkout.view.di.module;

import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 02/05/18.
 */
@Module
public class CheckoutUseCaseModule {

    @Provides
    CheckoutUseCase provideCheckoutUseCase(ICartRepository cartRepository, ICheckoutMapper checkoutMapper) {
        return new CheckoutUseCase(cartRepository, checkoutMapper);
    }

    @Provides
    GetThanksToppayUseCase provideGetThanksToppayUseCase(ITopPayRepository topPayRepository, ITopPayMapper topPayMapper) {
        return new GetThanksToppayUseCase(topPayRepository, topPayMapper);
    }
}
