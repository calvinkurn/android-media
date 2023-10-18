package com.tokopedia.developer_options.branchlink.domain

import com.google.gson.JsonObject
import com.tokopedia.developer_options.branchlink.data.repository.BranchLinkRepository
import javax.inject.Inject

class BranchLinkUseCase @Inject constructor(private val repository: BranchLinkRepository) {

    suspend operator fun invoke(branchUrl: String): JsonObject {
        val response = repository.getDeeplink(branchUrl)
        return response.body()?.data ?: throw Exception("response body is null")
    }
}
