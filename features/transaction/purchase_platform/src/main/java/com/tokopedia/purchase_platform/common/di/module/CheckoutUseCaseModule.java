package com.tokopedia.purchase_platform.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter;
import com.tokopedia.purchase_platform.common.data.repository.ICartRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 02/05/18.
 */
@Module
public class CheckoutUseCaseModule {

//    @Provides
//    CheckoutUseCase provideCheckoutUseCase(@ApplicationContext Context context, ICheckoutModuleRouter checkoutModuleRouter, ICartRepository cartRepository, ICheckoutMapper checkoutMapper) {
//        return new CheckoutUseCase(context, cartRepository, checkoutMapper, checkoutModuleRouter);
//    }

}
