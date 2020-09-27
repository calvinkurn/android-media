package com.tokopedia.developer_options.api

import com.tokopedia.developer_options.api.request.FeedbackFormRequest
import com.tokopedia.developer_options.api.response.CategoriesResponse
import com.tokopedia.developer_options.api.response.FeedbackFormResponse
import com.tokopedia.developer_options.api.response.ImageResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable

interface FeedbackApiInterface {

    @GET("/api/v1/feedback/form")
    fun getCategories(): Observable<CategoriesResponse>

    @POST("/api/v1/feedback/create")
    fun createFeedbackForm(@Body feedbackRequest: FeedbackFormRequest): Observable<FeedbackFormResponse>

    @Multipart
    @POST
    fun uploadAttachment(@Url url: String, @Part file: MultipartBody.Part): Observable<ImageResponse>

    @POST
    fun commitData(@Url url: String): Observable<ResponseBody>
}