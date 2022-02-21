package com.tokopedia.interceptors.refreshtoken

import com.google.gson.JsonArray
import com.tokopedia.graphql.GraphqlConstant.GqlApiKeys
import com.tokopedia.graphql.data.model.GraphqlRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Yoris on 29/10/21.
 */

@JvmSuppressWildcards
interface RefreshTokenApi {

    @POST("./")
    @Headers(GqlApiKeys.ANDROID_FLAG)
    fun getResponse(@Body requestObject: List<GraphqlRequest>): Call<JsonArray>
}
