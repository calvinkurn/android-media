package com.tokopedia.universal_sharing.data.repository

import com.google.gson.JsonObject
import com.tokopedia.developer_options.branchlink.data.repository.BranchLinkRepository
import com.tokopedia.network.authentication.AuthKey.Companion.KEY_BRANCHIO
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.universal_sharing.data.api.BranchLinkApi
import retrofit2.Response
import javax.inject.Inject

class BranchLinkDataStore @Inject constructor(private val api: BranchLinkApi) : BranchLinkRepository {
    override suspend fun getDeeplink(branchUrl: String): Response<DataResponse<JsonObject>> {
        return api.getDeeplink(BRANCH_URL, branchUrl, KEY_BRANCHIO)
    }

    companion object {
        private const val BRANCH_URL = "https://api.branch.io/v1/url"
    }
}
