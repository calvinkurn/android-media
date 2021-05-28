package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldActivationSubscription
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 29/03/21
 */

class PowerMerchantActivateUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<PMActivationStatusUiModel>() {

    override suspend fun executeOnBackground(): PMActivationStatusUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GoldActivationSubscription::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GoldActivationSubscription::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data: GoldActivationSubscription = gqlResponse.getData<GoldActivationSubscription>(GoldActivationSubscription::class.java)
                    ?: throw RuntimeException("returns null from backend")
            val message = data.goldActivationData.header.message.firstOrNull().orEmpty()
            return PMActivationStatusUiModel(
                    isSuccess = data.isSuccess(),
                    message = message,
                    currentShopTier = data.goldActivationData.data.shopTier
            )
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private val QUERY = """
         mutation activatePowerMerchant(${'$'}source: String!, ${'$'}current_shop_tier: Int, ${'$'}next_shop_tier: Int) {
           goldActivationSubscription(source: ${'$'}source, current_shop_tier: ${'$'}current_shop_tier, next_shop_tier: ${'$'}next_shop_tier) {
             header {
               messages
               error_code
               reason
             }
             data {
               shop_tier
               product {
                 id
                 initial_duration
                 auto_extend
                 name
               }
               expired_time
             }
           }
         }
        """.trimIndent()

        private const val CURRENT_SHOP_TIER = "current_shop_tier"
        private const val NEXT_SHOP_TIER = "next_shop_tier"
        private const val SOURCE = "source"

        fun createActivationParam(currentShopTier: Int, nextShopTierType: Int, source: String): RequestParams {
            return RequestParams.create().apply {
                putString(SOURCE, source)
                putInt(CURRENT_SHOP_TIER, currentShopTier)
                putInt(NEXT_SHOP_TIER, nextShopTierType)
            }
        }
    }
}