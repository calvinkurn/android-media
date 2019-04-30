package com.tokopedia.videouploader.data

import okhttp3.MultipartBody
import retrofit2.http.*
import rx.Observable

/**
 * @author by nisie on 13/03/19.
 */

interface UploadVideoApi {

    @Multipart @POST
    fun uploadVideo(@Url videoUrl: String,
                    @Header("x-token") videoToken: String,
                    @Part video: MultipartBody.Part
    ): Observable<String>

}