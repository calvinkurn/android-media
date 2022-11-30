package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.MiniCartTokoFoodResponse
import javax.inject.Inject

private const val QUERY = """
        query MiniCartTokofood(${'$'}params: cartTokofoodParams!) {
          mini_cart_tokofood(params: ${'$'}params) {
            message
            status
            data {
              shop {
                shop_id
                name
              }
              available_section {
                products {
                  cart_id
                  product_id
                  category_id
                  price
                  notes
                  quantity
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
                      option_id
                      is_selected
                      name
                      price
                      price_fmt
                    }
                  }
                }
              }
              unavailable_sections {
                products {
                  cart_id
                  product_id
                  category_id
                  price
                  notes
                  quantity
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
                      option_id
                      name
                      price
                      price_fmt
                    }
                  }
                }
              }
              summary_detail{
                total_items
                total_price
              }
            }
          }
        }
    """

@GqlQuery("MiniCartTokofood", QUERY)
class LoadCartTokoFoodUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): GraphqlUseCase<MiniCartTokoFoodResponse>(repository) {

    init {
        setTypeClass(MiniCartTokoFoodResponse::class.java)
        setGraphqlQuery(MiniCartTokofood())
    }

    suspend fun execute(source: String): CheckoutTokoFood? {
        val chosenAddress = chosenAddressRequestHelper.getChosenAddress()
        if (chosenAddress.addressId.isZero() || chosenAddress.geolocation.isBlank()) {
            return null
        }
        val additionalAttributes = CartAdditionalAttributesTokoFood(chosenAddress)
        val param = generateParams(additionalAttributes.generateString(), source)
        setRequestParams(param)
        val response = executeOnBackground()
        if (response.miniCartTokofood.isSuccess()) {
            return response.miniCartTokofood
        } else {
            throw MessageErrorException(response.miniCartTokofood.getMessageIfError())
        }
    }

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String,
                                   source: String): Map<String, Any> {
            val params = CheckoutTokoFoodParam(
                additionalAttributes = additionalAttributes,
                source = source
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

}
