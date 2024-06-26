package com.tokopedia.loyalty.di.module;

import com.google.gson.Gson;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.qualifier.LoyaltyModuleQualifier;
import com.tokopedia.loyalty.domain.repository.ITokoPointDBService;
import com.tokopedia.loyalty.domain.repository.TokoPointDBService;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

@Module
public class CacheDBModule {

    @Provides
    @LoyaltyScope
    @LoyaltyModuleQualifier
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @LoyaltyScope
    ITokoPointDBService provideITokoPointDBService(@LoyaltyModuleQualifier Gson gson) {
        return new TokoPointDBService(gson);
    }
}
