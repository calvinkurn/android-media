package com.tokopedia.topchat.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.pojo.unpinchat.UnpinChatResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MutationUnpinChatUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<UnpinChatResponse>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun unpinChat(msgId: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io,
                {
                    val params = generateParams(msgId)
                    val response = gqlUseCase.apply {
                        setTypeClass(UnpinChatResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.main) {
                        onSuccess(response.chatUnpin.success)
                    }
                },
                {
                    withContext(dispatchers.main) {
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
        mutation chatUnpin($$paramMsgId: [String!]!) {
          chatUnpin(msgIDs: $$paramMsgId) {
            success
          }
        }
    """.trimIndent()

    companion object {
        const val paramMsgId = "msgIDs"
    }

}