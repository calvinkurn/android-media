package com.tokopedia.product.addedit.description.data.source

import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YoutubeVideoApiService {
    @GET("/youtube/v3/videos")
    suspend fun getVideoDetail(@QueryMap params: Map<String, String>): Response<YoutubeResponse>
}