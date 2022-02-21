package com.tokopedia.mediauploader.video.data

import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.entity.SimpleUploader
import com.tokopedia.mediauploader.video.data.entity.Transcoding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface VideoUploadServices {

    @Multipart
    @POST
    suspend fun simpleUpload(
        @Url url: String,
        @Part videoFile: MultipartBody.Part,
        @Part(BODY_FILE_NAME) fileName: RequestBody,
        @Header(HEADER_TIMEOUT) timeOut: String
    ): SimpleUploader

    @FormUrlEncoded
    @POST
    suspend fun initLargeUpload(
        @Url url: String,
        @Field(BODY_FILE_NAME) fileName: String,
        @Field(BODY_SOURCE_ID) sourceId: String
    ): LargeUploader

    @Multipart
    @POST
    suspend fun uploadLargeUpload(
        @Url url: String,
        @Part(BODY_SOURCE_ID) sourceId: RequestBody,
        @Part(BODY_UPLOAD_ID) uploadId: RequestBody,
        @Part(BODY_PART_NUMBER) partNumber: RequestBody,
        @Part videoFile: MultipartBody.Part,
        @Header(HEADER_TIMEOUT) timeOut: String
    ): LargeUploader

    @GET
    suspend fun chunkCheckerUpload(
        @Url url: String,
        @Query(BODY_FILE_NAME) fileName: String,
        @Query(BODY_UPLOAD_ID) uploadId: String,
        @Query(BODY_PART_NUMBER) partNumber: String
    ): LargeUploader

    @FormUrlEncoded
    @POST
    suspend fun completeLargeUpload(
        @Url url: String,
        @Field(BODY_UPLOAD_ID) uploadId: String
    ): LargeUploader

    @GET
    suspend fun checkTranscodingStatus(
        @Url url: String
    ): Transcoding

    @FormUrlEncoded
    @POST
    suspend fun abortLargeUpload(
        @Url url: String,
        @Field(BODY_UPLOAD_ID) uploadId: String
    ): LargeUploader

    companion object {
        private const val BODY_FILE_NAME = "file_name"
        private const val BODY_SOURCE_ID = "source_id"
        private const val BODY_UPLOAD_ID = "upload_id"
        private const val BODY_PART_NUMBER = "part_number"
    }

}