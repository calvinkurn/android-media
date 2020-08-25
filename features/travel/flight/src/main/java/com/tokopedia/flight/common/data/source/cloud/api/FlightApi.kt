package com.tokopedia.flight.common.data.source.cloud.api

import com.google.gson.JsonObject
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationAttachmentUploadEntity
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest
import com.tokopedia.flight.common.constant.FlightUrl
import com.tokopedia.flight.common.data.model.request.DataRequest
import com.tokopedia.network.data.model.response.DataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * Created by alvarisi on 10/30/17.
 */
interface FlightApi {

    @GET(FlightUrl.FLIGHT_CANCELLATION_PASSENGER)
    fun getCancellablePassenger(@Query("invoice_id") invoiceId: String): Observable<Response<String>>

    @Headers("Content-Type: application/json")
    @POST(FlightUrl.FLIGHT_CANCELLATION_ESTIMATE_REFUND)
    fun getEstimateRefund(@Body flightEstimateRefundRequestDataRequest: DataRequest<FlightEstimateRefundRequest>): Observable<Response<DataResponse<EstimateRefundResultEntity>>>

    @Headers("Content-Type: application/json")
    @POST(FlightUrl.FLIGHT_CANCELLATION_REQUEST)
    fun requestCancellation(@Body cancellationRequest: JsonObject): Observable<Response<DataResponse<CancellationRequestEntity>>>

    @Multipart
    @POST(FlightUrl.FLIGHT_CANCELLATION_UPLOAD)
    fun uploadCancellationAttachment(@PartMap params: Map<String, RequestBody>,
                                     @Part docFile: MultipartBody.Part): Observable<Response<DataResponse<CancellationAttachmentUploadEntity>>>

    @Multipart
    @POST(FlightUrl.FLIGHT_CANCELLATION_UPLOAD)
    suspend fun uploadCancellationAttachmentCoroutine(@PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
                                                      @Part docFile: MultipartBody.Part)
            : Response<DataResponse<CancellationAttachmentUploadEntity>>

}