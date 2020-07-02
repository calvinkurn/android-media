package com.tokopedia.flight.common.data.source.cloud.api;

import com.google.gson.JsonObject;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationAttachmentUploadEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.model.request.DataRequest;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightApi {

    @GET(FlightUrl.FLIGHT_CANCELLATION_PASSENGER)
    Observable<Response<String>> getCancellablePassenger(@Query("invoice_id") String invoiceId);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CANCELLATION_ESTIMATE_REFUND)
    Observable<Response<DataResponse<EstimateRefundResultEntity>>> getEstimateRefund(@Body DataRequest<FlightEstimateRefundRequest> flightEstimateRefundRequestDataRequest);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CANCELLATION_REQUEST)
    Observable<Response<DataResponse<CancellationRequestEntity>>> requestCancellation(@Body JsonObject cancellationRequest);

    @Multipart
    @POST(FlightUrl.FLIGHT_CANCELLATION_UPLOAD)
    Observable<Response<DataResponse<CancellationAttachmentUploadEntity>>> uploadCancellationAttachment(@PartMap Map<String, RequestBody> params,
                                                                                                        @Part MultipartBody.Part docFile);
}
