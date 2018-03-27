package com.tokopedia.posapp.di.module;

import com.tokopedia.posapp.cart.di.CartModule;
import com.tokopedia.posapp.di.scope.ReactCacheScope;
import com.tokopedia.posapp.react.datasource.ReactCacheRepository;
import com.tokopedia.posapp.react.datasource.ReactCacheRepositoryImpl;
import com.tokopedia.posapp.react.factory.ReactDataFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/16/17.
 */

@ReactCacheScope
@Module(includes = {PosCacheModule.class, CartModule.class})
public class ReactCacheModule {

    @Provides
    ReactCacheRepository provideReactCacheRepository(ReactDataFactory reactDataFactory) {
        return new ReactCacheRepositoryImpl(reactDataFactory);
    }
}
