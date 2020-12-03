package com.tokopedia.feedback_form.feedbackpage.domain.service

import com.tokopedia.feedback_form.feedbackpage.domain.request.FeedbackFormRequest
import com.tokopedia.feedback_form.feedbackpage.domain.response.CommitResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.FeedbackDataResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.FeedbackFormResponse
import com.tokopedia.feedback_form.feedbackpage.domain.response.ImageResponse
import okhttp3.MultipartBody
import retrofit2.http.*
import rx.Observable

interface FeedbackApiInterface {

    @GET("/api/v1/feedback/form")
    fun getCategories(): Observable<FeedbackDataResponse>

    @POST("/api/v1/feedback/create")
    fun createFeedbackForm(@Body feedbackRequest: FeedbackFormRequest): Observable<FeedbackFormResponse>

    @Multipart
    @POST
    fun uploadAttachment(@Url url: String, @Part file: MultipartBody.Part): Observable<ImageResponse>

    @POST
    fun commitData(@Url url: String): Observable<CommitResponse>
}