package com.tokopedia.mediauploader.video.data

import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.entity.VideoUploader
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface VideoUploadServices {

    /*
    * This services used for simple video upload,
    * which is for uploading a video under 20mb of size.
    * There's no slicing blob video file, just run as is.
    * @param: url (string)
    * @param: videoFile (blob)
    * @param: fileName (string)
    * @param: timeOut (string)
    * */
    @Multipart
    @POST suspend fun simpleUpload(
        @Url urlToUpload: String,
        @Part videoFile: MultipartBody.Part,
        @Part(BODY_FILE_NAME) fileName: RequestBody,
        @Header(HEADER_TIMEOUT) timeOut: String
    ) : VideoUploader

    /*
    * Init to instantiated the multi-part large video upload
    * send the raw-string only as identifier and mark as
    * starting point of large video upload
    * @param: url (string)
    * @param: fileName (string)
    * @param: sourceId (string)
    * */
    @FormUrlEncoded
    @POST suspend fun initLargeUpload(
        @Url urlToUpload: String,
        @Field(BODY_FILE_NAME) fileName: String,
        @Field(BODY_SOURCE_ID) sourceId: String
    ) : VideoLargeUploader

    @Multipart
    @POST suspend fun uploadLargeUpload(
        @Url urlToUpload: String,
        @Part(BODY_SOURCE_ID) sourceId: RequestBody,
        @Part(BODY_UPLOAD_ID) uploadId: RequestBody,
        @Part(BODY_PART_NUMBER) partNumber: RequestBody,
        @Part videoFile: MultipartBody.Part,
        @Header(HEADER_TIMEOUT) timeOut: String
    ) : VideoLargeUploader

    @GET suspend fun isValidChunkLargeUpload(
        @Url urlToUpload: String,
        @Query(BODY_FILE_NAME) fileName: String,
        @Query(BODY_UPLOAD_ID) uploadId: String,
        @Query(BODY_PART_NUMBER) partNumber: String
    ) : VideoLargeUploader

    @FormUrlEncoded
    @POST suspend fun completeLargeUpload(
        @Url urlToUpload: String,
        @Field(BODY_FILE_NAME) fileName: String,
        @Field(BODY_UPLOAD_ID) uploadId: String,
        @Header(HEADER_AUTH) accessToken: String
    ) : VideoLargeUploader

    companion object {
        private const val BODY_FILE_NAME = "file_name"
        private const val BODY_SOURCE_ID = "source_id"
        private const val BODY_UPLOAD_ID = "upload_id"
        private const val BODY_PART_NUMBER = "part_number"

        private const val HEADER_AUTH = "Authorization"
    }

}