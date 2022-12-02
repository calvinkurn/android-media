package com.tokopedia.privacycenter.remote

import com.tokopedia.privacycenter.data.CreateRequestBody
import com.tokopedia.privacycenter.data.CreateRequestResponse
import com.tokopedia.privacycenter.data.GetCredentialResponse
import com.tokopedia.privacycenter.data.GetRequestDetailResponse
import com.tokopedia.privacycenter.data.SearchRequestBody
import com.tokopedia.privacycenter.data.SearchRequestResponse
import com.tokopedia.privacycenter.data.UpdateRequestBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface OneTrustApi {
    @POST("api/access/v1/oauth/token")
    @Multipart
    suspend fun getCredentials(@Part("grant_type") grantType: RequestBody, @Part("client_id") clientId: RequestBody, @Part("client_secret") clientSecret: RequestBody): Response<GetCredentialResponse>

    @POST("api/datasubject/v2/requestqueues/{templateId}")
    suspend fun createRequest(@Body body: CreateRequestBody, @Path("templateId") templateId: String, @HeaderMap header: Map<String, String>): Response<CreateRequestResponse>

    @PUT("api/datasubject/v2/requestqueues/{requestQueueRefId}/movestages/en-us")
    suspend fun updateRequest(@Body body: UpdateRequestBody, @Path("requestQueueRefId") refId: String, @HeaderMap header: Map<String, String>): Response<Void>

    @POST("api/datasubject/v2/requestqueues/search/en-us")
    suspend fun searchRequest(@Body body: SearchRequestBody, @HeaderMap header: Map<String, String>): Response<SearchRequestResponse>

    @GET("api/datasubject/v2/requestqueues/{requestQueueRefId}/language/en-us")
    suspend fun getRequest(@Path("requestQueueRefId") refId: String, @HeaderMap header: Map<String, String>): Response<GetRequestDetailResponse>
}
