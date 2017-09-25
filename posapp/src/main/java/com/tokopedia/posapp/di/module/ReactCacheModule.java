package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.factory.BankFactory;
import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.data.factory.EtalaseFactory;
import com.tokopedia.posapp.data.source.local.BankLocalSource;
import com.tokopedia.posapp.di.scope.ReactCacheScope;
import com.tokopedia.posapp.react.datasource.ReactCacheRepositoryImpl;
import com.tokopedia.posapp.react.datasource.cache.ReactBankCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactCartCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactEtalaseCacheSource;
import com.tokopedia.posapp.react.datasource.cache.ReactProductCacheSource;
import com.tokopedia.posapp.react.datasource.ReactCacheRepository;
import com.tokopedia.posapp.react.factory.ReactCacheFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/16/17.
 */

@ReactCacheScope
@Module(includes = {PosCacheModule.class, CartModule.class})
public class ReactCacheModule {

    @Provides
    ReactCartCacheSource provideReactCartCacheSource(CartFactory cartFactory, Gson gson) {
        return new ReactCartCacheSource(cartFactory, gson);
    }

    @Provides
    ReactProductCacheSource provideReactProductCacheSource() {
        return new ReactProductCacheSource();
    }

    @Provides
    ReactBankCacheSource provideBankCacheSource(BankFactory bankFactory, Gson gson) {
        return new ReactBankCacheSource(bankFactory, gson);
    }

    @Provides
    ReactEtalaseCacheSource provideEtalaseCacheSource(EtalaseFactory etalaseFactory, Gson gson) {
        return new ReactEtalaseCacheSource(etalaseFactory, gson);
    }

    @Provides
    ReactCacheFactory provideReactCacheFactory(ReactCartCacheSource reactCartCacheSource,
                                               ReactProductCacheSource reactProductCacheSource,
                                               ReactBankCacheSource reactBankCacheSource,
                                               ReactEtalaseCacheSource reactEtalaseCacheSource) {
        return new ReactCacheFactory(
                reactCartCacheSource, reactProductCacheSource, reactBankCacheSource, reactEtalaseCacheSource
        );
    }

    @Provides
    ReactCacheRepository provideReactCacheRepository(ReactCacheFactory reactCacheFactory) {
        return new ReactCacheRepositoryImpl(reactCacheFactory);
    }
}
