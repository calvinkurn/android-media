package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.apiservice.CartService;
import com.tokopedia.checkout.data.repository.CartRepository;
import com.tokopedia.checkout.data.repository.ICartRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 29/01/18.
 */
@Module
public class DataModule {

    @Provides
    CartService provideCartService() {
        return new CartService();
    }

    @Provides
    ICartRepository provideICartRepository(CartService cartService) {
        return new CartRepository(cartService);
    }

}
