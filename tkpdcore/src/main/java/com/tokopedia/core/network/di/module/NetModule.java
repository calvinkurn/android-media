package com.tokopedia.core.network.di.module;

import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.AccountsQualifier;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.BearerAuth;
import com.tokopedia.core.network.di.qualifier.BearerAuthTypeJsonUt;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoAuth;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.NoAuth;
import com.tokopedia.core.network.di.qualifier.NoAuthNoFingerprint;
import com.tokopedia.core.network.di.qualifier.RechargeQualifier;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsAuth;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ricoharisin on 3/23/17.
 */

@Module(includes = {OkHttpClientModule.class, RetrofitModule.class})
public class NetModule {

    @WsV4Qualifier
    @ApplicationScope
    @Provides
    public Retrofit provideWsV4Retrofit(@DefaultAuth OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN).client(okHttpClient).build();
    }

    @WsV4QualifierWithErrorHander
    @ApplicationScope
    @Provides
    public Retrofit provideWsV4RetrofitWithErrorHandler(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN).client(okHttpClient).build();
    }

    @AceQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideAceRetrofit(@NoAuth OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.ACE_DOMAIN).client(okHttpClient).build();
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsAuth OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOPADS_DOMAIN).client(okHttpClient).build();
    }

    @MojitoQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMojitoRetrofit(@MojitoAuth OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.MOJITO_DOMAIN).client(okHttpClient).build();
    }

    @HadesQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideHadesRetrofit(@NoAuth OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.HADES_DOMAIN).client(okHttpClient).build();
    }

    @AccountsQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideAccountsRetrofit(@BearerAuth OkHttpClient okHttpClient,
                                         Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN).client(okHttpClient).build();
    }

    @RechargeQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideRechargeRetrofit(@NoAuth OkHttpClient okHttpClient,
                                         Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.RECHARGE_API_DOMAIN).client(okHttpClient).build();
    }

    @YoutubeQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideYoutubeRetrofit(@NoAuth OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.GOOGLE_APIS).client(okHttpClient).build();
    }

    @MerlinQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMerlinRetrofit(@NoAuth OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.MERLIN_DOMAIN).client(okHttpClient).build();
    }

    @ResolutionQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideResolutionRetrofit(@BearerAuth OkHttpClient okHttpClient,
                                              Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION).client(okHttpClient).build();
    }

    @GoldMerchantQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideGoldMerchantRetrofit(@BearerAuthTypeJsonUt OkHttpClient okHttpClient,
                                                Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(TkpdBaseURL.GOLD_MERCHANT_DOMAIN).client(okHttpClient).build();
    }

    @CartQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideCartRetrofit(@BearerAuthTypeJsonUt OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOKOPEDIA_CART_DOMAIN).client(okHttpClient).build();
    }

}