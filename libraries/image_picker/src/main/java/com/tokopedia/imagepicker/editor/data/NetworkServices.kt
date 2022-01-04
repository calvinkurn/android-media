package com.tokopedia.imagepicker.editor.data

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Observable

interface NetworkServices {

    @POST("remove-background/")
    @Multipart
    fun removeBackground(
        @Part file: MultipartBody.Part
    ): Observable<ResponseBody>

}