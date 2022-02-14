package com.tokopedia.home_account.linkaccount.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 05/08/21.
 */

class GetLinkStatusUseCase @Inject constructor (@ApplicationContext private val repository: GraphqlRepository,
                                                dispatcher: CoroutineDispatcher)
    : CoroutineUseCase<String, LinkStatusResponse>(dispatcher) {

    override suspend fun execute(params: String): LinkStatusResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    private fun createParams(linkingType: String): Map<String, String> =
        mapOf(PARAM_LINKING to linkingType)

    companion object {
        const val PARAM_LINKING = "linking_type"
        const val ACCOUNT_LINKING_TYPE = "account_linking"
    }

    override fun graphqlQuery(): String = """
            query link_status(${'$'}linking_type: String!){
                accountsLinkerStatus(linking_type:${'$'}linking_type){
                    link_status {
                        linking_type
                        status
                        partner_user_id
                        linked_time
                    }
                    error
                }
            }
            """.trimIndent()
}