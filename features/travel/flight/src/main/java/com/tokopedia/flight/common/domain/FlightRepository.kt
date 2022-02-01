package com.tokopedia.flight.common.domain

import com.tokopedia.flight.cancellation.data.CancellationAttachmentUploadEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Furqan on 06/10/2021.
 */
interface FlightRepository {
    suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity
}