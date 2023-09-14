package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.ProjectInfoResponse
import javax.inject.Inject

class AccountLinkingStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, AccountLinkingStatusResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query kycProjectInfo (${'$'}projectID: Int!){
              kycProjectInfo(projectID: ${'$'}projectID) {
                AccountLinked
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Int): AccountLinkingStatusResult {
        val parameter = mapOf(PROJECT_ID to params)
        val response: ProjectInfoResponse = repository.request(graphqlQuery(), parameter)

        val linkedStatus = response.kycProjectInfo.accountLinked

        return if (linkedStatus == KEY_ACCOUNT_LINKED) {
            AccountLinkingStatusResult.Linked
        } else {
            AccountLinkingStatusResult.NotLinked
        }
    }

    companion object {
        private const val KEY_ACCOUNT_LINKED = 1
        private const val PROJECT_ID = "projectID"
    }
}

sealed class AccountLinkingStatusResult {
    object Loading : AccountLinkingStatusResult()
    object Linked : AccountLinkingStatusResult()
    object NotLinked : AccountLinkingStatusResult()
    data class Failed(val throwable: Throwable) : AccountLinkingStatusResult()
}

