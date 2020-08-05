package com.tokopedia.reviewseller.feature.reputationhistory.di;

import android.content.Context;
import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.InboxQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.reviewseller.feature.reputationhistory.data.repository.ReputationRepositoryImpl;
import com.tokopedia.reviewseller.feature.reputationhistory.data.repository.ReputationReviewRepositoryImpl;
import com.tokopedia.reviewseller.feature.reputationhistory.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.reviewseller.feature.reputationhistory.data.source.cloud.apiservice.api.SellerReputationApi;
import com.tokopedia.reviewseller.feature.reputationhistory.domain.ReputationRepository;
import com.tokopedia.reviewseller.feature.reputationhistory.domain.ReputationReviewRepository;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 2/13/18.
 */

@Module
@SellerReputationScope
public class SellerReputationModule {

    @SellerReputationScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter,
                                                                UserSession userSession){
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @SellerReputationScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                          NetworkRouter networkRouter,
                                                          UserSession userSession){
            return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @SellerReputationScope
    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @SellerReputationScope
    @Provides
    public ChuckerInterceptor provideChuckerInterceptor(@ApplicationContext Context context){
        ChuckerCollector collector = new ChuckerCollector(context, GlobalConfig.isAllowDebuggingTools());
        return new ChuckerInterceptor(context, collector);
    }


    @SellerReputationScope
    @Provides
    public DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @SellerReputationScope
    @Provides
    TkpdErrorResponseInterceptor provideTkpdErrorResponseInterceptor() {
        return new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @SellerReputationScope
    @Provides
    public OkHttpClient provideOkHttpClient(FingerprintInterceptor fingerprintInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            ChuckerInterceptor chuckInterceptor,
                                            DebugInterceptor debugInterceptor,
                                            TkpdErrorResponseInterceptor errorHandlerInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(chuckInterceptor)
                .addInterceptor(debugInterceptor)
                .addInterceptor(errorHandlerInterceptor)
                .build();
    }

    @SellerReputationScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @InboxQualifier
    @SellerReputationScope
    @Provides
    public Retrofit provideInboxRetrofit(OkHttpClient okHttpClient,
                                         Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(TkpdBaseURL.INBOX_DOMAIN).client(okHttpClient).build();
    }

    @Provides
    @SellerReputationScope
    public SellerReputationApi provideProductActionApi(@InboxQualifier Retrofit retrofit){
        return retrofit.create(SellerReputationApi.class);
    }

    @Provides
    @SellerReputationScope
    public ReputationRepository provideReputationRepository(CloudReputationReviewDataSource cloudReputationReviewDataSource){
        return new ReputationRepositoryImpl(cloudReputationReviewDataSource);
    }

    @Provides
    @SellerReputationScope
    public ReputationReviewRepository proReputationReviewRepository(CloudReputationReviewDataSource cloudReputationReviewDataSource, ShopInfoRepositoryImpl shopInfoRepository){
        return new ReputationReviewRepositoryImpl(cloudReputationReviewDataSource, shopInfoRepository);
    }

    @Provides
    @SellerReputationScope
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

}
