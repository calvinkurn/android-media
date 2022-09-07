package com.tokopedia.universal_sharing.data.repository

import com.tokopedia.universal_sharing.data.model.BranchLinkResponse
import retrofit2.Response

interface ExtractBranchLinkRepository {
    suspend fun getDeeplink(branchUrl: String): Response<BranchLinkResponse>
}