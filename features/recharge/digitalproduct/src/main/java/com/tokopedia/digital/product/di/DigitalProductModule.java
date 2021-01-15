package com.tokopedia.digital.product.di;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common_digital.common.data.api.DigitalResponseConverter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApi;
import com.tokopedia.digital.common.data.apiservice.DigitalHmacAuthInterceptor;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.di.DigitalRestApiRetrofit;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.product.domain.interactor.GetOperatorsByCategoryIdUseCase;
import com.tokopedia.digital.product.domain.interactor.GetProductsByOperatorIdUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by furqan on 25/06/18.
 */

@Module
public class DigitalProductModule {

    @Provides
    @DigitalProductScope
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, "DIGITAL_LAST_INPUT_CLIENT_NUMBER");
    }

    @Provides
    @DigitalProductScope
    ChuckerInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckerInterceptor(context);
    }

    @Provides
    @DigitalProductScope
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return ((NetworkRouter) context);
    }

    @Provides
    @DigitalProductScope
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter, UserSessionInterface userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @Provides
    @DigitalProductScope
    DigitalHmacAuthInterceptor provideDigitalHmacAuthInterceptor(@ApplicationContext Context context,
                                                                 NetworkRouter networkRouter,
                                                                 UserSessionInterface userSession) {
        return new DigitalHmacAuthInterceptor(context, networkRouter, userSession, TkpdBaseURL.DigitalApi.HMAC_KEY);
    }

    @Provides
    @DigitalProductScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    @Provides
    @DigitalProductScope
    DigitalGqlApi provideDigitalGqlApiService(Gson gson, OkHttpClient client) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(TokopediaUrl.Companion.getInstance().getGQL())
                .addConverterFactory(new DigitalResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        retrofitBuilder.client(client);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(DigitalGqlApi.class);
    }

    @Provides
    @DigitalProductScope
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Provides
    @DigitalProductScope
    OkHttpClient provideOkHttpClient(DigitalHmacAuthInterceptor digitalHmacAuthInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor,
                                     HttpLoggingInterceptor httpLoggingInterceptor,
                                     OkHttpRetryPolicy okHttpRetryPolicy,
                                     ChuckerInterceptor chuckInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(fingerprintInterceptor)
                .addInterceptor(digitalHmacAuthInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();

    }

    @Provides
    @DigitalProductScope
    ProductDigitalMapper provideProductDigitalMapper() {
        return new ProductDigitalMapper();
    }

    @Provides
    @DigitalProductScope
    CategoryDetailDataSource provideCategoryDetailDataSource(DigitalGqlApi digitalGqlApi,
                                                             ProductDigitalMapper productDigitalMapper,
                                                             @ApplicationContext Context context) {
        return new CategoryDetailDataSource(digitalGqlApi, productDigitalMapper, context);
    }

    @Provides
    @DigitalProductScope
    IDigitalCategoryRepository provideDigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource) {
        return new DigitalCategoryRepository(categoryDetailDataSource);
    }

    @Provides
    @DigitalProductScope
    GetDigitalCategoryByIdUseCase provideGetDigitalCategoryByIdUseCase(UserSessionInterface userSession,
                                                                       IDigitalCategoryRepository digitalCategoryRepository) {
        return new GetDigitalCategoryByIdUseCase(digitalCategoryRepository, userSession);
    }

    @Provides
    @DigitalProductScope
    GetOperatorsByCategoryIdUseCase provideGetOperatorsByCategoryIdUseCase(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        return new GetOperatorsByCategoryIdUseCase(getDigitalCategoryByIdUseCase);
    }

    @Provides
    @DigitalProductScope
    GetProductsByOperatorIdUseCase provideGetProductsByOperatorIdUseCase(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        return new GetProductsByOperatorIdUseCase(getDigitalCategoryByIdUseCase);
    }

    @Provides
    @DigitalProductScope
    DigitalRestApi provideDigitalRestApi(@DigitalRestApiRetrofit Retrofit retrofit) {
        return retrofit.create(DigitalRestApi.class);
    }
}
