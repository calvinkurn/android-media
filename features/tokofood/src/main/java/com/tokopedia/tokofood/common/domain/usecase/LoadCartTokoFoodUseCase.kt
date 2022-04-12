package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadCartTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<CheckoutTokoFoodParam, CheckoutTokoFoodResponse>(dispatchers.io) {

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: CheckoutTokoFoodParam): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        query LoadCartTokofood($$PARAMS_KEY: CheckoutTokoFoodParam!) {
          checkout_page_tokofood(params: $$PARAMS_KEY) {
            message
            status
            data {
              shop {
                shop_id
              }
              available_section {
                products {
                  cart_id
                  product_id
                  price
                  notes
                  variants {
                    variant_id
                    name
                    options {
                      option_id
                      name
                      price
                    }
                  }
                }
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: CheckoutTokoFoodParam): Flow<CheckoutTokoFoodResponse> = flow {
        val param = generateParams(params)
        val response =
            repository.request<Map<String, Any>, CheckoutTokoFoodResponse>(graphqlQuery(), param)
        emit(response)
    }

}