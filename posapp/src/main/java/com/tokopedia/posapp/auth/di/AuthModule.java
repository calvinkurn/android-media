package com.tokopedia.posapp.auth.di;

import com.tokopedia.core.network.di.qualifier.AccountsQualifier;
import com.tokopedia.posapp.auth.AccountApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author okasurya on 2/23/18.
 */

@Module
public class AuthModule {
    @AuthScope
    @Provides
    AccountApi provideAccountApi(@AccountsQualifier Retrofit retrofit) {
        return retrofit.create(AccountApi.class);
    }
}
