package com.tokopedia.flight.common.data.repository

import com.tokopedia.flight.cancellation.data.cloud.FlightCancellationCloudDataSource
import com.tokopedia.flight.cancellation.data.cloud.entity.*
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest
import com.tokopedia.flight.common.data.model.request.DataRequest
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity
import com.tokopedia.flight.orderlist.domain.FlightOrderRepositoryImpl
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable

/**
 * Created by zulfikarrahman on 10/25/17.
 */
class FlightRepositoryImpl(flightOrderDataSource: FlightOrderDataSource,
                           flightOrderMapper: FlightOrderMapper,
                           private val flightCancellationCloudDataSource: FlightCancellationCloudDataSource)
    : FlightOrderRepositoryImpl(flightOrderDataSource, flightOrderMapper), FlightRepository {

    override fun getOrderEntity(id: String): Observable<OrderEntity> =
            flightOrderDataSource.getOrder(id)

    override fun getCancelablePassenger(invoiceId: String): Observable<Map<String, List<Passenger>>> =
            flightCancellationCloudDataSource.getCancelablePassenger(invoiceId)

    override fun getCancellationReasons(): Observable<List<Reason>> =
            flightCancellationCloudDataSource.cancellationReasons

    override fun estimateRefund(request: FlightEstimateRefundRequest): Observable<EstimateRefundResultEntity> =
            flightCancellationCloudDataSource.getEstimateRefund(request)

    override fun cancellationRequest(request: FlightCancellationRequestBody): Observable<CancellationRequestEntity> {
        val requestBody = DataRequest(request)
        return flightCancellationCloudDataSource.requestCancellation(requestBody)
    }

    override fun uploadCancellationAttachment(params: Map<String, RequestBody>, file: MultipartBody.Part): Observable<CancellationAttachmentUploadEntity> =
            flightCancellationCloudDataSource.uploadCancellationAttachment(params, file)

    override suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity =
            flightCancellationCloudDataSource.uploadCancellationAttachmentCoroutine(params, file)

}