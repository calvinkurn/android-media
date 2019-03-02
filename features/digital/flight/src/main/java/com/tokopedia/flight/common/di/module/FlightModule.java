package com.tokopedia.flight.common.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListBackgroundSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;
import com.tokopedia.flight.banner.data.source.BannerDataSource;
import com.tokopedia.flight.booking.data.cloud.FlightCartDataSource;
import com.tokopedia.flight.cancellation.data.cloud.FlightCancellationCloudDataSource;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.model.FlightErrorResponse;
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl;
import com.tokopedia.flight.common.data.source.FlightAuthInterceptor;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.data.source.cloud.api.retrofit.StringResponseConverter;
import com.tokopedia.flight.common.di.qualifier.FlightChuckQualifier;
import com.tokopedia.flight.common.di.qualifier.FlightGsonPlainQualifier;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderMapper;
import com.tokopedia.flight.passenger.data.FlightPassengerFactorySource;
import com.tokopedia.flight.review.data.FlightBookingDataSource;
import com.tokopedia.flight.review.data.FlightCancelVoucherDataSource;
import com.tokopedia.flight.review.data.FlightCheckVoucheCodeDataSource;
import com.tokopedia.flight.search.data.db.FlightComboDao;
import com.tokopedia.flight.search.data.db.FlightJourneyDao;
import com.tokopedia.flight.search.data.db.FlightRouteDao;
import com.tokopedia.flight.search.data.db.FlightSearchRoomDb;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 10/24/2017.
 */

@FlightScope
@Module
public class FlightModule {
    private static final int NET_READ_TIMEOUT = 30;
    private static final int NET_WRITE_TIMEOUT = 30;
    private static final int NET_CONNECT_TIMEOUT = 30;
    private static final int NET_RETRY = 1;
    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    @FlightScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

    @FlightScope
    @Provides
    public FlightModuleRouter provideFlightModuleRouter(@ApplicationContext Context context) {
        if (context instanceof FlightModuleRouter) {
            return ((FlightModuleRouter) context);
        }
        throw new RuntimeException("App should implement " + FlightModuleRouter.class.getSimpleName());
    }

    @FlightScope
    @Provides
    @FlightChuckQualifier
    public Interceptor provideChuckInterceptory(FlightModuleRouter flightModuleRouter) {
        return flightModuleRouter.getChuckInterceptor();
    }

    @FlightScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            FlightAuthInterceptor flightAuthInterceptor,
                                            @FlightChuckQualifier Interceptor chuckIntereptor,
                                            @FlightQualifier OkHttpRetryPolicy okHttpRetryPolicy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(flightAuthInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(FlightErrorResponse.class));
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckIntereptor);
        }
        return builder.build();
    }

    @FlightScope
    @Provides
    @FlightQualifier
    public Retrofit provideFlightRetrofit(OkHttpClient okHttpClient,
                                          @FlightQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FlightUrl.BASE_URL).client(okHttpClient).build();
    }

    @FlightScope
    @Provides
    public FlightRepository provideFlightRepository(BannerDataSource bannerDataSource,
                                                    FlightAirportDataListSource flightAirportDataListSource,
                                                    FlightAirlineDataListSource flightAirlineDataListSource,
                                                    FlightClassesDataSource getFlightClassesUseCase,
                                                    FlightCartDataSource flightCartDataSource,
                                                    FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource,
                                                    FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource,
                                                    FlightBookingDataSource flightBookingDataSource,
                                                    FlightAirportVersionDBSource flightAirportVersionDBSource,
                                                    FlightOrderDataSource flightOrderDataSource,
                                                    FlightOrderMapper flightOrderMapper,
                                                    FlightPassengerFactorySource flightPassengerFactorySource,
                                                    FlightCancellationCloudDataSource flightCancellationCloudDataSource,
                                                    FlightCancelVoucherDataSource flightCancelVoucherDataSource) {
        return new FlightRepositoryImpl(bannerDataSource, flightAirportDataListSource, flightAirlineDataListSource,
                getFlightClassesUseCase, flightCartDataSource, flightAirportDataListBackgroundSource,
                flightCheckVoucheCodeDataSource, flightBookingDataSource, flightAirportVersionDBSource,
                flightOrderDataSource, flightOrderMapper, flightPassengerFactorySource, flightCancellationCloudDataSource,
                flightCancelVoucherDataSource);
    }

    @Provides
    @FlightGsonPlainQualifier
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @FlightScope
    @Provides
    public FlightApi provideFlightAirportApi(@FlightQualifier Retrofit retrofit) {
        return retrofit.create(FlightApi.class);
    }

    @FlightScope
    @FlightQualifier
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY);
    }

    @FlightScope
    @Provides
    @FlightQualifier
    public Retrofit.Builder provideRetrofitBuilder(@FlightQualifier Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    @FlightScope
    @FlightQualifier
    @Provides
    public Gson provideFlightGson() {
        return new GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Provides
    @FlightScope
    FlightSearchRoomDb provideFlightSearchRoomDb(@ApplicationContext Context context) {
        return FlightSearchRoomDb.getInstance(context);
    }

    @Provides
    @FlightScope
    FlightComboDao provideComboDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightComboDao();
    }

    @Provides
    @FlightScope
    FlightJourneyDao provideFlightJourneyDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightJourneyDao();
    }

    @Provides
    @FlightScope
    FlightRouteDao provideRouteDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightRouteDao();
    }

    @FlightScope
    @Provides
    public FlightDateUtil provideFlightDateUtil(){
        return new FlightDateUtil();
    }

    @FlightScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

}