package com.tokopedia.contactus.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.common.api.ContactUsURL;
import com.tokopedia.contactus.common.data.model.ContactUsErrorResponse;
import com.tokopedia.contactus.common.di.network.ContactUsAuthInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@Module
public class ContactUsModule {

    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter)context;
    }

    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }


    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ContactUsURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context,ContactUsAuthInterceptor contactUsAuthInterceptor, HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(contactUsAuthInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(ContactUsErrorResponse.class));
        return builder.build();
    }

    @Provides
    ContactUsAuthInterceptor provideContactUsAuthInterceptor(@ApplicationContext Context context,
                                                             NetworkRouter networkRouter,
                                                             UserSessionInterface userSessionInterface) {
        return new ContactUsAuthInterceptor(context,networkRouter, userSessionInterface);

    }
}
