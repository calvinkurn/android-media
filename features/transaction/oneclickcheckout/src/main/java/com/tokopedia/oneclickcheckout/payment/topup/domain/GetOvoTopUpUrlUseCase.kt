package com.tokopedia.oneclickcheckout.payment.topup.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.payment.data.OvoTopUpUrlGqlResponse
import javax.inject.Inject

class GetOvoTopUpUrlUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<OvoTopUpUrlGqlResponse>) {

    fun execute(customerName: String, customerEmail: String, customerPhone: String, redirectUrl: String,
                onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        val params = mutableMapOf<String, Any>().apply {
            put(PARAM_CUSTOMER_NAME, customerName)
            put(PARAM_CUSTOMER_EMAIL, customerEmail)
            put(PARAM_CUSTOMER_PHONE, customerPhone)
            put(PARAM_REDIRECT_URL, redirectUrl)
        }
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(params)
        graphqlUseCase.setTypeClass(OvoTopUpUrlGqlResponse::class.java)
        graphqlUseCase.execute({ gqlResponse: OvoTopUpUrlGqlResponse ->
            if (gqlResponse.response.success && gqlResponse.response.data.redirectURL.isNotBlank()) {
                onSuccess(gqlResponse.response.data.redirectURL)
            } else {
                onError(MessageErrorException(gqlResponse.response.errors.firstOrNull()?.message
                        ?: DEFAULT_ERROR_MESSAGE))
            }
        }, { throwable ->
            onError(throwable)
        })
    }

    companion object {
        private const val PARAM_CUSTOMER_NAME = "customerName"
        private const val PARAM_CUSTOMER_EMAIL = "customerEmail"
        private const val PARAM_CUSTOMER_PHONE = "customerPhone"
        private const val PARAM_REDIRECT_URL = "redirectURL"

        private val QUERY = """
            query fetchInstantTopupURL(${"$"}customerName: String!, ${"$"}customerEmail: String!, ${"$"}customerPhone: String!, ${"$"}redirectURL: String!) {
                fetchInstantTopupURL(customerName: ${"$"}customerName, customerEmail: ${"$"}customerEmail, customerPhone: ${"$"}customerPhone, redirectURL: ${"$"}redirectURL) {
                    success
                    processTime
                    data {
                        redirectURL
                    }
                    errors {
                        code
                        message
                    }
                }
            }
        """.trimIndent()
    }
}