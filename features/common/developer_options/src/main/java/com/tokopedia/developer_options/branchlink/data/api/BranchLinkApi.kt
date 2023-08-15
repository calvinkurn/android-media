package com.tokopedia.universal_sharing.data.api

import com.google.gson.JsonObject
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BranchLinkApi {

    @GET
    suspend fun getDeeplink(@Url url: String, @Query("url") urlBranch: String, @Query("branch_key") key: String): Response<DataResponse<JsonObject>>
}
