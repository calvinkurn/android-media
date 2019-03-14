package com.tokopedia.digital.product.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common_digital.common.data.api.DigitalResponseConverter;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.common.di.DigitalRestApiRetrofit;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApi;
import com.tokopedia.digital.common.data.apiservice.DigitalHmacAuthInterceptor;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.data.source.CategoryListDataSource;
import com.tokopedia.digital.common.data.source.StatusDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.product.data.mapper.USSDMapper;
import com.tokopedia.digital.product.data.repository.UssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.interactor.DigitalGetHelpUrlUseCase;
import com.tokopedia.digital.product.domain.interactor.GetOperatorsByCategoryIdUseCase;
import com.tokopedia.digital.product.domain.interactor.GetProductsByOperatorIdUseCase;
import com.tokopedia.digital.product.domain.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.domain.interactor.ProductDigitalInteractor;
import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository;
import com.tokopedia.digital.widget.domain.interactor.DigitalRecommendationUseCase;
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetUseCase;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.user.session.UserSession;

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
    USSDMapper provideUSSDMapper() {
        return new USSDMapper();
    }

    @Provides
    @DigitalProductScope
    IUssdCheckBalanceRepository provideUssdCheckBalanceRepository(DigitalRestApi digitalRestApi,
                                                                  USSDMapper ussdMapper) {
        return new UssdCheckBalanceRepository(digitalRestApi, ussdMapper);
    }

    @Provides
    @DigitalProductScope
    IProductDigitalInteractor provideProductDigitalInteractor(IUssdCheckBalanceRepository ussdCheckBalanceRepository) {
        return new ProductDigitalInteractor(ussdCheckBalanceRepository);
    }

    @Provides
    @DigitalProductScope
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return ((NetworkRouter) context);
    }

    @Provides
    @DigitalProductScope
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter, UserSession userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @Provides
    @DigitalProductScope
    DigitalHmacAuthInterceptor provideDigitalHmacAuthInterceptor(@ApplicationContext Context context,
                                                                 NetworkRouter networkRouter,
                                                                 UserSession userSession) {
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
                .baseUrl(TkpdBaseURL.HOME_DATA_BASE_URL)
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
                                     Gson gson) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.addInterceptor(fingerprintInterceptor)
                .addInterceptor(digitalHmacAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .build();

    }

    @Provides
    @DigitalProductScope
    ProductDigitalMapper provideProductDigitalMapper() {
        return new ProductDigitalMapper();
    }

    @Provides
    @DigitalProductScope
    CategoryDetailDataSource provideCategoryDetailDataSource(DigitalGqlApi digitalGqlApi,
                                                             CacheManager cacheManager,
                                                             ProductDigitalMapper productDigitalMapper,
                                                             @ApplicationContext Context context) {
        return new CategoryDetailDataSource(digitalGqlApi, cacheManager, productDigitalMapper, context);
    }

    @Provides
    @DigitalProductScope
    IDigitalCategoryRepository provideDigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource) {
        return new DigitalCategoryRepository(categoryDetailDataSource);
    }

    @Provides
    @DigitalProductScope
    GetDigitalCategoryByIdUseCase provideGetDigitalCategoryByIdUseCase(UserSession userSession,
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
    DigitalGetHelpUrlUseCase provideDigitalGetHelpUrlUseCase(IDigitalCategoryRepository digitalCategoryRepository) {
        return new DigitalGetHelpUrlUseCase(digitalCategoryRepository);
    }

    @Provides
    @DigitalProductScope
    com.tokopedia.digital.common.data.apiservice.DigitalRestApi provideDigitalRestApi(@DigitalRestApiRetrofit Retrofit retrofit) {
        return retrofit.create(com.tokopedia.digital.common.data.apiservice.DigitalRestApi.class);
    }

    @Provides
    @DigitalProductScope
    StatusMapper provideStatusMapper() {
        return new StatusMapper();
    }

    @Provides
    @DigitalProductScope
    CategoryMapper provideCategoryMapper() {
        return new CategoryMapper();
    }

    @Provides
    @DigitalProductScope
    StatusDataSource provideStatusDataSource(com.tokopedia.digital.common.data.apiservice.DigitalRestApi digitalRestApi, CacheManager cacheManager, StatusMapper statusMapper) {
        return new StatusDataSource(digitalRestApi, cacheManager, statusMapper);
    }

    @Provides
    @DigitalProductScope
    CategoryListDataSource provideCategoryListDataSource(com.tokopedia.digital.common.data.apiservice.DigitalRestApi digitalRestApi, CacheManager cacheManager, CategoryMapper categoryMapper) {
        return new CategoryListDataSource(digitalRestApi, cacheManager, categoryMapper);
    }

    @Provides
    @DigitalProductScope
    DigitalWidgetRepository provideDigitalWidgetRepository(StatusDataSource statusDataSource, CategoryListDataSource categoryListDataSource) {
        return new DigitalWidgetRepository(statusDataSource, categoryListDataSource);
    }

    @Provides
    @DigitalProductScope
    DigitalWidgetUseCase provideDigitalWidgetUseCase(@ApplicationContext Context context, DigitalWidgetRepository digitalWidgetRepository) {
        return new DigitalWidgetUseCase(context, digitalWidgetRepository);
    }

    @Provides
    @DigitalProductScope
    DigitalRecommendationUseCase provideDigitalRecommendationUseCase(@ApplicationContext Context context){
        return new DigitalRecommendationUseCase(new GraphqlUseCase(), context);
    }
}
