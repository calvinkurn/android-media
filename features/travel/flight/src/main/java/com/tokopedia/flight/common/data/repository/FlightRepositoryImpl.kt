package com.tokopedia.flight.common.data.repository

import com.tokopedia.flight.cancellation.data.CancellationAttachmentUploadEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationCloudDataSource
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource
import com.tokopedia.flight.orderlist.domain.FlightOrderRepositoryImpl
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by zulfikarrahman on 10/25/17.
 */
class FlightRepositoryImpl(flightOrderDataSource: FlightOrderDataSource,
                           flightOrderMapper: FlightOrderMapper,
                           private val flightCancellationCloudDataSource: FlightCancellationCloudDataSource)
    : FlightOrderRepositoryImpl(flightOrderDataSource, flightOrderMapper), FlightRepository {

    override suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity =
            flightCancellationCloudDataSource.uploadCancellationAttachmentCoroutine(params, file)

}