package com.tokopedia.abstraction.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.di.module.AppModule;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.di.scope.ApplicationScope;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author kulomady on 1/9/17.
 */
@ApplicationScope
@Component(modules = {
        AppModule.class
})
public interface BaseAppComponent {

    @ApplicationContext
    Context getContext();

    OkHttpClient.Builder getDefaultOkHttpClientBuilder();

    Retrofit.Builder retrofitBuilder();

    Gson gson();
}
