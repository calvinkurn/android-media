package com.tokopedia.flight.search.data.db;

import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.searchV2.data.db.mapper.FlightSearchDataMapper;
import com.tokopedia.flight_dbflow.FlightSearchReturnRouteDB;
import com.tokopedia.flight_dbflow.FlightSearchSingleRouteDB;

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
        FlightSearchDataMapper flightSearchDataMapper = new FlightSearchDataMapper();
        FlightSearchReturnRouteDB flightSearchReturnRouteDB = flightSearchDataMapper.mapToFlightSearchReturnRouteDB(flightSearchData);
        flightSearchReturnRouteDB.insert();
    }
}
