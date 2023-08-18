package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.AccountLinkingStatusResponse
import javax.inject.Inject

class AccountLinkingStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, AccountLinkingStatusResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
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

    override suspend fun execute(params: Unit): AccountLinkingStatusResult {
        val parameter = mapOf(PARAM_LINKING to ACCOUNT_LINKING_TYPE)
        val response: AccountLinkingStatusResponse = repository.request(graphqlQuery(), parameter)

        val linkedStatus = response.accountsLinkerStatus.linkStatus

        return if (linkedStatus.isNotEmpty() && linkedStatus.first().status == KEY_ACCOUNT_LINKED) {
            AccountLinkingStatusResult.Linked
        } else {
            AccountLinkingStatusResult.NotLinked
        }
    }

    companion object {
        private const val KEY_ACCOUNT_LINKED = "linked"
        private const val PARAM_LINKING = "linking_type"
        private const val ACCOUNT_LINKING_TYPE = "account_linking"
    }
}

sealed class AccountLinkingStatusResult {
    object Loading : AccountLinkingStatusResult()
    object Linked : AccountLinkingStatusResult()
    object NotLinked : AccountLinkingStatusResult()
    data class Failed(val throwable: Throwable) : AccountLinkingStatusResult()
}

