package com.tokopedia.graphql.data.source.cloud.api;

import com.google.gson.JsonArray
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlRequest
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Vishal
 */
interface GraphqlApiSuspend {

    @POST("./")
    @Headers(GraphqlConstant.GqlApiKeys.GRAPHQL_HEADER)
    suspend fun getResponseSuspend(
            @Body requestObject: MutableList<GraphqlRequest>,
            @HeaderMap header: Map<String, String>
    ) : JsonArray

}