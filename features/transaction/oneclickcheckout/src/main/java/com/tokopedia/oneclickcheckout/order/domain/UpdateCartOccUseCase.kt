package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import javax.inject.Inject

class UpdateCartOccUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<UpdateCartOccGqlResponse>) {

    fun execute(param: UpdateCartOccRequest, onSuccess: (UpdateCartOccGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(generateParam(param))
        graphqlUseCase.setTypeClass(UpdateCartOccGqlResponse::class.java)
        graphqlUseCase.execute({ response: UpdateCartOccGqlResponse ->
            if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
                onSuccess(response)
            } else {
                onError(MessageErrorException(response.getErrorMessage() ?: DEFAULT_ERROR_MESSAGE))
            }
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    private fun generateParam(param: UpdateCartOccRequest): Map<String, Any?> {
        return mapOf(PARAM_KEY to param)
    }

    companion object {
        const val PARAM_KEY = "update"

        val QUERY = """
        mutation update_cart_occ(${"$"}update: OneClickCheckoutUpdateCartParam) {
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