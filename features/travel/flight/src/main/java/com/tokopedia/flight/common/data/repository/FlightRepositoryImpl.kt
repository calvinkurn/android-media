package com.tokopedia.flight.common.data.repository

import com.tokopedia.flight.cancellation.data.CancellationAttachmentUploadEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationCloudDataSource
import com.tokopedia.flight.common.domain.FlightRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Furqan on 06/10/2021.
 */
class FlightRepositoryImpl(private val flightCancellationCloudDataSource: FlightCancellationCloudDataSource) :
    FlightRepository {

    override suspend fun uploadCancellationAttachmentCoroutine(
        params: Map<String, RequestBody>,
        file: MultipartBody.Part
    ): CancellationAttachmentUploadEntity =
        flightCancellationCloudDataSource.uploadCancellationAttachmentCoroutine(params, file)

}