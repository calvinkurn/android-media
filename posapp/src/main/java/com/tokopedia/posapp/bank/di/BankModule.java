package com.tokopedia.posapp.bank.di;

import com.tokopedia.posapp.bank.data.source.cloud.api.BankApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/5/17.
 */

@BankScope
@Module
public class BankModule {
    @Provides
    BankApi provideBankApi(Retrofit retrofit) {
        return retrofit.create(BankApi.class);
    }
}
