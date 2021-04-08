package com.tokopedia.analyticsdebugger.cassava.data.api

import com.tokopedia.analyticsdebugger.cassava.data.entity.QueryListEntity
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * @author by furqan on 07/04/2021
 */
interface CassavaApi {
    @GET(CassavaUrl.GET_QUERY_LIST)
    suspend fun getQueryList(
            @Header("Content-Type") contentType: String = "application/json",
            @Query("token") token: String,
            @Query("journey_id") journeyId: Int
    ): QueryListEntity.DataResponse
}