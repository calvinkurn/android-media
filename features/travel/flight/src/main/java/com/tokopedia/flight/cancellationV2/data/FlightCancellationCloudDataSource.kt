package com.tokopedia.flight.cancellationV2.data

import com.tokopedia.flight.common.data.source.cloud.api.FlightApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @author by furqan on 23/03/18.
 */
class FlightCancellationCloudDataSource @Inject constructor(private val flightApi: FlightApi) {

    suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity =
            flightApi.uploadCancellationAttachmentCoroutine(params, file).let {
                it.body()!!.data
            }

}