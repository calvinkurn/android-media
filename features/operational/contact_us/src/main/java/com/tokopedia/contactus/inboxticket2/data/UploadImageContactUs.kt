package com.tokopedia.contactus.inboxticket2.data

import com.tokopedia.contactus.orderquery.data.ImageUploadResult
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadImageContactUs {
    @Multipart
    @POST("/upload/attachment")
    suspend fun uploadImage(
            @Header("Content-MD5") contentMD5: String?,
            @Header("Date") date: String?,
            @Header("Authorization") authorization: String?,
            @Header("X-Method") xMethod: String?,
            @Part("user_id") userId: RequestBody?,
            @Part("device_id") deviceId: RequestBody?,
            @Part("hash") hash: RequestBody?,
            @Part("device_time") deviceTime: RequestBody?,
            @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody?,
            @Part("id") imageId: RequestBody?,
            @Part("web_service") web_service: RequestBody?
    ): ImageUploadResult

    @Multipart
    @POST("/upload/attachment/public/image")
    suspend fun uploadImagePublic(
            @Header("Content-MD5") contentMD5: String?,
            @Header("Date") date: String?,
            @Header("Authorization") authorization: String?,
            @Header("X-Method") xMethod: String?,
            @Part("user_id") userId: RequestBody?,
            @Part("device_id") deviceId: RequestBody?,
            @Part("hash") hash: RequestBody?,
            @Part("device_time") deviceTime: RequestBody?,
            @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody?,
            @Part("id") imageId: RequestBody?,
            @Part("web_service") web_service: RequestBody?
    ): ImageUploadResult
}