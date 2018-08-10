package com.tokopedia.attachproduct.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.attachproduct.data.model.mapper.TkpdResponseToAttachProductDomainModelMapper;
import com.tokopedia.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.attachproduct.data.repository.AttachProductRepositoryImpl;
import com.tokopedia.attachproduct.data.source.api.TomeGetShopProductAPI;
import com.tokopedia.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl;
import com.tokopedia.attachproduct.domain.model.mapper.DataModelToDomainModelMapper;
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.AuthUtil;
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


    @AttachProductScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @AttachProductScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
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
    public static TomeGetShopProductAPI provideApiTome(Retrofit retrofit){
        return retrofit.create(TomeGetShopProductAPI.class);
    }

    @AttachProductScope
    @Provides
    @AttachProductQualifier
    OkHttpClient provideOkHttpClient(OkHttpRetryPolicy retryPolicy,
                                     ErrorResponseInterceptor errorResponseInterceptor,
                                     @ApplicationContext Context context) {
        UserSession userSession = new UserSession(context);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new FingerprintInterceptor((NetworkRouter)context,userSession))
                .addInterceptor(new CacheApiInterceptor())
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(new TkpdAuthInterceptor(context,(NetworkRouter)context,userSession))
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);
//TODO Implement chuck
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            builder.addInterceptor(chuckInterceptor)
//                    .addInterceptor(httpLoggingInterceptor);
//        }
        return builder.build();
    }

    @Provides
    public static GetShopProductService provideShopService(TomeGetShopProductAPI api){
        return new GetShopProductService(api);
    }

    @Provides
    public static AttachProductRepository provideAttachProductRepository(GetShopProductService shopService,TkpdResponseToAttachProductDomainModelMapper mapper){
        return new AttachProductRepositoryImpl(shopService, mapper);
    }

    @Provides
    public static AttachProductUseCase provideAttachProductUseCase(AttachProductRepository repository, DataModelToDomainModelMapper mapper){
        return new AttachProductUseCase(repository,mapper);
    }
}
