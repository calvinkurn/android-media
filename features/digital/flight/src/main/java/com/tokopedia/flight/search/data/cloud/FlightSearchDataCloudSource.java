package com.tokopedia.flight.search.data.cloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.data.source.cloud.DataCloudSource;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.request.Attributes;
import com.tokopedia.flight.search.data.cloud.model.request.FlightSearchSingleRequestData;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.cloud.model.response.Meta;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class FlightSearchDataCloudSource extends DataCloudSource<FlightDataResponse<List<FlightSearchData>>> {

    private FlightApi flightApi;
    private Gson gsonWithDeserializer;

    @Inject
    public FlightSearchDataCloudSource(FlightApi flightApi, FlightSearchJsonDeserializer flightSearchJsonDeserializer) {
        this.flightApi = flightApi;
        Type type = new TypeToken<FlightDataResponse<List<FlightSearchData>>>(){}.getType();
        this.gsonWithDeserializer = new GsonBuilder().registerTypeAdapter(type, flightSearchJsonDeserializer).create();
    }

    @Override
    public Observable<FlightDataResponse<List<FlightSearchData>>> getData(HashMap<String, Object> params) {
        FlightSearchApiRequestModel flightSearchApiRequestModel = FlightSearchParamUtil.getInitialPassData(params);

        FlightSearchSingleRequestData flightSearchSingleRequestData =
                new FlightSearchSingleRequestData(flightSearchApiRequestModel);
        DataRequest<FlightSearchSingleRequestData> dataRequest = new DataRequest<>(flightSearchSingleRequestData);

        return flightApi.searchFlightSingle(dataRequest)
                .map((Func1<Response<String>, FlightDataResponse<List<FlightSearchData>>>)
                        stringResponse -> {
                            Type type = new TypeToken<FlightDataResponse<List<FlightSearchData>>>(){}.getType();
                            return gsonWithDeserializer.fromJson(stringResponse.body(), type);
                        })
                .zipWith(Observable.just(flightSearchSingleRequestData),
                        (flightDataResponseResponse, flightSearchSingleRequestData1) -> {
                            if (flightDataResponseResponse != null) {
                                Meta meta = flightDataResponseResponse.getMeta();
                                Attributes attribute = flightSearchSingleRequestData1.getAttributes();
                                meta.setArrivalAirport(attribute.getArrival());
                                meta.setDepartureAirport(attribute.getDeparture());
                                meta.setTime(attribute.getDate());
                                return flightDataResponseResponse;
                            } else {
                                return null;
                            }
                        });
    }

}