package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.data.mapper.AddToCartMapper;
import com.tokopedia.posapp.data.repository.CartRepository;
import com.tokopedia.posapp.data.repository.CartRepositoryImpl;
import com.tokopedia.posapp.domain.usecase.AddToCartUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 8/22/17.
 */

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
        return new CartRepositoryImpl(cartFactory);
    }

    @Provides
    AddToCartUseCase provideAddToCartUseCase(ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread,
                                             CartRepository cartRepository) {
        return new AddToCartUseCase(threadExecutor, postExecutionThread, cartRepository);
    }
}
