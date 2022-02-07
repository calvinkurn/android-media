package com.tokopedia.analyticsdebugger.cassava.data.api

import com.tokopedia.analyticsdebugger.cassava.data.entity.JourneyListEntity
import com.tokopedia.analyticsdebugger.cassava.data.entity.QueryListEntity
import com.tokopedia.analyticsdebugger.cassava.data.entity.ValidationResultEntity
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author by furqan on 07/04/2021
 */
interface CassavaApi {

    @GET(CassavaUrl.GET_QUERY_LIST)
    @Headers(
        "Content-Type: application/json",
        "x-device: android"
    )
    suspend fun getQueryList(
        @Query("token") token: String,
        @Query("journey_id") journeyId: String
    ): QueryListEntity.DataResponse

    @POST(CassavaUrl.POST_VALIDATION_RESULT)
    @Headers(
        "Content-Type: application/json",
        "x-device: android"
    )
    suspend fun postValidationResult(
        @Path("journeyId") journeyId: String,
        @Body validationRequest: ValidationResultRequest
    ): ValidationResultEntity

    @GET(CassavaUrl.GET_JOURNEY_LIST)
    @Headers(
        "Content-Type: application/json",
        "x-device: android"
    )
    suspend fun getJourneyList(
        @Query("token") token: String,
        @Query("tribe_name") tribeName: String,
        @Query("journey_name") journeyName: String
    ): JourneyListEntity.DataResponse

}