package com.tokopedia.flight.searchV2.data.api.combined;

import com.tokopedia.abstraction.base.data.source.cloud.DataCloudSource;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.searchV2.data.api.combined.request.AttributesRequestData;
import com.tokopedia.flight.searchV2.data.api.combined.request.FlightSearchCombinedRequestData;
import com.tokopedia.flight.searchV2.data.api.combined.response.FlightSearchCombinedResponse;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Rizky on 19/09/18.
 */
public class FlightSearchCombinedDataApiSource extends DataCloudSource<FlightDataResponse<List<FlightSearchCombinedResponse>>> {

    private FlightApi flightApi;

    @Inject
    public FlightSearchCombinedDataApiSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    @Override
    public Observable<FlightDataResponse<List<FlightSearchCombinedResponse>>> getData(HashMap<String, Object> params) {
        FlightSearchCombinedRequestData flightSearchCombinedRequestData =
                new FlightSearchCombinedRequestData(new AttributesRequestData(null));
        DataRequest<FlightSearchCombinedRequestData> dataRequest = new DataRequest<>(flightSearchCombinedRequestData);

        return flightApi.searchFlightCombined(dataRequest)
                .map(Response::body);
    }

}