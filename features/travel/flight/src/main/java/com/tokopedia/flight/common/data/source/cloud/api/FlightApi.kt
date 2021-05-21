package com.tokopedia.flight.common.data.source.cloud.api

import com.tokopedia.flight.cancellation.data.CancellationAttachmentUploadEntity
import com.tokopedia.flight.common.constant.FlightUrl
import com.tokopedia.network.data.model.response.DataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

/**
 * Created by alvarisi on 10/30/17.
 */
interface FlightApi {

    @Multipart
    @POST(FlightUrl.FLIGHT_CANCELLATION_UPLOAD)
    suspend fun uploadCancellationAttachmentCoroutine(@PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
                                                      @Part docFile: MultipartBody.Part)
            : Response<DataResponse<CancellationAttachmentUploadEntity>>

}