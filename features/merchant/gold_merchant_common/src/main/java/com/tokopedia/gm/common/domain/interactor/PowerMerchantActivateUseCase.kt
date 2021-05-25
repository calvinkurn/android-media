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
                    ?: throw MessageErrorException("returns null from backend")
            val errorMessage = data.goldActivationData.header.message.firstOrNull().orEmpty()
            return PMActivationStatusUiModel(
                    isSuccess = data.isSuccess(),
                    errorMessage = errorMessage,
                    currentShopTier = data.goldActivationData.data.shopTier
            )
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        private val QUERY = """
         mutation activatePowerMerchant {
           goldActivationSubscription {
             header {
               process_time
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

        fun createActivationParam(currentShopTier: Int, nextShopTierType: Int): RequestParams {
            return RequestParams.create().apply {
                putInt(CURRENT_SHOP_TIER, currentShopTier)
                putInt(NEXT_SHOP_TIER, nextShopTierType)
            }
        }
    }
}