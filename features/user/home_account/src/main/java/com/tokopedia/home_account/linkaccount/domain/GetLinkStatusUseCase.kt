package com.tokopedia.home_account.linkaccount.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers

/**
 * Created by Yoris on 05/08/21.
 */

class GetLinkStatusUseCase(private val repository: GraphqlRepository)
    : CoroutineUseCase<RequestParams, LinkStatusResponse>(Dispatchers.IO) {

    override suspend fun execute(params: RequestParams): LinkStatusResponse {
        return request(repository, params.parameters)
    }

    fun createParams(linkingType: String): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_LINKING, linkingType)
        }
    }

    companion object {
        const val PARAM_LINKING = "linking_type"
        const val ACCOUNT_LINKING_TYPE = "account_linking"

        val query = """
            query link_status(${'$'}linking_type: String!){
                accountsLinkerStatus(linking_type:${'$'}linking_type){
                    link_status {
                        linking_type
                        status
                        partner_id
                    }
                    error
                }
            }
            """.trimIndent()
    }

    override fun graphqlQuery(): String = query
}