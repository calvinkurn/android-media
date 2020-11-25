package com.tokopedia.flight.cancellation.data.cloud

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.tokopedia.flight.cancellation.data.cache.FlightCancellationReasonDataCacheSource
import com.tokopedia.flight.cancellation.data.cloud.entity.*
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest
import com.tokopedia.flight.common.data.model.request.DataRequest
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 23/03/18.
 */
class FlightCancellationCloudDataSource @Inject constructor(private val flightApi: FlightApi,
                                                            private val flightCancellationReasonDataCacheSource: FlightCancellationReasonDataCacheSource,
                                                            private val flightCancellationJsonDeserializer: FlightCancellationJsonDeserializer) {

    private val gsonWithDeserializer: Gson = GsonBuilder().registerTypeAdapter(CancelPassengerEntity::class.java, flightCancellationJsonDeserializer).create()

    fun getCancelablePassenger(invoiceId: String): Observable<Map<String, List<Passenger>>> =
            flightApi.getCancellablePassenger(invoiceId)
                    .map { stringResponse -> gsonWithDeserializer.fromJson(stringResponse.body(), CancelPassengerEntity::class.java) }
                    .flatMap { cancelPassengerEntity ->
                        flightCancellationReasonDataCacheSource.saveCache(
                                cancelPassengerEntity.attributes.reasons)
                        val passengerMap: MutableMap<String, List<Passenger>> = HashMap()
                        passengerMap[CANCELLABLES_KEY] = cancelPassengerEntity.attributes.passengers
                        passengerMap[NON_CANCELLABLES_KEY] = cancelPassengerEntity.attributes.nonCancellablePassengers
                        Observable.just<Map<String, List<Passenger>>>(passengerMap)
                    }

    fun getEstimateRefund(request: FlightEstimateRefundRequest): Observable<EstimateRefundResultEntity> =
            flightApi.getEstimateRefund(DataRequest(request))
                    .map { dataResponseResponse -> dataResponseResponse.body()!!.data }

    fun requestCancellation(request: DataRequest<FlightCancellationRequestBody>): Observable<CancellationRequestEntity> =
            flightApi.requestCancellation(gsonWithDeserializer
                    .fromJson(gsonWithDeserializer.toJson(request), JsonElement::class.java).asJsonObject)
                    .flatMap { dataResponseResponse -> Observable.just(dataResponseResponse.body()!!.data) }

    fun uploadCancellationAttachment(params: Map<String, RequestBody>, file: MultipartBody.Part): Observable<CancellationAttachmentUploadEntity> =
            flightApi.uploadCancellationAttachment(params, file)
                    .flatMap { dataResponseResponse -> Observable.just(dataResponseResponse.body()!!.data) }

    suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity =
            flightApi.uploadCancellationAttachmentCoroutine(params, file).let {
                it.body()!!.data
            }

    val cancellationReasons: Observable<List<Reason>>
        get() = flightCancellationReasonDataCacheSource.cache

    companion object {
        private const val CANCELLABLES_KEY = "cancellablePassengers"
        private const val NON_CANCELLABLES_KEY = "nonCancellablePassengers"
    }

}