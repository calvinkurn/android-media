package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.response.ATCTokofoodResponse
import com.tokopedia.tokofood.common.domain.response.CartGeneralAddToCartDataData
import com.tokopedia.tokofood.common.presentation.mapper.UpdateProductMapper
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import javax.inject.Inject

private const val QUERY = """
          mutation AddToCartTokofood(${'$'}params: CartGeneralAddToCartParams) {
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
                        custom_response
                      }
                    }
                  }
                }
              }
            }
          }
        """

@GqlQuery("AddToCartTokofood", QUERY)
class AddToCartTokoFoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<ATCTokofoodResponse>(repository) {

    init {
        setTypeClass(ATCTokofoodResponse::class.java)
        setGraphqlQuery(AddToCartTokofood())
    }

    suspend fun execute(params: UpdateParam): CartGeneralAddToCartDataData {
        val param = generateParams(
            params,
            chosenAddressRequestHelper.getChosenAddress()
        )
        setRequestParams(param)

        val response = executeOnBackground()
        if (response.isSuccess() || response.cartGeneralAddToCart.data.data.getIsShowBottomSheet()) {
            return response.cartGeneralAddToCart.data.data
        } else {
            throw MessageErrorException(response.cartGeneralAddToCart.data.message)
        }

    }

    companion object {

        private const val PARAMS_KEY = "params"

        private fun generateParams(param: UpdateParam,
                                   chosenAddress: TokoFoodChosenAddress): Map<String, Any> {
            val cartParam = UpdateProductMapper.getAtcProductParamById(
                param.productList,
                chosenAddress,
                param.shopId
            )
            return mapOf(PARAMS_KEY to cartParam)
        }

    }

}
