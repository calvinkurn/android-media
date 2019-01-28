package com.tokopedia.common.travel.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.travel.database.TravelPassengerDao;
import com.tokopedia.common.travel.database.TravelPassengerRoomDb;
import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.domain.provider.TravelScheduler;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@Module
public class CommonTravelModule {

    @CommonTravelScope
    @Provides
    TravelPassengerRoomDb provideTravelPassengerRoomDb(@ApplicationContext Context context) {
        return TravelPassengerRoomDb.getDatabase(context);
    }

    @CommonTravelScope
    @Provides
    TravelPassengerDao provideTravelPassengerDao(TravelPassengerRoomDb travelPassengerRoomDb) {
        return travelPassengerRoomDb.travelPassengerDao();
    }

    @Provides
    TravelProvider provideTravelProvider() {
        return new TravelScheduler();
    }
}
