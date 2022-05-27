package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.mapper.UpdateProductMapper
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddToCartTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
): FlowUseCase<UpdateParam, CartTokoFoodResponse>(dispatcher.io) {

    private val isDebug = false

    companion object {
        private const val PARAMS_KEY = "params"


        private fun generateParams(param: UpdateParam,
                                   additionalAttr: String): Map<String, Any> {
            val cartParam = UpdateProductMapper.getProductParamById(
                param.productList,
                additionalAttr,
                param.shopId
            )
            return mapOf(PARAMS_KEY to cartParam)
        }
    }

    override fun graphqlQuery(): String = """
        mutation AddToCartTokofood($$PARAMS_KEY: ATCGeneralParams!) {
          add_to_cart_general(params: $$PARAMS_KEY) {
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
              }
            }
          }
        }
    """.trimIndent()

    // Correct Query but not yet supported in staging
//    override fun graphqlQuery(): String = """
//        mutation AddToCartTokofood($$PARAMS_KEY: ATCGeneralParams!) {
//          add_to_cart_general(params: $$PARAMS_KEY) {
//            message
//            status
//            data {
//              success
//              message
//              carts {
//                success
//                message
//                business_id
//                cart_id
//                shop_id
//                product_id
//                quantity
//              }
//              bottomsheet {
//                is_show_bottomsheet
//                title
//                description
//                buttons{
//                    text
//                    color
//                    action
//                    link
//                }
//              }
//            }
//          }
//        }
//    """.trimIndent()

    override suspend fun execute(params: UpdateParam): Flow<CartTokoFoodResponse> = flow {
        if (isDebug) {
            kotlinx.coroutines.delay(1000)
            emit(getDummyResponse())
        } else {
            val param = generateParams(
                params,
                CartAdditionalAttributesTokoFood(chosenAddressRequestHelper.getChosenAddress()).generateString()
            )
            val response =
                repository.request<Map<String, Any>, CartTokoFoodResponse>(graphqlQuery(), param)
            if (response.isSuccess()) {
                emit(response)
            } else {
                throw MessageErrorException(response.getMessageIfError())
            }
        }
    }

    private fun getDummyResponse(): CartTokoFoodResponse {
        return CartTokoFoodResponse(
            success = TokoFoodCartUtil.SUCCESS_STATUS
        )
    }

}