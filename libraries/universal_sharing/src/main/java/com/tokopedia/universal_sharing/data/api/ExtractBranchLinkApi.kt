package com.tokopedia.universal_sharing.data.api

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.universal_sharing.data.model.BranchLinkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ExtractBranchLinkApi {

    @GET
    suspend fun getDeeplink(@Url url: String, @Query("url") urlBranch: String, @Query("branch_key") key: String): Response<DataResponse<BranchLinkResponse>>
}