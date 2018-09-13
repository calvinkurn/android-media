package com.tokopedia.affiliate.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.affiliate.common.network.AffiliateApi;
import com.tokopedia.affiliate.common.network.AffiliateUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by yfsx on 13/09/18.
 */
@Module
public class AffiliateModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @AffiliateScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                    httpLoggingInterceptor,
                                            OkHttpRetryPolicy retryPolicy,
//                                            ChuckInterceptor chuckInterceptor,
                                            @ApplicationContext Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(new TkpdAuthInterceptor(context, (NetworkRouter)context, new UserSession(context)));

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
//            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @AffiliateScope
    @Provides
    public Retrofit provideAffiliateRetrofit(OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(AffiliateUrl.BASE_URL).client(okHttpClient).build();
    }

    @AffiliateScope
    @Provides
    public AffiliateApi provideAffiliateApi(Retrofit retrofit) {
        return retrofit.create(AffiliateApi.class);
    }

//    @AffiliateScope
//    @Provides
//    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
//        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
//    }

    @AffiliateScope
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }
}
