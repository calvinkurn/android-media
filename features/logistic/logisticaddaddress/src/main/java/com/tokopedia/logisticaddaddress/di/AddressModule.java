package com.tokopedia.logisticaddaddress.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenter;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressPresenterImpl;
import com.tokopedia.logisticaddaddress.data.DataManager;
import com.tokopedia.logisticaddaddress.data.DataManagerImpl;
import com.tokopedia.logisticaddaddress.data.AddAddressRetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.data.AddressRepository;
import com.tokopedia.logisticdata.data.apiservice.AddressApi;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fajar Ulin Nuha on 11/10/18.
 */
@AddressScope
@Module
public class AddressModule {

    @Provides
    @AddressScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    @Provides
    @AddressScope
    TkpdOkHttpBuilder provideOkHttpBuilder(@ApplicationContext Context context) {
        return new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
    }

    @Provides
    @AddressScope
    NetworkRouter provideNetworkRouter(@ApplicationContext Context appContext) {
        return (NetworkRouter) appContext;
    }

    @Provides
    @AddressScope
    TkpdAuthInterceptor provideAuthInterceptor(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @AddressScope
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter, UserSession userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @Provides
    @AddressScope
    StringResponseConverter provideResponseConverter() {
        return new StringResponseConverter();
    }

    @Provides
    @AddressScope
    GsonBuilder provideGsonBuilder() {
        return new GsonBuilder();
    }

    @Provides
    @AddressScope
    @AddressRetrofit
    Retrofit provideAddressRetrofit(TkpdOkHttpBuilder tkpdOkHttpBuilder,
                                    TkpdAuthInterceptor tkpdAuthInterceptor,
                                    FingerprintInterceptor fingerprintInterceptor,
                                    StringResponseConverter stringResponseConverter,
                                    GsonBuilder gsonBuilder
    ) {
        return CommonNetwork.createRetrofit(
                TkpdBaseURL.Etc.URL_ADDRESS,
                tkpdOkHttpBuilder,
                tkpdAuthInterceptor,
                fingerprintInterceptor,
                stringResponseConverter,
                gsonBuilder
        );
    }

    @Provides
    @AddressScope
    @PeopleActRetrofit
    Retrofit providePeopleActRetrofit(TkpdOkHttpBuilder tkpdOkHttpBuilder,
                                      TkpdAuthInterceptor tkpdAuthInterceptor,
                                      FingerprintInterceptor fingerprintInterceptor,
                                      StringResponseConverter stringResponseConverter
    ) {
        tkpdOkHttpBuilder.addInterceptor(tkpdAuthInterceptor);
        tkpdOkHttpBuilder.addInterceptor(fingerprintInterceptor);
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build();
    }

    @Provides
    @AddressScope
    AddressApi provideAddressApi(@AddressRetrofit Retrofit retrofit) {
        return retrofit.create(AddressApi.class);
    }

    @Provides
    @AddressScope
    PeopleActApi providePeopleActApi(@PeopleActRetrofit Retrofit retrofit) {
        return retrofit.create(PeopleActApi.class);
    }

    @Provides
    @AddressScope
    AddAddressPresenter provideAddAddressPresenter(AddAddressPresenterImpl addAddressPresenter) {
        return addAddressPresenter;
    }

    @Provides
    @AddressScope
    AddressRepository provideAddressRepo(AddAddressRetrofitInteractorImpl addAddressRetrofitInteractor) {
        return addAddressRetrofitInteractor;
    }

    @Provides
    @AddressScope
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @AddressScope
    DataManager provideDataManager(DataManagerImpl dataManager) {
        return dataManager;
    }

}
