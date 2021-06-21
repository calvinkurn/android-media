package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.constant.PMConstant
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
        private const val KEY_QUEST = "quest"
        private const val KEY_SOURCE = "source"
        private const val KEY_CURRENT_SHOP_TIRE = "current_shop_tier"
        private const val KEY_NEXT_SHOP_TIRE = "next_shop_tier"
        private const val KEY_AUTO_EXTEND = "autoExtend"
        private val QUERY = """
            mutation deactivatePowerMerchant(${'$'}source: String!, ${'$'}quest: [questData], ${'$'}current_shop_tier: Int, ${'$'}next_shop_tier: Int) {
              goldTurnOffSubscription(source: ${'$'}source, quest: ${'$'}quest, current_shop_tier: ${'$'}current_shop_tier, next_shop_tier: ${'$'}next_shop_tier, autoExtend: false) {
                header {
                  error_code
                }
                data {
                  expiredTime
                }
              }
            }
        """.trimIndent()

        fun createRequestParam(
                questionData: MutableList<PMCancellationQuestionnaireAnswerModel>,
                currentShopTire: Int, nextShopTire: Int
        ): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_SOURCE, PMConstant.PM_SETTING_INFO_SOURCE)
                putInt(KEY_CURRENT_SHOP_TIRE, currentShopTire)
                putInt(KEY_NEXT_SHOP_TIRE, nextShopTire)
                putBoolean(KEY_AUTO_EXTEND, false)
                putObject(KEY_QUEST, questionData)
            }
        }
    }
}