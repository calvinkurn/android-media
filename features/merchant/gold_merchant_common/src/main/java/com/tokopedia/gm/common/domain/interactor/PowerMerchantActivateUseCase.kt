package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldActivationSubscription
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 29/03/21
 */

@GqlQuery("PowerMerchantActivateGqlQuery", PowerMerchantActivateUseCase.QUERY)
class PowerMerchantActivateUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<PMActivationStatusUiModel>() {

    override suspend fun executeOnBackground(): PMActivationStatusUiModel {
        val gqlRequest = GraphqlRequest(
            PowerMerchantActivateGqlQuery(),
            GoldActivationSubscription::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GoldActivationSubscription::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data: GoldActivationSubscription =
                gqlResponse.getData<GoldActivationSubscription>(GoldActivationSubscription::class.java)
                    ?: throw RuntimeException("returns null from backend")
            val message = data.goldActivationData.header.message.firstOrNull().orEmpty()
            return PMActivationStatusUiModel(
                isSuccess = data.isSuccess(),
                message = message,
                currentShopTier = data.goldActivationData.data.shopTier,
                errorCode = data.goldActivationData.header.errorCode
            )
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
         mutation activatePowerMerchant(${'$'}source: String!) {
           goldActivationSubscription(source: ${'$'}source) {
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
        """
        private const val SOURCE = "source"

        fun createActivationParam(source: String): RequestParams {
            return RequestParams.create().apply {
                putString(SOURCE, source)
            }
        }
    }
}