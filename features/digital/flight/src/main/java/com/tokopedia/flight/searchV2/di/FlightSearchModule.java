package com.tokopedia.flight.searchV2.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.searchV2.data.db.FlightComboDao;
import com.tokopedia.flight.searchV2.data.db.FlightJourneyComboJoinDao;
import com.tokopedia.flight.searchV2.data.db.FlightJourneyDao;
import com.tokopedia.flight.searchV2.data.db.FlightRouteDao;
import com.tokopedia.flight.searchV2.data.db.FlightSearchRoomDb;

import dagger.Module;
import dagger.Provides;

/**
 * @author by furqan on 01/10/18.
 */

@FlightSearchScope
@Module
public class FlightSearchModule {

    public FlightSearchModule() {
    }

    @Provides
    @FlightSearchScope
    FlightSearchRoomDb provideFlightSearchRoomDb(@ApplicationContext Context context) {
        return FlightSearchRoomDb.getInstance(context);
    }

    @Provides
    @FlightSearchScope
    FlightComboDao provideComboDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightComboDao();
    }

    @Provides
    @FlightSearchScope
    FlightJourneyDao provideFlightJourneyDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightJourneyDao();
    }

    @Provides
    @FlightSearchScope
    FlightRouteDao provideRouteDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightRouteDao();
    }

    @Provides
    @FlightSearchScope
    FlightJourneyComboJoinDao provideFlightJourneyComboJoinDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightJourneyComboJoinDao();
    }

}
