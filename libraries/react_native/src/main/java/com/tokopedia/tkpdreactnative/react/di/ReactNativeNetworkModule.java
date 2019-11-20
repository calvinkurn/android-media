package com.tokopedia.tkpdreactnative.react.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.tkpdreactnative.react.common.data.interceptor.DigitalHmacAuthInterceptor;
import com.tokopedia.tkpdreactnative.react.common.data.interceptor.DynamicTkpdAuthInterceptor;
import com.tokopedia.tkpdreactnative.react.common.data.service.CommonService;
import com.tokopedia.tkpdreactnative.react.data.ReactNetworkRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.data.StringResponseConverter;
import com.tokopedia.tkpdreactnative.react.data.UnifyReactNetworkRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkAuthDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkBearerDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkWsV4AuthDataSource;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkDefaultAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkFactory;
import com.tokopedia.tkpdreactnative.react.di.qualifier.ReactDefaultAuthQualifier;
import com.tokopedia.tkpdreactnative.react.di.qualifier.ReactDynamicAuthQualifier;
import com.tokopedia.tkpdreactnative.react.di.qualifier.ReactNoAuthQualifier;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alvarisi on 9/14/17.
 */
@Module
public class ReactNativeNetworkModule {

    @Provides
    @ReactNativeNetworkScope
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @ReactNativeNetworkScope
    DigitalHmacAuthInterceptor provideDigitalHmacAuthInterceptor(@ApplicationContext Context context,
                                                                 UserSessionInterface userSessionInterface){
        return new DigitalHmacAuthInterceptor(context,
                (NetworkRouter) context.getApplicationContext(),
                userSessionInterface,
                TkpdBaseURL.DigitalApi.HMAC_KEY);
    }

    @Provides
    @ReactNativeNetworkScope
    DynamicTkpdAuthInterceptor provideDynamicTkpdAuthInterceptor(@ApplicationContext Context context,
                                                                 UserSessionInterface userSessionInterface){
        return new DynamicTkpdAuthInterceptor(context,
                (NetworkRouter) context.getApplicationContext(),
                userSessionInterface);
    }

    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    OkHttpClient provideOkhttpNoAuth(@ApplicationContext Context context, UserSessionInterface userSessionInterface) {
        return new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSessionInterface))
                .addInterceptor(new TkpdBaseInterceptor())
                .build();
    }

    @ReactDefaultAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    OkHttpClient provideOkhttpDefaultAuth(@ApplicationContext Context context,
                                          UserSessionInterface userSessionInterface) {
        return new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(), userSessionInterface))
                .addInterceptor(new CacheApiInterceptor(context))
                .addInterceptor(new TkpdAuthInterceptor(context,
                        (NetworkRouter)context.getApplicationContext(),
                        userSessionInterface))
                .build();
    }

    @ReactDynamicAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    OkHttpClient provideOkhttpDynamicAuth(@ApplicationContext Context context,
                                          UserSessionInterface userSessionInterface,
                                          DynamicTkpdAuthInterceptor dynamicTkpdAuthInterceptor) {
        return new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context.getApplicationContext(),
                        userSessionInterface))
                .addInterceptor(dynamicTkpdAuthInterceptor)
                .build();
    }

    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    Retrofit provideRetrofitNoAuth(@ReactNoAuthQualifier OkHttpClient okHttpClient,  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getWS())
                .client(okHttpClient)
                .build();
    }

    @ReactDefaultAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    Retrofit provideRetrofitDefaultAuth(@ReactDefaultAuthQualifier OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getWS())
                .client(okHttpClient)
                .build();
    }

    @ReactDynamicAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    Retrofit provideRetrofitDynamicAuth(@ReactDynamicAuthQualifier OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getWS())
                .client(okHttpClient)
                .build();
    }

    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    CommonService provideCommonNoAuthService(@ReactNoAuthQualifier Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @ReactDefaultAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    CommonService provideCommonDefaultAuthService(@ReactDefaultAuthQualifier Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @ReactDynamicAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    CommonService provideCommonDynamicAuthService(@ReactDynamicAuthQualifier Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkFactory provdeReactNetworkFactory(@ReactNoAuthQualifier CommonService commonService) {
        return new ReactNetworkFactory(commonService);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkDefaultAuthFactory provideReactNetworkDefaultAuthFactory(@ReactDefaultAuthQualifier CommonService commonService) {
        return new ReactNetworkDefaultAuthFactory(commonService);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkAuthFactory provideReactNetworkAuthFactory(@ReactDynamicAuthQualifier CommonService commonService) {
        return new ReactNetworkAuthFactory(commonService);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkDataSource provideUnifyReactNetworkDataSource(@ReactNoAuthQualifier Retrofit.Builder retrofitBuilder,
                                                                   @ReactNoAuthQualifier OkHttpClient okHttpClient) {
        return new UnifyReactNetworkDataSource(retrofitBuilder, okHttpClient);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkAuthDataSource provideUnifyReactNetworkAuthDataSource(@ReactNoAuthQualifier Retrofit.Builder retrofitBuilder,
                                                                           @ReactNoAuthQualifier OkHttpClient okHttpClient,
                                                                           DigitalHmacAuthInterceptor digitalHmacAuthInterceptor,
                                                                           DynamicTkpdAuthInterceptor dynamicTkpdAuthInterceptor) {
        return new UnifyReactNetworkAuthDataSource(retrofitBuilder, okHttpClient, digitalHmacAuthInterceptor, dynamicTkpdAuthInterceptor);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkWsV4AuthDataSource provideUnifyReactNetworkWsV4AuthDataSource(Retrofit.Builder retrofitBuilder,
                                                                                   @ReactDefaultAuthQualifier OkHttpClient okHttpClient,
                                                                                   UserSessionInterface userSessionInterface) {
        return new UnifyReactNetworkWsV4AuthDataSource(retrofitBuilder, okHttpClient, userSessionInterface);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkBearerDataSource provideUnifyReactNetworkBearerDataSource(Retrofit.Builder retrofitBuilder,
                                                                               @ReactDynamicAuthQualifier OkHttpClient okHttpClient,
                                                                               UserSessionInterface userSessionInterface) {
        return new UnifyReactNetworkBearerDataSource(retrofitBuilder, okHttpClient, userSessionInterface);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkRepository provideUnifyReactNetworkRepository(UnifyReactNetworkDataSource unifyReactNetworkDataSource,
                                                                   UnifyReactNetworkAuthDataSource unifyReactNetworkAuthDataSource,
                                                                   UnifyReactNetworkBearerDataSource unifyReactNetworkBearerDataSource,
                                                                   UnifyReactNetworkWsV4AuthDataSource unifyReactNetworkWsV4AuthDataSource) {
        return new UnifyReactNetworkRepositoryImpl(unifyReactNetworkDataSource, unifyReactNetworkAuthDataSource, unifyReactNetworkBearerDataSource, unifyReactNetworkWsV4AuthDataSource);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkRepository provideReactNetworkRepository(ReactNetworkAuthFactory reactNetworkAuthFactory,
                                                         ReactNetworkFactory reactNetworkFactory,
                                                         ReactNetworkDefaultAuthFactory reactNetworkDefaultAuthFactory,
                                                         UserSessionInterface userSessionInterface) {
        return new ReactNetworkRepositoryImpl(
                reactNetworkAuthFactory,
                reactNetworkFactory,
                reactNetworkDefaultAuthFactory,
                userSessionInterface);
    }


    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    public Retrofit.Builder provideRetrofitBuilder(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}