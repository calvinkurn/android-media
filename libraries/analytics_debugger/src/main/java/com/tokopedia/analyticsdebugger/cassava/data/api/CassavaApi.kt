package com.tokopedia.analyticsdebugger.cassava.data.api

import com.tokopedia.analyticsdebugger.cassava.data.entity.QueryListEntity
import com.tokopedia.analyticsdebugger.cassava.data.entity.ValidationResultEntity
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import retrofit2.http.*

/**
 * @author by furqan on 07/04/2021
 */
interface CassavaApi {

    @GET(CassavaUrl.GET_QUERY_LIST)
    @Headers("Content-Type: application/json")
    suspend fun getQueryList(
            @Query("token") token: String,
            @Query("journey_id") journeyId: String
    ): QueryListEntity.DataResponse

    @POST(CassavaUrl.POST_VALIDATION_RESULT)
    @Headers("Content-Type: application/json")
    suspend fun postValidationResult(
            @Path("journeyId") journeyId: String,
            @Body validationRequest: ValidationResultRequest
    ): ValidationResultEntity

}