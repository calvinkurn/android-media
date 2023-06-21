package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata.TokoFoodCheckoutMetadata
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodCartInfoParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodCartParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodParamOld
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponseOld
import javax.inject.Inject

private const val QUERY = """
        mutation TokoFoodCheckoutGeneral(${'$'}params: CheckoutGeneralV2Params!) {
          checkout_general_v2(params: ${'$'}params) {
            header {
              process_time
              reason
              error_code
            }
            data {
              success
              error
              error_state
              error_metadata
              message
              data{
                callback_url
                query_string
                redirect_url
              }
            }
          }
        }
    """

@GqlQuery("TokoFoodCheckoutGeneralOld", QUERY)
class CheckoutGeneralTokoFoodUseCaseOld @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<CheckoutGeneralTokoFoodResponseOld>(repository) {

    init {
        setTypeClass(CheckoutGeneralTokoFoodResponseOld::class.java)
        setGraphqlQuery(TokoFoodCheckoutGeneralOld())
    }

    suspend fun execute(params: CheckoutTokoFood): CheckoutGeneralTokoFoodResponseOld {
        val param = generateParam(params)
        setRequestParams(param)
        return executeOnBackground()
    }

    companion object {

        private const val PARAMS_KEY = "params"

        private fun generateParam(tokoFood: CheckoutTokoFood): Map<String, Any> {
            val checkoutMetadata = TokoFoodCheckoutMetadata.convertCheckoutDataIntoMetadata(tokoFood)
            val metadataString = checkoutMetadata.generateString()
            val param =
                CheckoutGeneralTokoFoodParamOld(
                    carts = CheckoutGeneralTokoFoodCartParam(
                        businessType = tokoFood.data.checkoutAdditionalData.checkoutBusinessId.toIntOrNull() ?: TokoFoodCartUtil.TOKOFOOD_BUSINESS_TYPE,
                        cartInfo = listOf(
                            CheckoutGeneralTokoFoodCartInfoParam(
                                metadata = metadataString,
                                dataType = tokoFood.data.checkoutAdditionalData.dataType
                            )
                        )
                    )
                )
            return mapOf(PARAMS_KEY to param)
        }

    }
}
