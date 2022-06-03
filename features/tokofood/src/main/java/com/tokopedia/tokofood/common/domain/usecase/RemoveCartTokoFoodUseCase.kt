package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.RemoveCartTokoFoodResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveCartTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<RemoveCartTokoFoodParam, CartTokoFoodResponse>(dispatchers.io) {

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: RemoveCartTokoFoodParam): Map<String, Any> {
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        mutation RemoveCartTokofood($${PARAMS_KEY}: RemoveCartGeneralParams!) {
          remove_cart_general(params: $${PARAMS_KEY}) {
            message
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
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: RemoveCartTokoFoodParam): Flow<CartTokoFoodResponse> = flow {
        val param = generateParams(params)
        val response =
            repository.request<Map<String, Any>, RemoveCartTokoFoodResponse>(graphqlQuery(), param)
        if (response.cartResponse.isSuccess()) {
            emit(response.cartResponse)
        } else {
            throw MessageErrorException(response.cartResponse.getMessageIfError())
        }
    }

}