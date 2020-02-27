package com.tokopedia.updateinactivephone.data.network.api

import com.tokopedia.core.network.retrofit.response.TkpdResponse
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable

interface UploadImageApi {

    @Multipart
    @POST("")
    fun uploadImage(@Url url: String,
                    @QueryMap params: Map<String, String>,
                    @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody): Observable<Response<UploadImageData>>

    @FormUrlEncoded
    @POST(UpdateInactivePhoneURL.GET_UPLOAD_HOST)
    fun getUploadHost(@FieldMap params: Map<String, Any>): Observable<Response<TkpdResponse>>
}
