package com.tokopedia.logisticaddaddress.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.domain.IMapsMapper;
import com.tokopedia.logisticaddaddress.domain.MapsMapper;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationContract;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationPresenter;
import com.tokopedia.logisticaddaddress.features.pinpoint.GoogleMapFragment;
import com.tokopedia.logisticCommon.data.apiservice.MapsApi;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
@Module
public class GeolocationModule {

    Context context;
    GoogleMapFragment googleMapFragment;

    public GeolocationModule(Context context, GoogleMapFragment googleMapFragment) {
        this.context = context;
        this.googleMapFragment = googleMapFragment;
    }

    @Provides
    @GeolocationScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    @Provides
    @GeolocationScope
    TkpdOkHttpBuilder provideOkHttpBuilder(@ApplicationContext Context context) {
        return new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
    }

    @Provides
    @GeolocationScope
    NetworkRouter provideNetworkRouter(@ApplicationContext Context appContext) {
        return (NetworkRouter) appContext;
    }

    @Provides
    @GeolocationScope
    TkpdAuthInterceptor provideAuthInterceptor(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new TkpdAuthInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @GeolocationScope
    FingerprintInterceptor provideFingerprintInterceptor(NetworkRouter networkRouter, UserSession userSession) {
        return new FingerprintInterceptor(networkRouter, userSession);
    }

    @Provides
    @GeolocationScope
    StringResponseConverter provideResponseConverter() {
        return new StringResponseConverter();
    }

    @Provides
    @GeolocationScope
    GsonBuilder provideGsonBuilder() {
        return new GsonBuilder();
    }

    @Provides
    @GeolocationScope
    Retrofit provideGeolocationRetrofit(TkpdOkHttpBuilder tkpdOkHttpBuilder,
                                        TkpdAuthInterceptor tkpdAuthInterceptor,
                                        FingerprintInterceptor fingerprintInterceptor,
                                        StringResponseConverter stringResponseConverter
    ) {
        tkpdOkHttpBuilder.addInterceptor(tkpdAuthInterceptor);
        tkpdOkHttpBuilder.addInterceptor(fingerprintInterceptor);
        return new Retrofit.Builder()
                .baseUrl(TokopediaUrl.Companion.getInstance().getGW())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build();
    }

    @Provides
    @GeolocationScope
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @GeolocationScope
    MapsApi provideAddressApi(Retrofit retrofit) {
        return retrofit.create(MapsApi.class);
    }

    @Provides
    @GeolocationScope
    IMapsMapper provideMapsMapper(MapsMapper mapsMapper) {
        return mapsMapper;
    }

    @Provides
    @GeolocationScope
    GoogleMapFragment provideGoogleMapFragment() {
        return this.googleMapFragment;
    }

    @Provides
    @GeolocationScope
    @ActivityContext
    Context provideAppContext() {
        return this.context;
    }

    @Provides
    @GeolocationScope
    GeolocationContract.GeolocationPresenter provideGeolocationPresenter(GeolocationPresenter presenter) {
        return presenter;
    }

    @Provides
    @GeolocationScope
    RetrofitInteractor provideRetrofitInteractor(RetrofitInteractorImpl retrofitInteractor) {
        return retrofitInteractor;
    }
}
