package com.tokopedia.core.network.di.module;

import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.constants.TkpdBaseURL;
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
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoNoRetryAuth;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoSmallTimeoutNoAuth;
import com.tokopedia.core.network.di.qualifier.MojitoWishlistActionQualifier;
import com.tokopedia.core.network.di.qualifier.NoAuth;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.TomeBearerAuth;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Auth;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.url.TokopediaUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ricoharisin on 3/23/17.
 */

@Deprecated
@Module(includes = {OkHttpClientModule.class, RetrofitModule.class})
public class NetModule {

    @WsV4Qualifier
    @ApplicationScope
    @Provides
    public Retrofit provideWsV4Retrofit(@DefaultAuth OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getWS()).client(okHttpClient).build();
    }

    @WsV4QualifierWithErrorHander
    @ApplicationScope
    @Provides
    public Retrofit provideWsV4RetrofitWithErrorHandler(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient,
                                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getWS()).client(okHttpClient).build();
    }

    @AceQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideAceRetrofit(@NoAuth OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getACE()).client(okHttpClient).build();
    }

    @MojitoQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMojitoRetrofit(@MojitoAuth OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getMOJITO()).client(okHttpClient).build();
    }

    @MojitoGetWishlistQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMojitoGetWishlistRetrofit(@MojitoSmallTimeoutNoAuth OkHttpClient okHttpClient,
                                                     Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getMOJITO()).client(okHttpClient).build();
    }

    @MojitoWishlistActionQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMojitoWishlistActionRetrofit(@MojitoNoRetryAuth OkHttpClient okHttpClient,
                                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getMOJITO()).client(okHttpClient).build();
    }

    @HadesQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideHadesRetrofit(@NoAuth OkHttpClient okHttpClient,
                                         Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getHADES()).client(okHttpClient).build();
    }

    @YoutubeQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideYoutubeRetrofit(Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.GOOGLE_APIS).client(new OkHttpClient()).build();
    }

    @MerlinQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMerlinRetrofit(@NoAuth OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.MERLIN_DOMAIN).client(okHttpClient).build();
    }

    @TomeQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideTomeRetrofit(@TomeBearerAuth OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getTOME()).client(okHttpClient).build();
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
                                                Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.GOLD_MERCHANT_DOMAIN).client(okHttpClient).build();
    }

    @CartQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideCartRetrofit(@BearerAuthTypeJsonUt OkHttpClient okHttpClient,
                                        Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOKOPEDIA_CART_DOMAIN).client(okHttpClient).build();
    }

    @UploadWsV4Qualifier
    @ApplicationScope
    @Provides
    public Retrofit provideUploadWsV4Retrofit(@UploadWsV4Auth OkHttpClient okHttpClient,
                                              Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getWS()).client(okHttpClient).build();
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsQualifier OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TokopediaUrl.Companion.getInstance().getTA()).client(okHttpClient).build();
    }
}