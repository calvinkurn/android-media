package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.KycStatus
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.KycStatusResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class KycStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, KycStatusResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query kycStatus(${'$'}projectID: Int!) {
            kycStatus(projectID: ${'$'}projectID) {
              Message
              Detail {
                IsSuccess
                UserId
                ProjectId
                Status
                StatusName
                CreateTime
                CreateBy
                UpdateTime
                DataSource
              }
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Int): KycStatusResult {
        val parameter = mapOf(PROJECT_ID to params)
        val response: KycStatus = repository
            .request<Map<String, Int>, KycStatusResponse>(graphqlQuery(), parameter)
            .kycStatus

        return if (response.message.isNotEmpty()) {
            KycStatusResult.Failed(MessageErrorException(response.message.first()))
        } else {
            KycStatusResult.Success(status = response.detail.status)
        }
    }

    companion object {
        private const val PROJECT_ID = "projectID"
    }
}
