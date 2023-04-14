package com.tokopedia.favorite.data.source.apis

import com.tokopedia.favorite.domain.model.FavoritShopResponseData
import com.tokopedia.network.data.model.response.GraphqlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by naveengoyal on 5/7/18.
 */
interface FavoriteShopApi {

    @POST("./")
    @Headers("Content-Type: application/json")
    suspend fun suspendGetFavoritShopsData(@Body requestBody: String): Response<GraphqlResponse<FavoritShopResponseData>>

}
