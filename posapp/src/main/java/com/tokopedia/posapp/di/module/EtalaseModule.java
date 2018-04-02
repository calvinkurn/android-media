package com.tokopedia.posapp.di.module;

import com.tokopedia.posapp.di.scope.EtalaseScope;
import com.tokopedia.posapp.etalase.data.factory.EtalaseFactory;
import com.tokopedia.posapp.etalase.data.repository.EtalaseRepository;
import com.tokopedia.posapp.etalase.data.repository.EtalaseRepositoryImpl;

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
}
