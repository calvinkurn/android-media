package com.tokopedia.otp.cotp.di;

import com.tokopedia.otp.common.di.MethodListQualifier;
import com.tokopedia.otp.cotp.data.CotpApi;
import com.tokopedia.otp.cotp.data.CotpMethodListApi;
import com.tokopedia.otp.cotp.data.CotpUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 4/24/18.
 */


@CotpScope
@Module
public class CotpModule {

    @CotpScope
    @Provides
    @CotpQualifier
    public Retrofit provideCotpRetrofit(Retrofit.Builder retrofitBuilder,
                                        OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(CotpUrl.BASE_URL).client(okHttpClient).build();
    }

    @CotpScope
    @Provides
    public CotpApi provideCotpApi(@CotpQualifier Retrofit retrofit) {
        return retrofit.create(CotpApi.class);
    }

    @CotpScope
    @Provides
    @MethodListQualifier
    public Retrofit provideCotpMethodListQualifierRetrofit(Retrofit.Builder
                                                                   retrofitBuilder,
                                                           @MethodListQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(CotpUrl.BASE_URL).client(okHttpClient).build();
    }

    @CotpScope
    @Provides
    public CotpMethodListApi provideCotpMethodListApi(@MethodListQualifier Retrofit retrofit) {
        return retrofit.create(CotpMethodListApi.class);
    }
}
