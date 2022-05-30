package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
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
): FlowUseCase<CartTokoFoodParam, CartTokoFoodResponse>(dispatchers.io) {

    private val isDebug = false

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(params: CartTokoFoodParam): Map<String, Any> {
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

    override suspend fun execute(params: CartTokoFoodParam): Flow<CartTokoFoodResponse> = flow {
        if (isDebug) {
            kotlinx.coroutines.delay(1000)
            emit(getDummyResponse(params.carts.getOrNull(0)?.productId.orEmpty()))
        } else {
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

    private fun getDummyResponse(productId: String): CartTokoFoodResponse {
        return CartTokoFoodResponse(
            status = TokoFoodCartUtil.SUCCESS_STATUS,
            data = CartTokoFoodData(
                carts = listOf(
                    CartTokoFood(
                        productId = productId
                    )
                )
            )
        )
    }

}