package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.cart.data.factory.CartFactory;
import com.tokopedia.posapp.cart.data.mapper.AddToCartMapper;
import com.tokopedia.posapp.cart.data.repository.CartRepository;
import com.tokopedia.posapp.cart.data.repository.CartLocalRepository;
import com.tokopedia.posapp.di.scope.CartScope;
import com.tokopedia.posapp.cart.domain.usecase.AddToCartUseCase;
import com.tokopedia.posapp.cart.domain.usecase.GetAllCartUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 8/22/17.
 */

@CartScope
@Module
public class CartModule {
    @Provides
    AddToCartMapper provideAddToCartMapper() {
        return new AddToCartMapper();
    }

    @Provides
    CartFactory provideCartFactory() {
        return new CartFactory();
    }

    @Provides
    CartRepository provideCartRepository(CartFactory cartFactory) {
        return new CartLocalRepository(cartFactory);
    }

    @Provides
    AddToCartUseCase provideAddToCartUseCase(ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread,
                                             CartRepository cartRepository) {
        return new AddToCartUseCase(threadExecutor, postExecutionThread, cartRepository);
    }

    @Provides
    GetAllCartUseCase provideGetAllCartUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               CartRepository cartRepository) {
        return new GetAllCartUseCase(threadExecutor, postExecutionThread, cartRepository);
    }
}
