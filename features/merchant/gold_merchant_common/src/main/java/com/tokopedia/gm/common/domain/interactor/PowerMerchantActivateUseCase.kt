package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldActivationSubscription
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 29/03/21
 */

class PowerMerchantActivateUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<Boolean>() {

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(QUERY, GoldActivationSubscription::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GoldActivationSubscription::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data: GoldActivationSubscription = gqlResponse.getData<GoldActivationSubscription>(GoldActivationSubscription::class.java)
                    ?: throw RuntimeException("returns null from backend")
            return data.isSuccess()
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
               shop_id
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
    }
}