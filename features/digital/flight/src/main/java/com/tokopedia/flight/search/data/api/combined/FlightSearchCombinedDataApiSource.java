package com.tokopedia.flight.search.data.api.combined;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.api.combined.request.AttributesRequestData;
import com.tokopedia.flight.search.data.api.combined.request.FlightSearchCombinedRequestData;
import com.tokopedia.flight.search.data.api.combined.request.RouteRequestData;
import com.tokopedia.flight.search.data.api.combined.response.FlightSearchCombinedResponse;
import com.tokopedia.flight.search.data.api.single.response.FlightDataResponse;
import com.tokopedia.flight.search.presentation.model.FlightRouteModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchCombinedApiRequestModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Rizky on 19/09/18.
 */
public class FlightSearchCombinedDataApiSource {
    private FlightApi flightApi;

    @Inject
    public FlightSearchCombinedDataApiSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<FlightDataResponse<List<FlightSearchCombinedResponse>>> getData(
            FlightSearchCombinedApiRequestModel flightSearchCombinedApiRequestModel) {
        List<RouteRequestData> routeRequestData = new ArrayList<>();
        for (FlightRouteModel flightRouteModel : flightSearchCombinedApiRequestModel.getRoutes()) {
            routeRequestData.add(new RouteRequestData(
                    flightRouteModel.getDeparture(),
                    flightRouteModel.getArrival(),
                    flightRouteModel.getDate()));
        }

        AttributesRequestData attributesRequestData = new AttributesRequestData(
                routeRequestData, flightSearchCombinedApiRequestModel.get_class(),
                flightSearchCombinedApiRequestModel.getAdult(),
                flightSearchCombinedApiRequestModel.getInfant(),
                flightSearchCombinedApiRequestModel.getChild());
        FlightSearchCombinedRequestData flightSearchCombinedRequestData =
                new FlightSearchCombinedRequestData(attributesRequestData);
        DataRequest<FlightSearchCombinedRequestData> dataRequest = new DataRequest<>(flightSearchCombinedRequestData);

        return flightApi.searchFlightCombined(dataRequest)
                .map(Response::body);
    }
}