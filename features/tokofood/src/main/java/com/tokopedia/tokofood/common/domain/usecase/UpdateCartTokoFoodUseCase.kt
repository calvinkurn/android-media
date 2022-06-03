package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.UpdateCartTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.mapper.UpdateProductMapper
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCartTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
): FlowUseCase<UpdateParam, CartTokoFoodResponse>(dispatchers.io) {

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(param: UpdateParam,
                                   additionalAttr: String): Map<String, Any> {
            val cartParam = UpdateProductMapper.getUpdateProductParamById(
                param.productList,
                additionalAttr,
                param.shopId
            )
            return mapOf(PARAMS_KEY to cartParam)
        }
    }

    override fun graphqlQuery(): String = """
        mutation UpdateCartTokofood($${PARAMS_KEY}: UpdateCartGeneralParams!) {
          update_cart_general(params: $${PARAMS_KEY}) {
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
                quantity
                metadata
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: UpdateParam): Flow<CartTokoFoodResponse> = flow {
        val param = generateParams(
            params,
            CartAdditionalAttributesTokoFood(chosenAddressRequestHelper.getChosenAddress()).generateString()
        )
        val response =
            repository.request<Map<String, Any>, UpdateCartTokoFoodResponse>(graphqlQuery(), param)
        if (response.cartResponse.isSuccess()) {
            emit(response.cartResponse)
        } else {
            throw MessageErrorException(response.cartResponse.getMessageIfError())
        }
    }

}