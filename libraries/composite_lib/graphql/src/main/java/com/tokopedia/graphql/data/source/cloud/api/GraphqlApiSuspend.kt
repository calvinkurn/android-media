package com.tokopedia.graphql.data.source.cloud.api;

import com.google.gson.JsonArray;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.GraphqlRequest;


import kotlinx.coroutines.Deferred;
import retrofit2.http.Body;
import retrofit2.http.Header
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Vishal
 */
interface GraphqlApiSuspend {
    @POST("./")
    @Headers(GraphqlConstant.GqlApiKeys.GRAPHQL_HEADER)
    suspend fun getResponseSuspend(
            @Body requestObject:MutableList<GraphqlRequest>,
            @Header(AKAMAI_SENSOR_DATA) sensorData: String
    ) : JsonArray

    companion object {
        private const val AKAMAI_SENSOR_DATA = "X-acf-sensor-data"
    }
}