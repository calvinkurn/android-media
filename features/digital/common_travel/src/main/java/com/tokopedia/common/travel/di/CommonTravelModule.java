package com.tokopedia.common.travel.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.travel.database.CommonTravelRoomDb;
import com.tokopedia.common.travel.database.TravelPassengerDao;
import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.domain.provider.TravelScheduler;
import com.tokopedia.flight.country.database.CountryPhoneCodeDao;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@Module
public class CommonTravelModule {

    @CommonTravelScope
    @Provides
    CommonTravelRoomDb provideTravelPassengerRoomDb(@ApplicationContext Context context) {
        return CommonTravelRoomDb.getDatabase(context);
    }

    @CommonTravelScope
    @Provides
    TravelPassengerDao provideTravelPassengerDao(CommonTravelRoomDb commonTravelRoomDb) {
        return commonTravelRoomDb.travelPassengerDao();
    }

    @CommonTravelScope
    @Provides
    CountryPhoneCodeDao provideCountryPhoneCodeDao(CommonTravelRoomDb commonTravelRoomDb) {
        return commonTravelRoomDb.countryPhoneCodeDao();
    }

    @Provides
    TravelProvider provideTravelProvider() {
        return new TravelScheduler();
    }
}
