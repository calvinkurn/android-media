package com.tokopedia.logisticgeolocation.di;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tokopedia.logisticdata.data.apiservice.AddressApi;
import com.tokopedia.logisticdata.data.apiservice.MapsApi;
import com.tokopedia.logisticgeolocation.GoogleMapFragment;
import com.tokopedia.logisticgeolocation.model.IMapsMapper;
import com.tokopedia.logisticgeolocation.model.MapsMapper;
import com.tokopedia.network.CommonNetwork;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
@GeolocationScope
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
                                    StringResponseConverter stringResponseConverter,
                                    GsonBuilder gsonBuilder
    ) {
        return CommonNetwork.createRetrofit(
                TkpdBaseURL.MAPS_DOMAIN,
                tkpdOkHttpBuilder,
                tkpdAuthInterceptor,
                fingerprintInterceptor,
                stringResponseConverter,
                gsonBuilder
        );
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
}
