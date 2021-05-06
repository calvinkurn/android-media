package com.tokopedia.attachproduct.di;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.attachproduct.data.model.mapper.TkpdResponseToAttachProductDomainModelMapper;
import com.tokopedia.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.attachproduct.data.repository.AttachProductRepositoryImpl;
import com.tokopedia.attachproduct.data.source.api.AttachProductApi;
import com.tokopedia.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl;
import com.tokopedia.attachproduct.domain.model.mapper.DataModelToDomainModelMapper;
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Hendri on 19/02/18.
 */

@Module
public class AttachProductModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }


    @AttachProductScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @AttachProductScope
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @AttachProductScope
    @Provides
    @AttachProductQualifier
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(AttachProductUrl.URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    public static AttachProductApi provideApiTome(Retrofit retrofit) {
        return retrofit.create(AttachProductApi.class);
    }

    @Provides
    @ChuckInterceptorAttachProductQualifier
    public static ChuckerInterceptor provideChuck(@ApplicationContext Context context) {
        return new ChuckerInterceptor(context);
    }

    @AttachProductScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @AttachProductScope
    @Provides
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                   NetworkRouter networkRouter,
                                                   UserSession userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @AttachProductScope
    @Provides
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter,
                                                         UserSession userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @AttachProductScope
    @Provides
    @AttachProductQualifier
    OkHttpClient provideOkHttpClient(OkHttpRetryPolicy retryPolicy,
                                     ErrorResponseInterceptor errorResponseInterceptor,
                                     @ChuckInterceptorAttachProductQualifier ChuckerInterceptor
                                             chuckInterceptor,
                                     @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                     TkpdAuthInterceptor tkpdAuthInterceptor,
                                     FingerprintInterceptor fingerprintInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor);
        }
        return builder.build();
    }

    @Provides
    public static GetShopProductService provideShopService(AttachProductApi api) {
        return new GetShopProductService(api);
    }

    @Provides
    public static AttachProductRepository provideAttachProductRepository(GetShopProductService shopService, TkpdResponseToAttachProductDomainModelMapper mapper) {
        return new AttachProductRepositoryImpl(shopService, mapper);
    }

    @Provides
    public static AttachProductUseCase provideAttachProductUseCase(AttachProductRepository repository, DataModelToDomainModelMapper mapper) {
        return new AttachProductUseCase(repository, mapper);
    }
}
