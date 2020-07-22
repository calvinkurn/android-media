package com.tokopedia.flight.common.domain

import com.tokopedia.flight.cancellation.data.cloud.entity.*
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity
import com.tokopedia.flight.orderlist.domain.FlightOrderRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable

/**
 * Created by zulfikarrahman on 10/25/17.
 */
interface FlightRepository : FlightOrderRepository {
    fun getOrderEntity(id: String): Observable<OrderEntity>
    fun getCancelablePassenger(invoiceId: String): Observable<Map<String, List<Passenger>>>
    fun getCancellationReasons(): Observable<List<Reason>>
    fun estimateRefund(estimateRefundRequest: FlightEstimateRefundRequest): Observable<EstimateRefundResultEntity>
    fun cancellationRequest(request: FlightCancellationRequestBody): Observable<CancellationRequestEntity>
    fun uploadCancellationAttachment(params: Map<String, RequestBody>, file: MultipartBody.Part): Observable<CancellationAttachmentUploadEntity>

    suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity
}