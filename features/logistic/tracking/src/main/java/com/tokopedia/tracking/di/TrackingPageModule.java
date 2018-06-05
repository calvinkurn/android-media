package com.tokopedia.tracking.di;

import android.content.Context;

import com.google.gson.Gson;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.logisticdata.data.apiservice.TrackingOrderApi;
import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;
import com.tokopedia.tracking.mapper.ITrackingPageMapper;
import com.tokopedia.tracking.mapper.TrackingPageMapper;
import com.tokopedia.tracking.presenter.ITrackingPagePresenter;
import com.tokopedia.tracking.presenter.TrackingPagePresenter;
import com.tokopedia.tracking.repository.ITrackingPageRepository;
import com.tokopedia.tracking.repository.TrackingPageRepository;
import com.tokopedia.tracking.usecase.TrackCourierUseCase;
import com.tokopedia.tracking.utils.DateUtil;
import com.tokopedia.tracking.view.ITrackingPageFragment;

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
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();

    }

    @Provides
    @TrackingPageScope
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context, OkHttpRetryPolicy okHttpRetryPolicy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(new TkpdAuthInterceptor())
                .addInterceptor(new FingerprintInterceptor());
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(new HttpLoggingInterceptor())
                    .addInterceptor(new ChuckInterceptor(context));
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
    ITrackingPageRepository provideTrackingPageRepository(TrackingOrderApi api, ITrackingPageMapper mapper) {
        return new TrackingPageRepository(api, mapper);
    }

    @Provides
    @TrackingPageScope
    TrackCourierUseCase provideTrackCourierUseCase(ITrackingPageRepository repository) {
        return new TrackCourierUseCase(repository);
    }

    @Provides
    @TrackingPageScope
    ITrackingPagePresenter provideTrackingPagePresenter(TrackCourierUseCase useCase,
                                                        UserSession userSession) {
        return new TrackingPagePresenter(useCase, userSession, view);
    }

    @Provides
    @TrackingPageScope
    DateUtil provideDateUtil(){
        return new DateUtil();
    }

}
