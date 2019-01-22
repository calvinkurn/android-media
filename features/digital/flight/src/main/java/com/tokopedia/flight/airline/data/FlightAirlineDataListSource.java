package com.tokopedia.flight.airline.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.airline.data.cache.FlightAirlineDataCacheSource;
import com.tokopedia.flight.airline.data.cloud.FlightAirlineDataListCloudSource;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource;
import com.tokopedia.flight.airline.util.FlightAirlineParamUtil;
import com.tokopedia.flight_dbflow.FlightAirlineDB;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListSource extends DataListSource<AirlineData, FlightAirlineDB> {
    private static final String DEFAULT_EMPTY_VALUE = "";
    private FlightAirlineDataListDBSource flightAirlineDataListDBSource;
    private FlightAirlineDataListCloudSource flightAirlineDataListCloudSource;

    @Inject
    public FlightAirlineDataListSource(FlightAirlineDataCacheSource flightAirlineDataListCacheSource,
                                       FlightAirlineDataListDBSource flightAirlineDataListDBSource,
                                       FlightAirlineDataListCloudSource flightAirlineDataListCloudSource) {
        super(flightAirlineDataListCacheSource, flightAirlineDataListDBSource, flightAirlineDataListCloudSource);
        this.flightAirlineDataListDBSource = flightAirlineDataListDBSource;
        this.flightAirlineDataListCloudSource = flightAirlineDataListCloudSource;
    }

    public static HashMap<String, Object> generateGetParam(String idToSearch) {
        return FlightAirlineParamUtil.generateMap(idToSearch);
    }

    public Observable<List<FlightAirlineDB>> getAirlineList(final String idToSearch) {
        final HashMap<String, Object> map = generateGetParam(idToSearch);
        return getDataList(map);
    }

    public Observable<FlightAirlineDB> getAirline(final String airlineId) {
        return flightAirlineDataListDBSource.isDataAvailable().flatMap((Func1<Boolean, Observable<FlightAirlineDB>>)
                aBoolean -> {
                    if (aBoolean) {
                        return flightAirlineDataListDBSource.getAirline(airlineId)
                                .flatMap((Func1<FlightAirlineDB, Observable<FlightAirlineDB>>)
                                        flightAirlineDB -> {
                                            if (flightAirlineDB == null)
                                                return getFlightAirlineFromCloud(airlineId);
                                            else
                                                return Observable.just(flightAirlineDB);
                                        });
                    } else {
                        return getFlightAirlineFromCloud(airlineId);
                    }
                });
    }

    private Observable<FlightAirlineDB> getFlightAirlineFromCloud(final String airlineId) {
        return flightAirlineDataListCloudSource.getAirline(airlineId)
                .flatMap((Func1<AirlineData, Observable<Boolean>>) airlineData ->
                        flightAirlineDataListDBSource.insert(airlineData)).flatMap(new Func1<Boolean, Observable<FlightAirlineDB>>() {
                    @Override
                    public Observable<FlightAirlineDB> call(Boolean aBoolean) {
                        return flightAirlineDataListDBSource.getAirline(airlineId);
                    }
                }).onErrorReturn(throwable -> new FlightAirlineDB(
                        airlineId,
                        DEFAULT_EMPTY_VALUE,
                        DEFAULT_EMPTY_VALUE,
                        DEFAULT_EMPTY_VALUE,
                        0,
                        1));
    }

}