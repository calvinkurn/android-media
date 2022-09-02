package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.cloud.model.DeactivationPowerMerchantResponse
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.presentation.model.DeactivationResultUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 31/03/21
 */

@GqlQuery("DeactivatePMGqlQuery", DeactivatePMUseCase.QUERY)
class DeactivatePMUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<DeactivationResultUiModel>() {

    override suspend fun executeOnBackground(): DeactivationResultUiModel {
        val gqlRequest = GraphqlRequest(
            DeactivatePMGqlQuery(), DeactivationPowerMerchantResponse::class.java, params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)
        val gqlErrors = gqlResponse.getError(DeactivationPowerMerchantResponse::class.java)

        if (gqlErrors.isNullOrEmpty()) {
            val data = gqlResponse.getData<DeactivationPowerMerchantResponse>()
            return DeactivationResultUiModel(
                data.goldTurnOffSubscription?.header?.errorCode.orEmpty()
            )
        } else {
            throw MessageErrorException(gqlErrors.joinToString(" - ") { it.message })
        }
    }

    companion object {
        internal const val QUERY = """
            mutation deactivatePowerMerchant(${'$'}source: String!, ${'$'}quest: [questData]) {
              goldTurnOffSubscription(source: ${'$'}source, quest: ${'$'}quest, autoExtend: false) {
                header {
                  error_code
                }
                data {
                  expiredTime
                }
              }
            }
        """
        private const val KEY_QUEST = "quest"
        private const val KEY_SOURCE = "source"

        fun createRequestParam(
            questionData: MutableList<PMCancellationQuestionnaireAnswerModel>
        ): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_SOURCE, PMConstant.PM_SETTING_INFO_SOURCE)
                putObject(KEY_QUEST, questionData)
            }
        }
    }
}