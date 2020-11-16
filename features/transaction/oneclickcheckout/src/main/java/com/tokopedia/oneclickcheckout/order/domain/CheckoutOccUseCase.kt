package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccRequest
import com.tokopedia.oneclickcheckout.order.view.model.*
import java.util.*
import javax.inject.Inject

class CheckoutOccUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) {

    suspend fun executeSuspend(param: CheckoutOccRequest): CheckoutOccData {
        val request = GraphqlRequest(QUERY, CheckoutOccGqlResponse::class.java, generateParam(param))
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<CheckoutOccGqlResponse>()
        return mapCheckoutData(response)
    }

    private fun generateParam(param: CheckoutOccRequest): Map<String, Any?> {
        return mapOf(PARAMS to param)
    }

    private fun mapCheckoutData(checkoutOccGqlResponse: CheckoutOccGqlResponse): CheckoutOccData {
        val response = checkoutOccGqlResponse.response
        return CheckoutOccData(response.status, response.header.messages.firstOrNull(),
                CheckoutOccResult(response.data.success,
                        CheckoutOccErrorData(response.data.error.code, response.data.error.imageUrl, response.data.error.message),
                        CheckoutOccPaymentParameter(response.data.paymentParameter.callbackUrl, response.data.paymentParameter.payload,
                                CheckoutOccRedirectParam(
                                        response.data.paymentParameter.redirectParam.url,
                                        response.data.paymentParameter.redirectParam.gateway,
                                        response.data.paymentParameter.redirectParam.method,
                                        response.data.paymentParameter.redirectParam.form
                                )
                        ),
                        OccPrompt(response.data.prompt.type.toLowerCase(Locale.ROOT),
                                response.data.prompt.title, response.data.prompt.description, response.data.prompt.imageUrl,
                                response.data.prompt.buttons.map {
                                    OccPromptButton(it.text, it.link, it.action.toLowerCase(Locale.ROOT), it.color.toLowerCase(Locale.ROOT))
                                }
                        )
                )
        )
    }

    companion object {
        const val PARAMS = "params"

        val QUERY = """
            mutation one_click_checkout(${"$"}params: oneClickCheckoutParams) {
              one_click_checkout(params:${"$"}params) {
                header {
                  process_time
                  reason
                  error_code
                  messages
                }
                data {
                  success
                  error {
                    code
                    image_url
                    message
                    additional_info {
                        price_validation {
                            is_updated
                            message {
                                title
                                desc
                                action
                            }
                            tracker_data {
                                product_changes_type
                                campaign_type
                                product_ids
                            }
                        }
                    }
                  }
                  payment_parameter {
                    callback_url
                    payload
                    redirect_param {
                      url
                      gateway
                      method
                      form
                    }
                  }
                  prompt {
                    type
                    title
                    description
                    image_url
                    buttons {
                     text
                     link
                     action
                     color
                    }
                  }
                }
                status
              }
            }
        """.trimIndent()
    }
}