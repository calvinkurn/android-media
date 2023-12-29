package com.tokopedia.play.broadcaster.data.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created By : Jonathan Darwin on March 13, 2023
 */
interface BeautificationAssetApi {
    @Streaming
    @GET
    suspend fun downloadAsset(@Url url: String): ResponseBody
}
