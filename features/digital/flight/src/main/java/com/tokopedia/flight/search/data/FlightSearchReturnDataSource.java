package com.tokopedia.flight.search.data;

import com.tokopedia.flight.search.data.cache.FlightSearchReturnDataCacheSource;
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource;
import com.tokopedia.flight.search.data.db.FlightSearchReturnDataDBSource;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchReturnDataSource extends AbsFlightSearchDataSource {

    @Inject
    public FlightSearchReturnDataSource(FlightSearchReturnDataCacheSource dataListCacheManager,
                                        FlightSearchReturnDataDBSource flightSearchReturnDataListDBSource,
                                        FlightSearchDataCloudSource dataListCloudManager) {
        super(dataListCacheManager, flightSearchReturnDataListDBSource, dataListCloudManager);
    }
}
