package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.MiniCartTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadCartTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
): FlowUseCase<String, CheckoutTokoFood>(dispatchers.io) {

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

    override fun graphqlQuery(): String = """
        query MiniCartTokofood($$PARAMS_KEY: cartTokofoodParams!) {
          mini_cart_tokofood(params: $$PARAMS_KEY) {
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
    """.trimIndent()

    override suspend fun execute(params: String): Flow<CheckoutTokoFood> = flow {
        val additionalAttributes = CartAdditionalAttributesTokoFood(chosenAddressRequestHelper.getChosenAddress())
        val param = generateParams(additionalAttributes.generateString(), params)
        val response =
            repository.request<Map<String, Any>, MiniCartTokoFoodResponse>(graphqlQuery(), param)
        if (response.miniCartTokofood.isSuccess()) {
            emit(response.miniCartTokofood)
        } else {
            throw MessageErrorException(response.miniCartTokofood.getMessageIfError())
        }
    }

}