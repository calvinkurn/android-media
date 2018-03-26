package com.tokopedia.posapp.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.cache.data.factory.EtalaseFactory;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepository;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepositoryImpl;
import com.tokopedia.posapp.di.scope.EtalaseScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/19/17.
 */

@EtalaseScope
@Module
public class EtalaseModule {
    public static final String POS = "POS_CACHE";

    @Provides
    EtalaseRepository provideEtalaseRepository(EtalaseFactory etalaseFactory) {
        return new EtalaseRepositoryImpl(etalaseFactory);
    }

    @Provides
    @Named(POS)
    LocalCacheHandler providePosLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, POS);
    }
}
