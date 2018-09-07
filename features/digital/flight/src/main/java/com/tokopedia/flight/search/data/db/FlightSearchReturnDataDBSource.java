package com.tokopedia.flight.search.data.db;

import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightSearchReturnDataDBSource extends AbsFlightSearchDataDBSource {

    @Inject
    public FlightSearchReturnDataDBSource() {
    }

    @Override
    protected Class<? extends FlightSearchSingleRouteDB> getDBClass() {
        return FlightSearchReturnRouteDB.class;
    }

    @Override
    protected void insertSingleFlightData(FlightSearchData flightSearchData) {
        FlightSearchReturnRouteDB flightSearchReturnRouteDB = new FlightSearchReturnRouteDB(flightSearchData);
        flightSearchReturnRouteDB.insert();
    }
}
