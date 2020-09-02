package com.tokopedia.developer_options.api

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import rx.Observable

interface FeedbackApi {
    @POST("issue/")
    @Headers("Authorization: Basic YW5kcm9pZC1hcHBzQHRva29wZWRpYS5jb206YjU1QWNBbjJBZFpjcEVpR200MHMzQjkx", "Accept: application/json", "Content-Type: application/json")
    fun getResponse(@Body feedbackRequest: FeedbackRequest): Observable<FeedbackResponse>
}