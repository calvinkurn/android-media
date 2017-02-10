package com.tokopedia.sellerapp.activities;

import com.tokopedia.sellerapp.daggerModules.NetworkModules;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by normansyahputa on 9/10/16.
 */
@Module
public class TestNetworkModules extends NetworkModules {

    @Provides
    @Singleton
    public NetworkBehavior provideBehavior(){
        return NetworkBehavior.create();
    }

    @Provides
    @Singleton
    public MockRetrofit provideMockRetrofit(@Named(V4_OKHTTP) Retrofit retrofit, NetworkBehavior behavior){
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
        return mockRetrofit;
    }
}
