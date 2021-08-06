package com.tokopedia.home_account.linkaccount.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.linkaccount.base.BaseUseCase
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.usecase.RequestParams

/**
 * Created by Yoris on 05/08/21.
 */

class GetLinkStatusUseCase(private val repository: GraphqlRepository)
    : BaseUseCase<RequestParams, LinkStatusResponse>() {

    override suspend fun execute(params: RequestParams): LinkStatusResponse {
        return execute(query, repository, params)
    }

    companion object {
        val query = """
            
        """.trimIndent()
    }
}