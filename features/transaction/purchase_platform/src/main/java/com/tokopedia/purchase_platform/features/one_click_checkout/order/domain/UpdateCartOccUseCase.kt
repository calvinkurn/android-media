package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain


import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccResponse
import javax.inject.Inject

class UpdateCartOccUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<UpdateCartOccResponse>) {

    fun execute(param: UpdateCartRequest, onSuccess: (UpdateCartOccResponse) -> Unit, onError: (Throwable) -> Unit){
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(PARAM_KEY to param))
        graphqlUseCase.setTypeClass(UpdateCartOccResponse::class.java)
        graphqlUseCase.execute({ response: UpdateCartOccResponse ->
            if (response.response.status.equals("OK", true)) {
                if(response.response.data.success == 1) {
                    onSuccess(response)
                } else if (response.response.data.messages.isNotEmpty()) {
                    onError(MessageErrorException(response.response.data.messages[0]))
                } else {
                    onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
                }

            } else if (response.response.errorMessage.isNotEmpty()) {
                onError(MessageErrorException(response.response.errorMessage[0]))
            } else {
                onError(MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi"))
            }

        }, {throwable: Throwable ->
            onError(throwable)
        })

    }

    companion object {
        const val PARAM_KEY = "update"
        val QUERY = """
            query update_cart_occ(${"$"}update: OneClickCheckoutUpdateCartParam) {
            update_cart_occ(param: ${"$"}update) {
                error_message
                status
                data {
                    messages
                    success
                }
            }
        }
        """.trimIndent()
    }
}