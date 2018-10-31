package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.qualifier.LoyaltyModuleQualifier;
import com.tokopedia.loyalty.di.qualifier.PromoQualifier;
import com.tokopedia.loyalty.di.qualifier.TokopointGqlQualifier;
import com.tokopedia.loyalty.di.qualifier.TxPaymentQualifier;
import com.tokopedia.loyalty.domain.apiservice.PromoApi;
import com.tokopedia.loyalty.domain.apiservice.TXPaymentVoucherApi;
import com.tokopedia.loyalty.domain.apiservice.TokoPointAuthInterceptor;
import com.tokopedia.loyalty.domain.apiservice.TokoPointGqlApi;
import com.tokopedia.loyalty.domain.apiservice.RetrofitFactory;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.domain.repository.IPromoResponseMapper;
import com.tokopedia.loyalty.domain.repository.ITokoPointDBService;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.repository.ITokoPointResponseMapper;
import com.tokopedia.loyalty.domain.repository.PromoRepository;
import com.tokopedia.loyalty.domain.repository.PromoResponseMapper;
import com.tokopedia.loyalty.domain.repository.TokoPointRepository;
import com.tokopedia.loyalty.domain.repository.TokoPointResponseMapper;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@Module(includes = {CacheDBModule.class})
public class ServiceApiModule {

    @Provides
    @LoyaltyScope
    ITokoPointResponseMapper provideITokoPointResponseMapper() {
        return new TokoPointResponseMapper();
    }

    @Provides
    @LoyaltyScope
    IPromoResponseMapper provideIPromoResponseMapper() {
        return new PromoResponseMapper();
    }

    @Provides
    @LoyaltyScope
    @TokopointGqlQualifier
    Retrofit provideRetrofitTokopointGQL(@ApplicationContext Context context,
                                         @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                         @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TokoPointAuthInterceptor(context, networkRouter, userSession));
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitTokoPointConfig(TkpdBaseURL.DEFAULT_TOKOPEDIA_GQL_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @LoyaltyScope
    @PromoQualifier
    Retrofit provideRetrofitLoyalty(@ApplicationContext Context context,
                                         @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                         @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, networkRouter, userSession));
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitPromoConfig(TkpdBaseURL.Transaction.URL_TX_PAYMENT_VOUCHER)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @LoyaltyScope
    @TxPaymentQualifier
    Retrofit provideRetrofitTxPayment(@ApplicationContext Context context,
                                    @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                    @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TkpdBaseInterceptor());
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitPromoConfig(TkpdBaseURL.PROMO_API_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @LoyaltyScope
    @LoyaltyModuleQualifier
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context){
        if(context instanceof NetworkRouter){
            return ((NetworkRouter)context);
        }
        return null;
    }

    @Provides
    @LoyaltyScope
    @LoyaltyModuleQualifier
    UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @Provides
    @LoyaltyScope
    TokoPointGqlApi provideTokoPointGqlApi(@TokopointGqlQualifier Retrofit retrofit){
        return retrofit.create(TokoPointGqlApi.class);
    }

    @Provides
    @LoyaltyScope
    PromoApi providePromoApi(@PromoQualifier Retrofit retrofit){
        return retrofit.create(PromoApi.class);
    }

    @Provides
    @LoyaltyScope
    TXPaymentVoucherApi provideTxPaymentVoucherApi(@TxPaymentQualifier Retrofit retrofit){
        return retrofit.create(TXPaymentVoucherApi.class);
    }

    @Provides
    @LoyaltyScope
    ITokoPointRepository provideITokoPointRepository(TokoPointGqlApi tokoPointGqlApi,
                                                     ITokoPointDBService tokoPointDBService,
                                                     TokoPointResponseMapper tokoPointResponseMapper,
                                                     TXPaymentVoucherApi txPaymentVoucherApi
    ) {
        return new TokoPointRepository(
                tokoPointGqlApi,
                tokoPointDBService,
                tokoPointResponseMapper,
                txPaymentVoucherApi);
    }

    @Provides
    @LoyaltyScope
    IPromoRepository provideIPromoRepository(PromoApi promoApi,
                                             IPromoResponseMapper iPromoResponseMapper) {
        return new PromoRepository(promoApi, iPromoResponseMapper);
    }
}
