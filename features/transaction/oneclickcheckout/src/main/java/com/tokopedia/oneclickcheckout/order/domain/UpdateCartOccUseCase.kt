package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.order.data.get.OccPromptResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OccPromptButton
import java.util.*
import javax.inject.Inject

class UpdateCartOccUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val chosenAddressRequestHelper: ChosenAddressRequestHelper) {

    suspend fun executeSuspend(param: UpdateCartOccRequest): OccPrompt? {
        val request = GraphqlRequest(QUERY, UpdateCartOccGqlResponse::class.java, generateParam(param))
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<UpdateCartOccGqlResponse>()
        if (response.response.status.equals(STATUS_OK, true) && response.response.data.success == 1) {
            return null
        }
        val prompt = mapPrompt(response.response.data.prompt)
        if (prompt.shouldShowPrompt()) {
            return prompt
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
        return OccPrompt(promptResponse.type.toLowerCase(Locale.ROOT), promptResponse.title,
                promptResponse.description, promptResponse.imageUrl, promptResponse.buttons.map {
            OccPromptButton(it.text, it.link, it.action.toLowerCase(Locale.ROOT), it.color.toLowerCase(Locale.ROOT))
        })
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