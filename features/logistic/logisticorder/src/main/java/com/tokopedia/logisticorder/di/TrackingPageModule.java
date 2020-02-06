package com.tokopedia.logisticorder.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckerInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.logisticdata.data.apiservice.TrackingOrderApi;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;
import com.tokopedia.logisticdata.data.repository.ITrackingPageRepository;
import com.tokopedia.logisticdata.data.repository.TrackingPageRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.logisticorder.mapper.ITrackingPageMapper;
import com.tokopedia.logisticorder.mapper.TrackingPageMapper;
import com.tokopedia.logisticorder.presenter.ITrackingPagePresenter;
import com.tokopedia.logisticorder.presenter.TrackingPagePresenter;
import com.tokopedia.logisticorder.usecase.GetRetryAvailability;
import com.tokopedia.logisticorder.usecase.RetryPickup;
import com.tokopedia.logisticorder.usecase.TrackCourierUseCase;
import com.tokopedia.logisticorder.usecase.executor.MainSchedulerProvider;
import com.tokopedia.logisticorder.usecase.executor.SchedulerProvider;
import com.tokopedia.logisticorder.utils.DateUtil;
import com.tokopedia.logisticorder.view.ITrackingPageFragment;
import com.tokopedia.logisticorder.view.OrderAnalyticsOrderTracking;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by kris on 5/9/18. Tokopedia
 */
@Module
public class TrackingPageModule {

    private ITrackingPageFragment view;

    public TrackingPageModule(ITrackingPageFragment view) {
        this.view = view;
    }

    @Provides
    @TrackingPageScope
    OrderAnalyticsOrderTracking provideOrderAnalyticsOrderTracking() {
        return new OrderAnalyticsOrderTracking();
    }

    @Provides
    @TrackingPageScope
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();

    }

    @Provides
    @TrackingPageScope
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);

    }

    @Provides
    @TrackingPageScope
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                     UserSession userSession,
                                     OkHttpRetryPolicy okHttpRetryPolicy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(new TkpdAuthInterceptor(
                        context, ((NetworkRouter) context),
                        userSession))
                .addInterceptor(new FingerprintInterceptor(((NetworkRouter) context),
                        userSession));
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(new HttpLoggingInterceptor())
                    .addInterceptor(new ChuckerInterceptor(context));
        }
        return builder.build();
    }

    @Provides
    @TrackingPageScope
    Retrofit provideCourierTrackingApi(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(LogisticDataConstantUrl.CourierTracking.BASE_URL)
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @TrackingPageScope
    TrackingOrderApi provideTrackOrderApi(Retrofit retrofit) {
        return retrofit.create(TrackingOrderApi.class);
    }

    @Provides
    @TrackingPageScope
    ITrackingPageMapper provideTrackingMapper() {
        return new TrackingPageMapper();
    }

    @Provides
    @TrackingPageScope
    ITrackingPageRepository provideTrackingPageRepository(TrackingOrderApi api) {
        return new TrackingPageRepository(api);
    }

    @Provides
    @TrackingPageScope
    TrackCourierUseCase provideTrackCourierUseCase(ITrackingPageRepository repository, ITrackingPageMapper mapper) {
        return new TrackCourierUseCase(repository, mapper);
    }

    @Provides
    @TrackingPageScope
    ITrackingPagePresenter provideTrackingPagePresenter(TrackCourierUseCase useCase,
                                                        GetRetryAvailability getRetryUsecase,
                                                        RetryPickup retryPickupUsecase,
                                                        UserSession userSession) {
        return new TrackingPagePresenter(useCase, getRetryUsecase, retryPickupUsecase, userSession, view);
    }

    @Provides
    @TrackingPageScope
    DateUtil provideDateUtil() {
        return new DateUtil();
    }

    @Provides
    @TrackingPageScope
    SchedulerProvider provideSchedulerProvider() {
        return new MainSchedulerProvider();
    }

}
