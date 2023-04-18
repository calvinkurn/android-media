package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.response.ATCTokofoodResponse
import com.tokopedia.tokofood.common.domain.response.CartGeneralAddToCartData
import com.tokopedia.tokofood.common.presentation.mapper.UpdateProductMapper
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import javax.inject.Inject

private const val QUERY = """
          mutation UpdateCartTokofood(${'$'}params: CartGeneralAddToCartParams) {
            cart_general_add_to_cart(params: ${'$'}params) {
              data {
                success
                message
                data {
                  business_data {
                    business_id
                    success
                    message
                    additional_grouping {
                      details {
                        additional_group_id
                        cart_ids
                        message
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
                        metadata
                        price
                        price_fmt
                        custom_response
                      }
                    }
                  }
                }
              }
            }
          }
    """

@GqlQuery("UpdateCartTokofood", QUERY)
class UpdateCartTokoFoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<ATCTokofoodResponse>(repository) {

    init {
        setTypeClass(ATCTokofoodResponse::class.java)
        setGraphqlQuery(UpdateCartTokofood())
    }

    suspend fun execute(updateParam: UpdateParam,
                        source: String): CartGeneralAddToCartData {
        val param = generateParams(
            updateParam,
            chosenAddressRequestHelper.getChosenAddress(),
            source
        )
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.isSuccess()) {
            return response.cartGeneralAddToCart.data
        } else {
            throw MessageErrorException(response.cartGeneralAddToCart.data.message)
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(updateParam: UpdateParam,
                                   chosenAddress: TokoFoodChosenAddress,
                                   source: String
        ): Map<String, Any> {
            val cartParam = UpdateProductMapper.getUpdateProductParamById(
                updateParam.productList,
                chosenAddress,
                updateParam.shopId,
                source
            )
            return mapOf(PARAMS_KEY to cartParam)
        }
    }

}
