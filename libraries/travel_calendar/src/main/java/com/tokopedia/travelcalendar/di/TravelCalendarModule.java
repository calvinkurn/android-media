package com.tokopedia.travelcalendar.di;

import com.tokopedia.travelcalendar.data.TravelCalendarRepository;
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase;
import com.tokopedia.travelcalendar.network.TravelCalendarApi;
import com.tokopedia.travelcalendar.network.TravelCalendarAuthInterceptor;
import com.tokopedia.travelcalendar.network.TravelCalendarUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
@Module
public class TravelCalendarModule {

    @TravelCalendarScope
    @Provides
    OkHttpClient provideOkHttpClient(TravelCalendarAuthInterceptor travelCalendarAuthInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(travelCalendarAuthInterceptor);
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
}
