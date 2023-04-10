package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.CheckEligibilityResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.CheckOneKYCEligibility
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class CheckEligibilityUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, CheckEligibilityResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query checkOneKYCEligibility() {
                checkOneKYCEligibility() {
                  isSuccess
                  errorMessages
                  data {
                    flow
                    name
                  }
               }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): CheckEligibilityResult {
        val response: CheckOneKYCEligibility = repository.request<Unit, CheckEligibilityResponse>(graphqlQuery(), params).checkOneKYCEligibility

        return if (!response.isSuccess) {
            CheckEligibilityResult.Failed(MessageErrorException(response.errorMessages.first()))
        } else if (response.data.flow == KYCConstant.GotoKycFlow.PROGRESSIVE) {
            CheckEligibilityResult.Progressive(response.data.name)
        } else {
            CheckEligibilityResult.NonProgressive()
        }
    }
}
