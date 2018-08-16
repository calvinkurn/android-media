package com.tokopedia.common_digital.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.constant.DigitalUrl;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.common.data.api.DigitalGqlApi;
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor;
import com.tokopedia.common_digital.product.data.datasource.CategoryDetailDataSource;
import com.tokopedia.common_digital.product.data.mapper.ProductDigitalMapper;
import com.tokopedia.common_digital.product.data.repository.DigitalCategoryRepository;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.common_digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.common_digital.product.domain.usecase.DigitalGetHelpUrlUseCase;
import com.tokopedia.common_digital.product.domain.usecase.GetCategoryByIdUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
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

    @DigitalScope
    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return ((NetworkRouter) context);
        }
        throw new RuntimeException("Application must implement " + NetworkRouter.class.getCanonicalName());
    }

    @DigitalScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @DigitalScope
    DigitalInterceptor provideDigitalInterceptor(@ApplicationContext Context context,
                                                 NetworkRouter networkRouter, UserSession userSession) {
        return new DigitalInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @DigitalScope
    public DigitalRouter provideDigitalRouter(@ApplicationContext Context context) {
        if (context instanceof DigitalRouter) {
            return ((DigitalRouter) context);
        }
        throw new RuntimeException("Application must implement " + DigitalRouter.class.getCanonicalName());
    }

    @Provides
    @DigitalScope
    @DigitalRestApiClient
    public OkHttpClient provideDigitalRestApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                                          DigitalRouter digitalRouter,
                                                          DigitalInterceptor digitalInterceptor,
                                                          NetworkRouter networkRouter,
                                                          UserSession userSession) {
        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);

        builder.addInterceptor(digitalInterceptor);
        builder.addInterceptor(new FingerprintInterceptor(networkRouter, userSession));
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse.class));
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(digitalRouter.getChuckInterceptor());
        }

        return builder.build();
    }

    @Provides
    @DigitalScope
    @DigitalGqlApiClient
    public OkHttpClient provideDigitalGqlApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                                         @ApplicationContext Context context,
                                                         DigitalRouter digitalRouter,
                                                         NetworkRouter networkRouter,
                                                         UserSession userSession) {
        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);

        builder.addInterceptor(new FingerprintInterceptor(networkRouter, userSession));
        builder.addInterceptor(new TkpdAuthInterceptor(context, networkRouter, userSession));
        builder.addInterceptor(new ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse.class));
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(digitalRouter.getChuckInterceptor());
        }

        return builder.build();
    }

    @Provides
    @DigitalScope
    @DigitalRestApiRetrofit
    public Retrofit provideDigitalRestApiRetrofit(@DigitalRestApiClient OkHttpClient okHttpClient,
                                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(DigitalUrl.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    @DigitalScope
    @DigitalGqlApiRetrofit
    public Retrofit provideDigitalGqlApiRetrofit(@DigitalGqlApiClient OkHttpClient okHttpClient,
                                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.HOME_DATA_BASE_URL).client(okHttpClient).build();
    }

    @Provides
    @DigitalScope
    public DigitalRestApi provideDigitalRestApi(@DigitalRestApiRetrofit Retrofit retrofit) {
        return retrofit.create(DigitalRestApi.class);
    }

    @Provides
    @DigitalScope
    public DigitalGqlApi provideDigitalGqlApi(@DigitalGqlApiRetrofit Retrofit retrofit) {
        return retrofit.create(DigitalGqlApi.class);
    }

    @Provides
    @DigitalScope
    ProductDigitalMapper provideProductDigitalMapper() {
        return new ProductDigitalMapper();
    }

    @Provides
    @DigitalScope
    CategoryDetailDataSource provideCategoryDetailDataSource(DigitalGqlApi digitalGqlApi,
                                                             CacheManager cacheManager,
                                                             ProductDigitalMapper productDigitalMapper,
                                                             @ApplicationContext Context context) {
        return new CategoryDetailDataSource(digitalGqlApi, cacheManager, productDigitalMapper, context);
    }

    @Provides
    @DigitalScope
    IDigitalCategoryRepository provideDigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource) {
        return new DigitalCategoryRepository(categoryDetailDataSource);
    }

    @Provides
    @DigitalScope
    public GetCategoryByIdUseCase provideGetCategoryByIdUseCase(IDigitalCategoryRepository digitalCategoryRepository,
                                                                UserSession userSession) {
        return new GetCategoryByIdUseCase(digitalCategoryRepository, userSession);
    }

    @Provides
    @DigitalScope
    public DigitalGetHelpUrlUseCase provideDigitalGetHelpUrlUseCase(IDigitalCategoryRepository digitalCategoryRepository) {
        return new DigitalGetHelpUrlUseCase(digitalCategoryRepository);
    }

}
