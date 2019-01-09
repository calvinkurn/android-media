package com.tokopedia.travelcalendar.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.travelcalendar.data.TravelCalendarRepository;
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase;
import com.tokopedia.travelcalendar.domain.TravelCalendarRouter;
import com.tokopedia.travelcalendar.domain.TravelCalendarProvider;
import com.tokopedia.travelcalendar.domain.TravelCalendarScheduler;
import com.tokopedia.travelcalendar.network.TravelCalendarApi;
import com.tokopedia.travelcalendar.network.TravelCalendarAuthInterceptor;
import com.tokopedia.travelcalendar.network.TravelCalendarUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
@Module
public class TravelCalendarModule {

    @TravelCalendarScope
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                     TravelCalendarAuthInterceptor travelCalendarAuthInterceptor,
                                     TravelCalendarRouter travelCalendarRouter) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(travelCalendarAuthInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(travelCalendarRouter.getChuckInterceptor());
        }
        return builder.build();
    }

    @TravelCalendarScope
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(TravelCalendarUrl.GQL_BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    TravelCalendarApi provideTokoCashApi(Retrofit retrofit) {
        return retrofit.create(TravelCalendarApi.class);
    }

    @Provides
    GetHolidayUseCase provideGetHolidayUseCase(TravelCalendarRepository travelCalendarRepository) {
        return new GetHolidayUseCase(travelCalendarRepository);
    }

    @Provides
    TravelCalendarProvider provideTravelCalendarProvider() {
        return new TravelCalendarScheduler();
    }

    @Provides
    public TravelCalendarRouter provideTravelCalendarRouter(@ApplicationContext Context context) {
        if (context instanceof TravelCalendarRouter) {
            return ((TravelCalendarRouter) context);
        }
        throw new RuntimeException("Application must implement " + TravelCalendarRouter.class.getCanonicalName());
    }

}
