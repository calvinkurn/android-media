package com.tokopedia.mediauploader.data

import com.tokopedia.mediauploader.data.entity.Uploader
import retrofit2.http.POST
import retrofit2.http.Path

interface UploaderServices {

    @POST("$UPLOAD_URL/{file_type}/{source_id}")
    suspend fun uploadFile(
            /*
            * file_type:
            * image | file | video
            * */
            @Path("file_type") fileType: String,
            /*
            * source_id:
            * tribe source id based on request
            * */
            @Path("source_id") sourceId: String
    ) : Uploader

    companion object {
        private const val API_VERSION = "v1"
        private const val UPLOAD_PATH = "upload"

        const val UPLOAD_URL = "/$API_VERSION/$UPLOAD_PATH"
    }

}