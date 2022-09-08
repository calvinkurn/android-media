package com.tokopedia.universal_sharing.usecase

import com.tokopedia.universal_sharing.data.model.BranchLinkResponse
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkRepository
import java.lang.Exception
import javax.inject.Inject

class ExtractBranchLinkUseCase @Inject constructor(private val repository: ExtractBranchLinkRepository) {

    suspend operator fun invoke(branchUrl: String): BranchLinkResponse {
        val response = repository.getDeeplink(branchUrl)
        if (response.isSuccessful && response.body() != null) return response.body()!!.data
        else throw Exception("error get deeplink from branchlink")
    }
}