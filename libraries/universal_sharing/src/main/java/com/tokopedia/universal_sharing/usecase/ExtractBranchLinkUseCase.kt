package com.tokopedia.universal_sharing.usecase

import com.tokopedia.universal_sharing.data.repository.ExtractBranchLinkRepository

class ExtractBranchLinkUseCase(private val repository: ExtractBranchLinkRepository) {

    suspend operator fun invoke(branchUrl: String) = repository.getDeeplink(branchUrl)
}