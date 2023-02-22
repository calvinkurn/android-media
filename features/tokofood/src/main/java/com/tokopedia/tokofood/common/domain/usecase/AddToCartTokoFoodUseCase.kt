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
          mutation AddToCartTokofood(${'$'}params: ATCGeneralParams!) {
            cart_general_add_to_cart(params: ${'$'}params) {
              message
              status
              data {
                success
                message
                business_data {
                  business_id
                  success
                  message
                  additional_grouping {
                    detail {
                      additional_group_id
                      cart_ids
                      message
                    }
                  }
                  custom_response {
                    bottomsheet {
                      is_show_bottomsheet
                      title
                      description
                      image_url
                      buttons{
                          text
                          color
                          action
                          link
                      }
                    }
                    shop {
                      shop_id
                      name
                      distance
                    }
                  }
                  cart_groups {
                    cart_group_id
                    success
                    carts {
                      cart_id
                      success
                      product_id
                      shop_id
                      quantity
                      metadata {
                        notes
                        variants {
                          variant_id
                          option_id
                        }
                      }
                      custom_response {
                        category_id
                        notes
                        name
                        description
                        image_url
                        original_price
                        original_price_fmt
                        discount_percentage
                        variants {
                          variant_id
                          name
                          rules {
                            selection_rule {
                              type
                              max_quantity
                              min_quantity
                              required
                            }
                          }
                          options {
                            is_selected
                            option_id
                            name
                            price
                            price_fmt
                            status
                          }
                        }
                        price
                        price_fmt
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
