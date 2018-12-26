package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.qualifier.DigitalQualifier;
import com.tokopedia.loyalty.di.qualifier.LoyaltyModuleQualifier;
import com.tokopedia.loyalty.di.qualifier.PromoQualifier;
import com.tokopedia.loyalty.di.qualifier.TokopointGqlQualifier;
import com.tokopedia.loyalty.di.qualifier.TokopointQualifier;
import com.tokopedia.loyalty.di.qualifier.TxPaymentQualifier;
import com.tokopedia.loyalty.domain.apiservice.DigitalApi;
import com.tokopedia.loyalty.domain.apiservice.DigitalHmacAuthInterceptor;
import com.tokopedia.loyalty.domain.apiservice.PromoApi;
import com.tokopedia.loyalty.domain.apiservice.TXPaymentVoucherApi;
import com.tokopedia.loyalty.domain.apiservice.TokoPointApi;
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
    ITokoPointResponseMapper provideITokoPointResponseMapper() {
        return new TokoPointResponseMapper();
    }

    @Provides
    IPromoResponseMapper provideIPromoResponseMapper() {
        return new PromoResponseMapper();
    }

    @Provides
    @TokopointGqlQualifier
    Retrofit provideRetrofitTokopointGQL(@ApplicationContext Context context,
                                         @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                         @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TokoPointAuthInterceptor(context, networkRouter, userSession, TkpdBaseURL.TokoPoint.HMAC_KEY));
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitTokoPointConfig(TkpdBaseURL.DEFAULT_TOKOPEDIA_GQL_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @TokopointQualifier
    Retrofit provideRetrofitTokopoint(@ApplicationContext Context context,
                                         @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                         @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TokoPointAuthInterceptor(context, networkRouter, userSession, TkpdBaseURL.TokoPoint.HMAC_KEY));
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitTokoPointConfig(TkpdBaseURL.TOKOPOINT_API_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @PromoQualifier
    Retrofit provideRetrofitLoyalty(@ApplicationContext Context context,
                                         @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                         @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TkpdAuthInterceptor(context, networkRouter, userSession));
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitPromoConfig(TkpdBaseURL.PROMO_API_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @TxPaymentQualifier
    Retrofit provideRetrofitTxPayment(@ApplicationContext Context context,
                                    @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                    @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new TkpdBaseInterceptor());
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitPromoConfig(TkpdBaseURL.Transaction.URL_TX_PAYMENT_VOUCHER)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @DigitalQualifier
    Retrofit provideRetrofitDigital(@ApplicationContext Context context,
                                      @LoyaltyModuleQualifier NetworkRouter networkRouter,
                                      @LoyaltyModuleQualifier UserSession userSession){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context,builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor(networkRouter,userSession));
        tkpdOkHttpBuilder.addInterceptor(new DigitalHmacAuthInterceptor(context, networkRouter, userSession, TkpdBaseURL.DigitalApi.HMAC_KEY));
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return RetrofitFactory.createRetrofitDigitalConfig(TkpdBaseURL.DIGITAL_API_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @LoyaltyModuleQualifier
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context){
        if(context instanceof NetworkRouter){
            return ((NetworkRouter)context);
        }
        return null;
    }

    @Provides
    @LoyaltyModuleQualifier
    UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }

    @Provides
    TokoPointGqlApi provideTokoPointGqlApi(@TokopointGqlQualifier Retrofit retrofit){
        return retrofit.create(TokoPointGqlApi.class);
    }

    @Provides
    PromoApi providePromoApi(@PromoQualifier Retrofit retrofit){
        return retrofit.create(PromoApi.class);
    }

    @Provides
    TXPaymentVoucherApi provideTxPaymentVoucherApi(@TxPaymentQualifier Retrofit retrofit){
        return retrofit.create(TXPaymentVoucherApi.class);
    }

    @Provides
    ITokoPointRepository provideITokoPointRepository(TokoPointGqlApi tokoPointGqlApi,
                                                     ITokoPointDBService tokoPointDBService,
                                                     TokoPointResponseMapper tokoPointResponseMapper,
                                                     TXPaymentVoucherApi txPaymentVoucherApi,
                                                     DigitalApi digitalApi,
                                                     TokoPointApi tokoPointApi
    ) {
        return new TokoPointRepository(
                tokoPointGqlApi,
                tokoPointDBService,
                tokoPointResponseMapper,
                txPaymentVoucherApi, digitalApi, tokoPointApi);
    }

    @Provides
    DigitalApi provideDigitalApi(@DigitalQualifier Retrofit retrofit){
        return retrofit.create(DigitalApi.class);
    }

    @Provides
    TokoPointApi provideTokopointApi(@TokopointQualifier Retrofit retrofit){
        return retrofit.create(TokoPointApi.class);
    }

    @Provides
    IPromoRepository provideIPromoRepository(PromoApi promoApi,
                                             IPromoResponseMapper iPromoResponseMapper) {
        return new PromoRepository(promoApi, iPromoResponseMapper);
    }
}
