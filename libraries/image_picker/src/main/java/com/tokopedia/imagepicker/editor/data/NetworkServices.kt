package com.tokopedia.imagepicker.editor.data

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface NetworkServices {

//    @POST("v1.0/removebg")
//    @FormUrlEncoded
//    fun removeBackground(
//        @Field("image_file_b64") base64: String,
//        @Field("size") size: String,
//        @Header("X-Api-Key") apiKey: String
//    ): Observable<ResponseBody>

    @POST("remove-background/")
    @Multipart
    fun removeBackground(
        @Part file: MultipartBody.Part
    ): Observable<ResponseBody>

}