package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.order.data.get.OccPromptResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OccPromptButton
import com.tokopedia.oneclickcheckout.order.view.model.OccToasterAction
import com.tokopedia.oneclickcheckout.order.view.model.OccUIMessage
import javax.inject.Inject

class UpdateCartOccUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                               private val chosenAddressRequestHelper: ChosenAddressRequestHelper) {

    suspend fun executeSuspend(param: UpdateCartOccRequest): OccUIMessage? {
        val request = GraphqlRequest(QUERY, UpdateCartOccGqlResponse::class.java, generateParam(param))
        val response = graphqlRepository.response(listOf(request)).getSuccessData<UpdateCartOccGqlResponse>()
        if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
            return null
        }
        val prompt = mapPrompt(response.response.data.prompt)
        if (prompt.shouldShowPrompt()) {
            return prompt
        }
        if (response.response.data.toasterAction.showCta) {
            return OccToasterAction(response.getErrorMessage()
                    ?: DEFAULT_ERROR_MESSAGE, response.response.data.toasterAction.text)
        }
        throw MessageErrorException(response.getErrorMessage() ?: DEFAULT_ERROR_MESSAGE)
    }

    private fun generateParam(param: UpdateCartOccRequest): Map<String, Any?> {
        if (param.chosenAddress == null) {
            param.chosenAddress = chosenAddressRequestHelper.getChosenAddress()
        }
        return mapOf(PARAM_KEY to param)
    }

    private fun mapPrompt(promptResponse: OccPromptResponse): OccPrompt {
        return OccPrompt(promptResponse.type.lowercase(), promptResponse.title,
                promptResponse.description, promptResponse.imageUrl, promptResponse.buttons.map {
            OccPromptButton(it.text, it.link, it.action.lowercase(), it.color.lowercase())
        })
    }

    companion object {
        const val PARAM_KEY = "param"

        val QUERY = """
        mutation update_cart_occ_multi(${"$"}param: OneClickCheckoutMultiUpdateCartParam) {
            update_cart_occ_multi(param: ${"$"}param) {
                error_message
                status
                data {
                    messages
                    success
                    toaster_action {
                        text
                        show_cta
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
            }
        }
        """.trimIndent()
    }
}