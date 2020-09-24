package com.tokopedia.developer_options.api

import com.tokopedia.developer_options.api.request.FeedbackFormRequest
import com.tokopedia.developer_options.api.response.CategoriesResponse
import com.tokopedia.developer_options.api.response.FeedbackFormResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
            "X-Atlassian-Token: no-check" )
    fun getImageResponse(@Url url: String, @Part file: MultipartBody.Part) : Observable<List<ImageResponse>>

    @GET("/api/v1/feedback/form")
    fun getCategories(): Observable<CategoriesResponse>

    @POST("/api/v1/feedback/create")
    fun createFeedbackForm(@Body feedbackRequest: FeedbackFormRequest): Observable<FeedbackFormResponse>

    @Multipart
    @POST
    fun uploadAttachment(@Url url: String, @Part("Key") key: RequestBody, @Part file: MultipartBody.Part): Observable<String>

    @POST
    fun commitData(@Url url: String): Observable<String>
}