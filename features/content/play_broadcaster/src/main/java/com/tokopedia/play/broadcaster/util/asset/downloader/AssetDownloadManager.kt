package com.tokopedia.play.broadcaster.util.asset.downloader

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetDownloadManager @Inject constructor() {

    private val okhttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://gql.tokopedia.com/")
        .client(okhttpBuilder.build())
        .build()

    private val resourceApi = retrofit.create(AssetResourceApi::class.java)

    suspend fun download(url: String): ResponseBody {
        return resourceApi.downloadAsset(url)
    }

    interface AssetResourceApi {
        @Streaming
        @GET
        suspend fun downloadAsset(@Url fileUrl:String): ResponseBody
    }
}
