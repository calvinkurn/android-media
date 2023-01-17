package com.tokopedia.universal_sharing.data.repository

import com.tokopedia.network.authentication.AuthKey.Companion.KEY_BRANCHIO
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.universal_sharing.data.api.ExtractBranchLinkApi
import com.tokopedia.universal_sharing.data.model.BranchLinkResponse
import retrofit2.Response
import javax.inject.Inject

class ExtractBranchLinkDataStore @Inject constructor(private val api: ExtractBranchLinkApi) : ExtractBranchLinkRepository {
    override suspend fun getDeeplink(branchUrl: String): Response<DataResponse<BranchLinkResponse>> {
        return api.getDeeplink(BRANCH_URL, branchUrl, KEY_BRANCHIO)
    }

    companion object {
        private const val BRANCH_URL = "https://api.branch.io/v1/url"
    }
}