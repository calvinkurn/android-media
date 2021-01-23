package com.tokopedia.graphql.data.source.cloud.api;

import com.google.gson.JsonArray
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.GraphqlConstant.GqlApiKeys.ANDROID_FLAG
import com.tokopedia.graphql.data.model.GraphqlRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Vishal
 */
interface GraphqlApiSuspend {


    @POST("./")
    @Headers(ANDROID_FLAG)
    suspend fun getResponseSuspend(@Body requestObject: MutableList<GraphqlRequest>,
                                   @HeaderMap header: Map<String, String>,
                                   @Header(GraphqlConstant.GqlApiKeys.CACHE) values: String?): Response<JsonArray>
}
