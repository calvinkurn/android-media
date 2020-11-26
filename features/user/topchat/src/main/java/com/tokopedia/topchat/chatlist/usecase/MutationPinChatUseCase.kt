package com.tokopedia.topchat.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.pojo.pinchat.PinChatResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MutationPinChatUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<PinChatResponse>,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun pinChat(
            msgId: String,
            onSuccess: (Boolean) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(msgId)
                    val response = gqlUseCase.apply {
                        setTypeClass(PinChatResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.Main) {
                        onSuccess(response.chatPin.success)
                    }
                },
                {
                    withContext(dispatchers.Main) {
                        onError(it)
                    }
                }
        )
    }

    private fun generateParams(msgId: String): Map<String, Any> {
        return mapOf(
                paramMsgId to arrayOf(msgId)
        )
    }

    val query = """
        mutation chatPin($$paramMsgId: [String!]!) {
          chatPin(msgIDs: $$paramMsgId) {
            success
          }
        }
    """.trimIndent()

    companion object {
        const val paramMsgId = "msgIDs"
    }
}