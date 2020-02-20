package com.tokopedia.graphql.data.source.cloud.api;

import com.google.gson.JsonArray;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.GraphqlRequest;

import java.util.List;

import kotlinx.coroutines.Deferred;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Vishal
 */
public interface GraphqlApi {
    @POST("./")
    @Headers(GraphqlConstant.GqlApiKeys.GRAPHQL_HEADER)
    Observable<JsonArray> getResponse(@Body List<GraphqlRequest> requestObject);

    @POST("./")
    @Headers(GraphqlConstant.GqlApiKeys.GRAPHQL_HEADER)
    Observable<Response<JsonArray>> getResponse(@Body List<GraphqlRequest> requestObject,
                                                @Header(GraphqlConstant.GqlApiKeys.CACHE) String values);
}
