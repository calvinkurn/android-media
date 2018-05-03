package com.tokopedia.tkpd.tkpdcontactus.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.tkpd.tkpdcontactus.common.data.model.ContactUsErrorResponse;
import com.tokopedia.tkpd.tkpdcontactus.common.di.network.ContactUsAuthInterceptor;
import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;
import com.tokopedia.tkpd.tkpdcontactus.common.api.ContactUsURL;

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
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ContactUsURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(ContactUsAuthInterceptor contactUsAuthInterceptor,HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(contactUsAuthInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(ContactUsErrorResponse.class));

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor);

        }
        return builder.build();
    }

    @Provides
    ContactUsAuthInterceptor provideContactUsAuthInterceptor(@ApplicationContext Context context,
                                                             AbstractionRouter abstractionRouter,
                                                             UserSession userSession) {
        return new ContactUsAuthInterceptor(context,abstractionRouter,userSession);

    }
}
