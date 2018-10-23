package com.tokopedia.flashsale.management.coroutineGraphql

import com.google.gson.JsonArray
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlRequest
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GraphqlApiKt{
    @POST("./")
    @Headers(GraphqlConstant.GqlApiKeys.GRAPHQL_HEADER)
    fun getResponse(@Body requestObjects: List<GraphqlRequest>): Deferred<JsonArray>
}