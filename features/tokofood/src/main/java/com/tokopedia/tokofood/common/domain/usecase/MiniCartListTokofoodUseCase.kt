package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CartListTokofoodParam
import com.tokopedia.tokofood.common.domain.param.CartListTokofoodParamBusinessData
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import javax.inject.Inject

private const val QUERY = """
        mutation MiniCartGeneralCartList(${'$'}params: CartGeneralCartListParams) {
          cart_general_cart_list(params: ${'$'}params) {
            data {
              message
              success
              data {
                shopping_summary {
                  business_breakdown {
                    business_id
                    total_bill
                    total_bill_fmt
                    product {
                      title
                      total_price
                      total_price_fmt
                      total_quantity
                      total_cart
                    }
                    add_ons {
                      title
                      price
                      price_fmt
                      custom_response
                    }
                  }
                }
                business_data {
                  business_id
                  success
                  message
                  additional_grouping {
                    details {
                      additional_group_id
                      cart_ids
                      message
                      additional_group_child_ids
                    }
                  }
                  custom_response
                  cart_groups {
                    cart_group_id
                    success
                    carts {
                      cart_id
                      success
                      product_id
                      shop_id
                      quantity
                      price
                      price_fmt
                      metadata
                      custom_response
                    }
                  }
                }
              }
            }
          }
        }
    """

@GqlQuery("MiniCartGeneralCartList", QUERY)
class MiniCartListTokofoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<CartListTokofoodResponse>(repository) {

    init {
        setTypeClass(CartListTokofoodResponse::class.java)
        setGraphqlQuery(MiniCartGeneralCartList())
    }

    suspend fun execute(source: String): CartGeneralCartListData? {
        val chosenAddress = chosenAddressRequestHelper.getChosenAddress()
        if (chosenAddress.addressId.isZero() || chosenAddress.geolocation.isBlank()) {
            return null
        }
        val additionalAttributes = CartAdditionalAttributesTokoFood(
            chosenAddressRequestHelper.getChosenAddress()
        )
        val param = generateParams(additionalAttributes, source)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.cartGeneralCartList.data.isSuccess()) {
            return response.cartGeneralCartList.data
        } else {
            throw MessageErrorException(response.cartGeneralCartList.data.getErrorMessage())
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: CartAdditionalAttributesTokoFood,
                                   source: String): Map<String, Any> {
            val params = CartListTokofoodParam(
                source = source,
                businessData = listOf(
                    CartListTokofoodParamBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        customRequest = additionalAttributes
                    )
                )
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

}
