package com.tokopedia.digital.newcart.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.digital.newcart.data.DigitalDealsApi;
import com.tokopedia.digital.newcart.data.DigitalDealsDataSourceFactory;
import com.tokopedia.digital.newcart.data.DigitalDealsRepositoryImpl;
import com.tokopedia.digital.newcart.data.DigitalDealsUrl;
import com.tokopedia.digital.newcart.domain.DigitalDealGetProductsUseCase;
import com.tokopedia.digital.newcart.domain.DigitalDealsGetCategoriesUseCase;
import com.tokopedia.digital.newcart.domain.DigitalDealsRepository;
import com.tokopedia.digital.newcart.domain.model.mapper.DealCategoryViewModelMapper;
import com.tokopedia.digital.newcart.domain.model.mapper.DealProductViewModelMapper;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.utils.OkHttpRetryPolicy;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DigitalCartDealsModule {
    public DigitalCartDealsModule() {
    }

    @Provides
    @DigitalDealsQualifier
    @DigitalDealsScope
    OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }


    @Provides
    @DigitalDealsQualifier
    @DigitalDealsScope
    Retrofit.Builder provideRetrofitBuilder(@DigitalDealsQualifier Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    @DigitalDealsQualifier
    @Provides
    @DigitalDealsScope
    Gson provideGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Provides
    @DigitalDealsQualifier
    @DigitalDealsScope
    Retrofit provideDigitalRestApiRetrofit(@DigitalDealsQualifier OkHttpClient okHttpClient,
                                           @DigitalDealsQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(DigitalDealsUrl.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    @DigitalDealsScope
    DigitalDealsApi provideApi(@DigitalDealsQualifier Retrofit retrofit) {
        return retrofit.create(DigitalDealsApi.class);
    }

    @Provides
    @DigitalDealsScope
    DigitalDealsDataSourceFactory provideDataSourceFactory(DigitalDealsApi digitalDealsApi) {
        return new DigitalDealsDataSourceFactory(digitalDealsApi);
    }

    @Provides
    @DigitalDealsScope
    DigitalDealsRepository provideDealsRepository(DigitalDealsDataSourceFactory dataSourceFactory, DealCategoryViewModelMapper mapper, DealProductViewModelMapper productViewModelMapper) {
        return new DigitalDealsRepositoryImpl(dataSourceFactory, mapper, productViewModelMapper);
    }

    @Provides
    @DigitalDealsScope
    DigitalDealsGetCategoriesUseCase provideDealsGetCategoriesUseCase(DigitalDealsRepository repository) {
        return new DigitalDealsGetCategoriesUseCase(repository);
    }

    @Provides
    @DigitalDealsScope
    DigitalDealGetProductsUseCase provideDigitalDealGetProductsUseCase(DigitalDealsRepository repository) {
        return new DigitalDealGetProductsUseCase(repository);
    }
}
