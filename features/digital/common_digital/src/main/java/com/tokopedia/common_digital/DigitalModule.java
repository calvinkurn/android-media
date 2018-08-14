package com.tokopedia.common_digital;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Rizky on 13/08/18.
 */
@Module
public class DigitalModule {

    @Provides
    DigitalInterceptor provideDigitalInterceptor(@ApplicationContext Context context,
                                                 NetworkRouter networkRouter, UserSession userSession) {
        return new DigitalInterceptor(context, networkRouter, userSession);
    }

    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            DigitalRouter digitalRouter, DigitalInterceptor digitalInterceptor) {
        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
        builder.addInterceptor(digitalInterceptor);
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse.class));
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(digitalRouter.getChuckInterceptor());
        }

        return builder.build();
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(DigitalUrl.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    public DigitalApi provideTrainApi(Retrofit retrofit) {
        return retrofit.create(DigitalApi.class);
    }

}
