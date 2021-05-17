package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.minicart.common.data.request.localizationchooseaddress.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.data.request.localizationchooseaddress.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.minicart.common.data.request.updatecart.UpdateCartRequest
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.minicart.common.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.RuntimeException
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                            private val chosenAddressRequestHelper: ChosenAddressRequestHelper) : UseCase<UpdateCartV2Data>() {

    // Todo : set params
    var requestParams: RequestParams? = null

    override suspend fun executeOnBackground(): UpdateCartV2Data {
        if (requestParams == null) {
            throw RuntimeException("Request Params has not been initialized!")
        }

        val paramUpdateList = requestParams?.getObject(PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>

        val params = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_CARTS to paramUpdateList,
                KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
        )

        val request = GraphqlRequest(QUERY, UpdateCartGqlResponse::class.java, params)
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<UpdateCartGqlResponse>()

        if (response.updateCartData.status == "OK" && response.updateCartData.data.status) {
            return response.updateCartData
        } else {
            throw ResponseErrorException(response.updateCartData.error.joinToString(", "))
        }
    }

    companion object {
        val PARAM_UPDATE_CART_REQUEST = "PARAM_UPDATE_CART_REQUEST"
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