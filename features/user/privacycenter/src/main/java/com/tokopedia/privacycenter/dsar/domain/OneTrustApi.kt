package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.privacycenter.dsar.model.CreateRequestBody
import com.tokopedia.privacycenter.dsar.model.CreateRequestResponse
import com.tokopedia.privacycenter.dsar.model.GetCredentialResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface OneTrustApi {
    @POST("access/v1/oauth/token")
    @Headers("content-type", "multipart/form-data")
    suspend fun getCredentials(): Response<GetCredentialResponse>

    @POST("datasubject/v2/requestqueues/{templateId}")
    @Headers("content-type", "application/json")
    suspend fun createRequest(@Body body: CreateRequestBody, @Path("templateId") templateId: String): Response<CreateRequestResponse>

    @POST("datasubject/v2/requestqueues/{{templateId}}")
    @Headers("content-type", "application/json")
    suspend fun updateRequest(@Body body: CreateRequestBody): Response<CreateRequestResponse>


}
