package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.DeactivationPowerMerchantResponse
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 31/03/21
 */

class DeactivatePMUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<Boolean>() {

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(QUERY, DeactivationPowerMerchantResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)
        val gqlErrors = gqlResponse.getError(DeactivationPowerMerchantResponse::class.java)

        if (gqlErrors.isNullOrEmpty()) {
            val data = gqlResponse.getData<DeactivationPowerMerchantResponse>()
            return data.isSuccess
        } else {
            throw MessageErrorException(gqlErrors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        private const val QUEST_KEY = "quest_data"
        private val QUERY = """
            mutation deactivatePowerMerchant(${'$'}quest_data: [questData]) {
              goldTurnOffSubscription(autoExtend: false, quest: ${'$'}quest_data) {
                header {
                  error_code
                }
                data {
                  expiredTime
                }
              }
            }
        """.trimIndent()

        fun createRequestParam(questionData: MutableList<PMCancellationQuestionnaireAnswerModel>): RequestParams {
            return RequestParams.create().apply {
                putObject(QUEST_KEY, questionData)
            }
        }
    }
}