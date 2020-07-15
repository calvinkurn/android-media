package com.tokopedia.editshipping.domain

import com.tokopedia.editshipping.domain.response.ValidateShippingResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class ValidateShippingUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<ValidateShippingResponse>) {

    fun execute (onSuccess: (ValidateShippingResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
//        graphqlUseCase.setRequestParams()
        graphqlUseCase.setTypeClass(ValidateShippingResponse::class.java)
        graphqlUseCase.execute({ response: ValidateShippingResponse ->
            if(response.response.status.equals("OK", true)) {
                onSuccess(response)
            } else {
                onError(MessageErrorException("Error"))
            }
        }, {
            throwable: Throwable -> onError(throwable)
        })
    }

    companion object{
        val QUERY = """
            query shippingEditorMobilePopup(${"$"}inputShippingEditorMobilrPopup : KeroShippingEditorMobilePopupInput!) {
              kero_shipping_editor_mobile_popup(input: ${"$"}inputShippingEditorMobilePopup ) {
                status
                config
                server_process_time
                message_status
                data {
                  show_popup
                  ticker_title
                  ticker_content
                  popup_content
                }
              }
            }
        """.trimIndent()
    }
}
