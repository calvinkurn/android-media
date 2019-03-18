package com.tokopedia.videouploader.data

import com.tokopedia.videouploader.domain.pojo.GenerateTokenPojo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * @author by nisie on 13/03/19.
 */

interface UploadVideoApi {

    @GET(UploadVideoUrl.URL_GENERATE_TOKEN)
    fun generateToken(@QueryMap params: HashMap<String, String>):
            Observable<GenerateTokenPojo>

    @Multipart
    @POST(UploadVideoUrl.URL_UPLOAD_VIDEO)
    fun uploadVideo(@Header("x-token") videoToken: String,
                    @Part video: MultipartBody.Part):
            Observable<String>

    @GET(UploadVideoUrl.URL_GET_VIDEO_INFO)
    fun getVideoInfo(@QueryMap params: HashMap<String, Any>):
            Observable<Response<String>>
}