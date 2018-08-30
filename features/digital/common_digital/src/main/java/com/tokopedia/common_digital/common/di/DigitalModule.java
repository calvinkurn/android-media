package com.tokopedia.common_digital.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.common_digital.cart.data.datasource.DigitalAddToCartDataSource;
import com.tokopedia.common_digital.cart.data.datasource.DigitalInstantCheckoutDataSource;
import com.tokopedia.common_digital.cart.data.mapper.CartMapperData;
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.common_digital.cart.data.repository.DigitalCartRepository;
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.constant.DigitalUrl;
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
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

//    @Provides
//    @DigitalScope
//    @DigitalGqlApiClient
//    public OkHttpClient provideDigitalGqlApiOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
//                                                         @ApplicationContext Context context,
//                                                         DigitalRouter digitalRouter,
//                                                         NetworkRouter networkRouter,
//                                                         UserSession userSession) {
//        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
//                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
//                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
//
//        builder.addInterceptor(new FingerprintInterceptor(networkRouter, userSession));
//        builder.addInterceptor(new TkpdAuthInterceptor(context, networkRouter, userSession));
//        builder.addInterceptor(new ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse.class));
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            builder.addInterceptor(httpLoggingInterceptor)
//                    .addInterceptor(digitalRouter.getChuckInterceptor());
//        }
//
//        return builder.build();
//    }

//    @DigitalScope
//    @DigitalGqlApiRetrofit
//    @Provides
//    public Retrofit.Builder provideRetrofitBuilder(Gson gson) {
//        return new Retrofit.Builder()
//                .addConverterFactory(new StringResponseConverter())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
//    }

    @Provides
    @DigitalScope
    @DigitalRestApiRetrofit
    public Retrofit provideDigitalRestApiRetrofit(@DigitalRestApiClient OkHttpClient okHttpClient,
                                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(DigitalUrl.BASE_URL).client(okHttpClient).build();
    }

//    @Provides
//    @DigitalScope
//    @DigitalGqlApiRetrofit
//    public Retrofit provideDigitalGqlApiRetrofit(@DigitalGqlApiClient OkHttpClient okHttpClient,
//                                                 @DigitalGqlApiRetrofit Retrofit.Builder retrofitBuilder) {
//        return retrofitBuilder.baseUrl(TkpdBaseURL.HOME_DATA_BASE_URL).client(okHttpClient).build();
//    }

    @Provides
    @DigitalScope
    public DigitalRestApi provideDigitalRestApi(@DigitalRestApiRetrofit Retrofit retrofit) {
        return retrofit.create(DigitalRestApi.class);
    }

//    @Provides
//    @DigitalScope
//    public DigitalGqlApi provideDigitalGqlApi(@DigitalGqlApiRetrofit Retrofit retrofit) {
//        return retrofit.create(DigitalGqlApi.class);
//    }

//    @Provides
//    @DigitalScope
//    ProductDigitalMapper provideProductDigitalMapper() {
//        return new ProductDigitalMapper();
//    }

//    @Provides
//    @DigitalScope
//    AgentDigitalCategoryDetailDataSource provideCategoryDetailDataSource(DigitalGqlApi digitalGqlApi,
//                                                                         CacheManager cacheManager,
//                                                                         ProductDigitalMapper productDigitalMapper,
//                                                                         @ApplicationContext Context context) {
//        return new AgentDigitalCategoryDetailDataSource(digitalGqlApi, cacheManager, productDigitalMapper, context);
//    }

//    @Provides
//    @DigitalScope
//    IAgentDigitalCategoryRepository provideDigitalCategoryRepository(AgentDigitalCategoryDetailDataSource agentDigitalCategoryDetailDataSource) {
//        return new AgentDigitalCategoryRepository(agentDigitalCategoryDetailDataSource);
//    }

//    @Provides
//    @DigitalScope
//    public GetAgentDigitalCategoryByIdUseCase provideGetAgentDigitalCategoryByIdUseCase(IAgentDigitalCategoryRepository digitalCategoryRepository,
//                                                                                        UserSession userSession) {
//        return new GetAgentDigitalCategoryByIdUseCase(digitalCategoryRepository, userSession);
//    }

    @Provides
    @DigitalScope
    ICartMapperData provideCartMapperData() {
        return new CartMapperData();
    }

    @Provides
    @DigitalScope
    public DigitalAddToCartDataSource provideDigitalAddToCartDataSource(DigitalRestApi digitalRestApi,
                                                                        ICartMapperData cartMapperData) {
        return new DigitalAddToCartDataSource(digitalRestApi, cartMapperData);
    }

    @Provides
    @DigitalScope DigitalInstantCheckoutDataSource provideDigitalInstantCheckoutDataSource(DigitalRestApi digitalRestApi,
                                                                                           ICartMapperData cartMapperData) {
        return new DigitalInstantCheckoutDataSource(digitalRestApi, cartMapperData);
    }

    @Provides
    @DigitalScope
    public IDigitalCartRepository provideDigitalCartRepository(DigitalAddToCartDataSource digitalAddToCartDataSource,
                                                               DigitalInstantCheckoutDataSource digitalInstantCheckoutDataSource) {
        return new DigitalCartRepository(digitalAddToCartDataSource, digitalInstantCheckoutDataSource);
    }

    @Provides
    @DigitalScope
    public DigitalAddToCartUseCase provideDigitalAddToCartUseCase(IDigitalCartRepository digitalCartRepository) {
        return new DigitalAddToCartUseCase(digitalCartRepository);
    }

    @Provides
    @DigitalScope
    public DigitalInstantCheckoutUseCase provideDigitalInstantCheckoutUseCase(IDigitalCartRepository digitalCartRepository) {
        return new DigitalInstantCheckoutUseCase(digitalCartRepository);
    }

}
