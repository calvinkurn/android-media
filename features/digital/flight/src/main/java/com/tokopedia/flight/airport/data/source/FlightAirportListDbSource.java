package com.tokopedia.flight.airport.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.database.FlightAirportCountryDao;
import com.tokopedia.flight.airport.data.source.database.FlightAirportCountryTable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
public class FlightAirportListDbSource {

    public static final String FLIGHT_SEARCH_AIRPORT_JSON = "flight_search_airport.json";

    private FlightAirportCountryDao flightAirportCountryDao;
    private FlightAirportMapper flightAirportMapper;
    private Context context;

    @Inject
    public FlightAirportListDbSource(FlightAirportCountryDao flightAirportCountryDao,
                                     FlightAirportMapper flightAirportMapper,
                                     @ApplicationContext Context context) {
        this.flightAirportCountryDao = flightAirportCountryDao;
        this.flightAirportMapper = flightAirportMapper;
        this.context = context;
    }

    public List<FlightAirportCountry> getData() {
        Gson g = new Gson();
        Type dataResponseType = new TypeToken<DataResponse<List<FlightAirportCountry>>>() {
        }.getType();
        DataResponse<List<FlightAirportCountry>> dataResponse = g.fromJson(loadJSONFromAsset(), dataResponseType);
        return dataResponse.getData();
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open(FLIGHT_SEARCH_AIRPORT_JSON);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public Observable<List<FlightAirportCountryTable>> getDataFromJson() {
        return Observable.just(true)
                .map(it -> flightAirportCountryDao.findAllPhoneCodes())
                .flatMap(it -> {
                    if (it.isEmpty()) {
                        return Observable.just(getData())
                                .map(flightCountryData -> flightAirportMapper.mapEntitiesToTables(flightCountryData))
                                .map(airportCountryTables -> flightAirportCountryDao.insertAll(airportCountryTables))
                                .map(result -> flightAirportCountryDao.findAllPhoneCodes());
                    } else {
                        return Observable.just(flightAirportCountryDao.findAllPhoneCodes());
                    }
                });
    }

    public Observable<List<FlightAirportCountryTable>> getPhoneCodeList(String keyword) {
        String query = "%" + keyword + "%";
        if (query.equals("")) {
            return getDataFromJson();
        } else {
            return getDataFromJson()
                    .map(it -> flightAirportCountryDao.getPhoneCodeByKeyword(query));
        }
    }

    public Observable<FlightAirportCountryTable> getAirportByCountryId(String id) {
        String query = "%" + id + "%";
        return getDataFromJson()
                .map(it -> flightAirportCountryDao.getCountryIdByKeyword(query));
    }
}