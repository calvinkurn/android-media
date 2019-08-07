package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.purchase_platform.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.purchase_platform.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 02/05/18.
 */
@Module
public class CheckoutUseCaseModule {

    @Provides
    CheckoutUseCase provideCheckoutUseCase(ICheckoutModuleRouter checkoutModuleRouter, ICartRepository cartRepository, ICheckoutMapper checkoutMapper) {
        return new CheckoutUseCase(cartRepository, checkoutMapper, checkoutModuleRouter);
    }

}
