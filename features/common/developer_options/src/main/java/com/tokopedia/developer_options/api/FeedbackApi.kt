package com.tokopedia.developer_options.api

import okhttp3.MultipartBody
import retrofit2.http.*
import rx.Observable

interface FeedbackApi {
    @POST("issue/")
    @Headers("Authorization: Basic YW5kcm9pZC1hcHBzQHRva29wZWRpYS5jb206YjU1QWNBbjJBZFpjcEVpR200MHMzQjkx", "Accept: application/json", "Content-Type: application/json")
    fun getResponse(@Body feedbackRequest: FeedbackRequest): Observable<FeedbackResponse>

    @Multipart
    @POST
    @Headers("Authorization: Basic YW5kcm9pZC1hcHBzQHRva29wZWRpYS5jb206YjU1QWNBbjJBZFpjcEVpR200MHMzQjkx",
            "Accept: application/json",
            "X-Atlassian-Token: no-check",
            "Content-Type: multipart/form-data" )
    fun getImageResponse(@Url url: String, @Part file: MultipartBody.Part) : Observable<ImageResponse>
}