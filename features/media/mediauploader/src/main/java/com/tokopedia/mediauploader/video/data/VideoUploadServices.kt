package com.tokopedia.mediauploader.video.data

import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import com.tokopedia.mediauploader.video.data.entity.VideoUploader
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface VideoUploadServices {

    @POST
    @Multipart
    suspend fun uploadSingleVideo(
        /*
        * get url from data policy
        * @type: String
        * */
        @Url urlToUpload: String,

        /*
        * video file using Part
        * @type: Blob
        * */
        @Part videoFile: MultipartBody.Part,

        /*
        * video file name
        * @type: String
        * */
        @Field(PARAM_FILE_NAME) fileName: String,

        /*
        * change time out at runtime
        * @type: String
        * */
        @Header(HEADER_TIMEOUT) timeOut: String
    ) : VideoUploader

    companion object {
        private const val PARAM_FILE_NAME = "file_name"
    }

}