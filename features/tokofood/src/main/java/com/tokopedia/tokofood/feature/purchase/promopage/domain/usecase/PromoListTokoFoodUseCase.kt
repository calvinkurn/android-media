package com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.CartGeneralPromoListDataData
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.promopage.domain.param.PromoTokoFoodParam
import com.tokopedia.tokofood.feature.purchase.promopage.domain.param.PromoTokofoodParamBusinessData
import com.tokopedia.tokofood.feature.purchase.promopage.domain.param.PromoTokofoodParamCart
import com.tokopedia.tokofood.feature.purchase.promopage.domain.param.PromoTokofoodParamCustomRequest
import javax.inject.Inject

private const val QUERY = """
        query PromoListTokofood(${'$'}params: CartGeneralPromoListParams) {
          cart_general_promo_list(params: ${'$'}params) {
            data {
              message
              success
              data {
                business_data {
                  business_id
                  success
                  message
                  custom_response
                }
              }
            }
          }
        }
    """

@GqlQuery("PromoListTokofood", QUERY)
open class PromoListTokoFoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<PromoListTokoFoodResponse>(repository) {

    init {
        setTypeClass(PromoListTokoFoodResponse::class.java)
        setGraphqlQuery(PromoListTokofood())
    }

    open suspend fun execute(source: String,
                        merchantId: String,
                        cartList: List<String>): CartGeneralPromoListDataData {
        val param = generateParams(
            chosenAddressRequestHelper.getChosenAddress(),
            source,
            merchantId,
            cartList
        )
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.cartGeneralPromoList.data.isSuccess()) {
            return response.cartGeneralPromoList.data.data
        } else {
            throw MessageErrorException(response.cartGeneralPromoList.data.message)
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(chosenAddress: TokoFoodChosenAddress,
                                   source: String,
                                   merchantId: String,
                                   cartList: List<String>): Map<String, Any> {
            val params = PromoTokoFoodParam(
                source = source,
                businessData = listOf(
                    PromoTokofoodParamBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        customRequest = PromoTokofoodParamCustomRequest(
                            chosenAddress = chosenAddress,
                            merchantId = merchantId
                        ),
                        carts = cartList.map {
                            PromoTokofoodParamCart(
                                cartId = it
                            )
                        }
                    )
                )
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

}
