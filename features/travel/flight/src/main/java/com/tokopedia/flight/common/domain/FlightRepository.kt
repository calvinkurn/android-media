package com.tokopedia.flight.common.domain

import com.tokopedia.flight.cancellation.data.CancellationAttachmentUploadEntity
import com.tokopedia.flight.orderlist.domain.FlightOrderRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Furqan on 06/10/2021.
 */
interface FlightRepository : FlightOrderRepository {
    suspend fun uploadCancellationAttachmentCoroutine(params: Map<String, RequestBody>, file: MultipartBody.Part): CancellationAttachmentUploadEntity
}