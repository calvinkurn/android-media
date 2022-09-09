package com.tokopedia.universal_sharing.usecase

import android.util.Log
import com.tokopedia.universal_sharing.data.model.BranchLinkResponse
import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkRepository
import java.lang.Exception
import javax.inject.Inject

class ExtractBranchLinkUseCase @Inject constructor(private val repository: ExtractBranchLinkRepository) {

    suspend operator fun invoke(branchUrl: String): BranchLinkResponse {
        val response = repository.getDeeplink(branchUrl)
        return response.body()!!.data
    }
}