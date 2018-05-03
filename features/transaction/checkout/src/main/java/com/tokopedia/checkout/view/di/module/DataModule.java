package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.checkout.data.apiservice.CartApi;
import com.tokopedia.checkout.data.apiservice.CartApiInterceptor;
import com.tokopedia.checkout.data.apiservice.CartResponseConverter;
import com.tokopedia.checkout.data.repository.CartRepository;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.data.repository.ITopPayRepository;
import com.tokopedia.checkout.data.repository.TopPayRepository;
import com.tokopedia.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.checkout.view.di.qualifier.CartChuckApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartFingerPrintApiInterceptorQualifier;
import com.tokopedia.checkout.view.di.qualifier.CartQualifier;
import com.tokopedia.core.network.apiservices.transaction.TXActService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 29/01/18.
 */
@Module
public class DataModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 0;

    @Provides
    @CartQualifier
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(
                NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY
        );
    }

    @Provides
    @CartChuckApiInterceptorQualifier
    ChuckInterceptor provideChuckInterceptor(ICartCheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.getCartCheckoutChuckInterceptor();
    }

    @Provides
    @CartFingerPrintApiInterceptorQualifier
    FingerprintInterceptor fingerprintInterceptor(ICartCheckoutModuleRouter cartCheckoutModuleRouter) {
        return cartCheckoutModuleRouter.getCartCheckoutFingerPrintInterceptor();
    }

    @Provides
    @CartQualifier
    CartApiInterceptor getCartApiInterceptor(@ApplicationContext Context context,
                                             UserSession userSession,
                                             AbstractionRouter abstractionRouter) {
        return new CartApiInterceptor(context, abstractionRouter, userSession, TkpdBaseURL.Cart.HMAC_KEY);
    }

    @Provides
    @CartQualifier
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            @CartQualifier CartApiInterceptor cartApiInterceptor,
                                            @CartQualifier OkHttpRetryPolicy okHttpRetryPolicy,
                                            @CartFingerPrintApiInterceptorQualifier FingerprintInterceptor fingerprintInterceptor,
                                            @CartChuckApiInterceptorQualifier ChuckInterceptor chuckInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cartApiInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    @CartQualifier
    public Retrofit provideCartRetrofit(@CartQualifier OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.BASE_API_DOMAIN)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @CartQualifier
    public CartApi provideCartApi(@CartQualifier Retrofit retrofit) {
        return retrofit.create(CartApi.class);
    }

    @Provides
    ICartRepository provideICartRepository(@CartQualifier CartApi cartApi) {
        return new CartRepository(cartApi);
    }

    @Provides
    @CartQualifier
    TXActService provideTXActService() {
        return new TXActService();
    }

    @Provides
    ITopPayRepository provideITopPayRepository(@CartQualifier TXActService txActService) {
        return new TopPayRepository(txActService);
    }
}
