package com.tokopedia.sellerapp.daggerModules;

import android.content.Context;

import com.tokopedia.sellerapp.SellerMainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule{

    private SellerMainApplication app;

    public AppModule(SellerMainApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return app;
    }

}
