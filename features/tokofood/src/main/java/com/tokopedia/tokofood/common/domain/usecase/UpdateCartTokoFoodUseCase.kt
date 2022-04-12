package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCartTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<CartTokoFoodParam, CartTokoFoodResponse>(dispatchers.io) {

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: CartTokoFoodParam): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        query UpdateCartTokofood($${PARAMS_KEY}: CartTokoFoodParam!) {
          update_cart_general(params: $${PARAMS_KEY}) {
            error_messages
            status
            data {
              success
              message
              carts {
                success
                message
                business_id
                cart_id
                shop_id
                product_id
                quantity
                metadata
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: CartTokoFoodParam): Flow<CartTokoFoodResponse> = flow {
        val param = generateParams(params)
        val response =
            repository.request<Map<String, Any>, CartTokoFoodResponse>(graphqlQuery(), param)
        emit(response)
    }

}