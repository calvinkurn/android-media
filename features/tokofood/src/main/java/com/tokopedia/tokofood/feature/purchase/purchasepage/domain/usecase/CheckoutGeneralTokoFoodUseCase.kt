package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokoFoodParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokofoodParamBusinessCheckoutState
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokofoodParamBusinessData
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param.CheckoutGeneralTokofoodParamTransaction
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import javax.inject.Inject

private const val QUERY = """
        mutation TokoFoodCheckoutGeneral(${'$'}params: CheckoutCartGeneralParams) {
          checkout_cart_general(params: ${'$'}params) {
            header {
              process_time
              reason
              error_code
            }
            data {
              success
              error
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

@GqlQuery("TokoFoodCheckoutGeneral", QUERY)
open class CheckoutGeneralTokoFoodUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<CheckoutGeneralTokoFoodResponse>(repository) {

    init {
        setTypeClass(CheckoutGeneralTokoFoodResponse::class.java)
        setGraphqlQuery(TokoFoodCheckoutGeneral())
    }

    open suspend fun execute(params: CartGeneralCartListData): CheckoutGeneralTokoFoodResponse{
        val param = generateParam(params)
        setRequestParams(param)
        return executeOnBackground()
    }

    companion object {

        private const val PARAMS_KEY = "params"

        private fun generateParam(tokoFood: CartGeneralCartListData): Map<String, Any> {
            val tokofoodBusinessData = tokoFood.data.getTokofoodBusinessData()
            val checkoutAdditionalData = tokofoodBusinessData.customResponse.checkoutAdditionalData
            val param =
                CheckoutGeneralTokoFoodParam(
                    transaction = CheckoutGeneralTokofoodParamTransaction(
                        flowType = checkoutAdditionalData.flowType,
                        businessData = listOf(
                            CheckoutGeneralTokofoodParamBusinessData(
                                checkoutBusinessType = checkoutAdditionalData.checkoutBusinessId,
                                checkoutDataType = checkoutAdditionalData.dataType,
                                businessId = TokoFoodCartUtil.getBusinessId(),
                                businessCheckoutState = CheckoutGeneralTokofoodParamBusinessCheckoutState(
                                    userAddress = tokofoodBusinessData.customResponse.userAddress,
                                    shop = tokofoodBusinessData.customResponse.shop,
                                    shipping = tokofoodBusinessData.customResponse.shipping,
                                    shoppingSummary = tokofoodBusinessData.customResponse.shoppingSummary
                                ),
                                cartGroups = tokofoodBusinessData.cartGroups.orEmpty()
                            )
                        )
                    )
                )
            return mapOf(PARAMS_KEY to param)
        }

    }
}
