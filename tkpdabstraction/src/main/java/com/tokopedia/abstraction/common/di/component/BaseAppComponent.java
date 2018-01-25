package com.tokopedia.abstraction.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.module.AppModule;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.common.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.common.di.qualifier.OkHttpClientBuilderNonBaseQualifier;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;

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

    @OkHttpClientBuilderNonBaseQualifier
    OkHttpClient.Builder getDefaultOkHttpClientBuilderDefault();

    OkHttpClient.Builder getDefaultOkHttpClientBuilder();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    @AuthKeyQualifier
    String provideAuthKey();

    UserSession userSession();

    AbstractionRouter provideAbstractionRouter();

    @FreshAccessTokenQualifier
    String provideAccessToken();
}
