package com.tokopedia.mediauploader.data

import com.tokopedia.mediauploader.data.entity.Uploader
import okhttp3.MultipartBody
import retrofit2.http.*

interface UploaderServices {

    @POST
    @Multipart
    suspend fun uploadFile(
            /*
            * get url from data policy
            * */
            @Url urlToUpload: String,

            /*
            * file_upload:
            * media blob (a file) to upload
            * */
            @Part fileUpload: MultipartBody.Part
    ) : Uploader

    companion object {
        private const val API_VERSION = "v1"
        private const val UPLOAD_PATH = "upload"

        const val UPLOAD_URL = "/$API_VERSION/$UPLOAD_PATH/{file_type}/{source_id}"
    }

}