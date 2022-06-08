package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodAvailabilitySection
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata.TokoFoodCheckoutMetadata
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodCartInfoParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodCartParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.URLEncoder
import javax.inject.Inject

class CheckoutGeneralTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<CheckoutTokoFood, CheckoutGeneralTokoFoodResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        mutation TokoFoodCheckoutGeneral($$PARAMS_KEY: CheckoutGeneralV2Params!) {
          checkout_general_v2(params: $$PARAMS_KEY) {
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
    """.trimIndent()

    override suspend fun execute(params: CheckoutTokoFood): Flow<CheckoutGeneralTokoFoodResponse> =
        flow {
            val param = generateParam(params)
            val response =
                repository.request<Map<String, Any>, CheckoutGeneralTokoFoodResponse>(
                    graphqlQuery(),
                    param
                )
            emit(response)
        }

    companion object {

        private const val PARAMS_KEY = "params"

        private fun generateParam(tokoFood: CheckoutTokoFood): Map<String, Any> {
            val checkoutMetadata = TokoFoodCheckoutMetadata.convertCheckoutDataIntoMetadata(tokoFood)
            val metadataString = checkoutMetadata.generateString()
            val param =
                CheckoutGeneralTokoFoodParam(
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