package com.tokopedia.developer_options.branchlink.data.repository

import com.google.gson.JsonObject
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response

interface BranchLinkRepository {
    suspend fun getDeeplink(branchUrl: String): Response<DataResponse<JsonObject>>
}
