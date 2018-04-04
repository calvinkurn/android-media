package com.tokopedia.posapp.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.PosSessionHandler;
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
    PosSessionHandler providePosSessionHandler(@ApplicationContext Context context) {
        return new PosSessionHandler(context);
    }

    @Provides
    ReactCacheRepository provideReactCacheRepository(ReactDataFactory reactDataFactory) {
        return new ReactCacheRepositoryImpl(reactDataFactory);
    }
}
