package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccRequest
import javax.inject.Inject

class CheckoutOccUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<CheckoutOccGqlResponse>) {

    fun execute(param: CheckoutOccRequest, onSuccess: (CheckoutOccGqlResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setTypeClass(CheckoutOccGqlResponse::class.java)
        graphqlUseCase.setRequestParams(generateParam(param))

        graphqlUseCase.execute({ checkoutOccGqlResponse: CheckoutOccGqlResponse ->
            onSuccess(checkoutOccGqlResponse)
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    private fun generateParam(param: CheckoutOccRequest): Map<String, Any?> {
        return mapOf(PARAMS to param)
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
                }
                status
              }
            }
        """.trimIndent()
    }
}