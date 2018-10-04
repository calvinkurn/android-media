package com.tokopedia.flight.searchV2.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.searchV2.data.db.FlightSearchRoomDb;
import com.tokopedia.flight.searchV2.data.db.FlightComboDao;
import com.tokopedia.flight.searchV2.data.db.FlightJourneyDao;
import com.tokopedia.flight.searchV2.data.db.FlightRouteDao;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rizky on 01/10/18.
 */
@Module
public class FlightSearchV2Module {

    public FlightSearchV2Module() {
    }

    @Provides
    @FlightSearchV2Scope
    FlightSearchRoomDb provideFlightSearchRoomDb(@ApplicationContext Context context) {
        return FlightSearchRoomDb.getInstance(context);
    }

    @Provides
    @FlightSearchV2Scope
    FlightComboDao provideComboDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightComboDao();
    }

    @Provides
    @FlightSearchV2Scope
    FlightJourneyDao provideJourneyDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightJourneyDao();
    }

    @Provides
    @FlightSearchV2Scope
    FlightRouteDao provideRouteDao(FlightSearchRoomDb flightSearchRoomDb) {
        return flightSearchRoomDb.flightRouteDao();
    }

}
