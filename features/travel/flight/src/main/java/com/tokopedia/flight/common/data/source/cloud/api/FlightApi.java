package com.tokopedia.flight.common.data.source.cloud.api;

import com.google.gson.JsonObject;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.model.request.DataRequest;
import com.tokopedia.flight.search.data.api.combined.request.FlightSearchCombinedRequestData;
import com.tokopedia.flight.search.data.api.combined.response.FlightSearchCombinedResponse;
import com.tokopedia.flight.search.data.api.single.request.FlightSearchSingleRequestData;
import com.tokopedia.flight.search.data.api.single.response.FlightDataResponse;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightApi {

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_SEARCH_SINGLE)
    Observable<Response<String>> searchFlightSingle(@Body DataRequest<FlightSearchSingleRequestData> flightSearchRequest);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_SEARCH_COMBINED)
    Observable<Response<FlightDataResponse<List<FlightSearchCombinedResponse>>>> searchFlightCombined(
            @Body DataRequest<FlightSearchCombinedRequestData> flightSearchCombinedRequestDataDataRequest);

    @GET(FlightUrl.FLIGHT_CANCELLATION_PASSENGER)
    Observable<Response<String>> getCancellablePassenger(@Query("invoice_id") String invoiceId);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CANCELLATION_ESTIMATE_REFUND)
    Observable<Response<DataResponse<EstimateRefundResultEntity>>> getEstimateRefund(@Body DataRequest<FlightEstimateRefundRequest> flightEstimateRefundRequestDataRequest);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CANCELLATION_REQUEST)
    Observable<Response<DataResponse<CancellationRequestEntity>>> requestCancellation(@Body JsonObject cancellationRequest);

}
