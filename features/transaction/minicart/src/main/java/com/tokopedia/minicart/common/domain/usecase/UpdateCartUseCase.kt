package com.tokopedia.minicart.common.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.request.updatecart.UpdateCartRequest
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                            private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<UpdateCartV2Data>() {

    private var params: Map<String, Any?>? = null
    private var isFromMiniCartWidget: Boolean = false

    fun setParams(miniCartItemList: List<MiniCartItem>, isFromMiniCartWidget: Boolean = false) {
        val updateCartRequestList = mutableListOf<UpdateCartRequest>()

        miniCartItemList.forEach {
            updateCartRequestList.add(
                    UpdateCartRequest().apply {
                        cartId = it.cartId
                        quantity = it.quantity
                        notes = it.notes
                    }
            )
        }

        mapParams(updateCartRequestList)
        this.isFromMiniCartWidget = isFromMiniCartWidget
    }

    fun setParamsFromUiModels(miniCartItemList: List<MiniCartProductUiModel>) {
        val updateCartRequestList = mutableListOf<UpdateCartRequest>()

        miniCartItemList.forEach {
            updateCartRequestList.add(
                    UpdateCartRequest().apply {
                        cartId = it.cartId
                        quantity = it.productQty
                        notes = it.productNotes
                    }
            )
        }

        mapParams(updateCartRequestList)
        isFromMiniCartWidget = true
    }

    private fun mapParams(updateCartRequestList: MutableList<UpdateCartRequest>) {
        params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_CARTS to updateCartRequestList,
                KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )
    }

    override suspend fun executeOnBackground(): UpdateCartV2Data {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        val request = GraphqlRequest(QUERY, UpdateCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<UpdateCartGqlResponse>()

//        val response = Gson().fromJson(MOCK_RESPONSE, UpdateCartGqlResponse::class.java)

        return if (response.updateCartData.status == "OK") {
            if (isFromMiniCartWidget) {
                response.updateCartData
            } else {
                if (response.updateCartData.data.status) {
                    response.updateCartData
                } else {
                    throw ResponseErrorException(response.updateCartData.error.joinToString(", "))
                }
            }
        } else {
            throw ResponseErrorException(response.updateCartData.error.joinToString(", "))
        }
    }

    companion object {
        val PARAM_CARTS = "carts"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"

        val QUERY = """
        mutation update_cart_v2(${'$'}carts: [ParamsCartUpdateCartV2Type], ${'$'}lang: String,  ${'$'}chosen_address: ChosenAddressParam) {
            update_cart_v2(carts: ${'$'}carts, lang: ${'$'}lang, chosen_address: ${'$'}chosen_address) {
                error_message
                status
                data {
                    error
                    status 
                    message
                    toaster_action {
                        text
                        show_cta
                    }
                    out_of_service {
                        id
                        code
                        image
                        title
                        description
                        buttons {
                          id
                          code
                          message
                          color
                        }
                    }
                }
            }
        }
        """.trimIndent()
    }

}

val MOCK_RESPONSE = """
    {
      "update_cart_v2": {
        "error_message": [],
        "status": "OK",
        "data": {
          "error": "Message error with out of service",
          "status": false,
          "message": "",
          "toaster_action": {
            "text": "Oke",
            "show_cta": true
          }
        }
      }
    }
""".trimIndent()