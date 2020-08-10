package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.common.util.Utils
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChatToggleBlockChatUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatSettingsResponse>,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    private val paramMsgId = "messageID"
    private val paramBlockType = "blockType"
    private val paramIsBlocked = "isBlocked"
    private var promoStateChangerJob: Job? = null

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun safeCancel() {
        if (coroutineContext.isActive) {
            gqlUseCase.cancelJobs()
            cancel()
        }
    }

    fun blockPromo(
            msgId: String,
            onSuccess: (String) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (isPreviousRequestRunning()) return
        promoStateChangerJob = launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(msgId, true)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatSettingsResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val dueDate = Utils.getDateTime(response.chatBlockResponse.chatBlockStatus.validDate)
                    withContext(dispatchers.Main) {
                        onSuccess(dueDate)
                    }
                },
                {
                    withContext(dispatchers.Main) {
                        onError(it)
                    }
                }
        )
    }

    fun allowPromo(
            messageId: String,
            onSuccess: () -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (isPreviousRequestRunning()) return
        promoStateChangerJob = launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(messageId, false)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatSettingsResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.Main) {
                        onSuccess()
                    }
                },
                {
                    withContext(dispatchers.Main) {
                        onError(it)
                    }
                }
        )
    }

    private fun isPreviousRequestRunning(): Boolean {
        return promoStateChangerJob != null && promoStateChangerJob?.isCompleted == false
    }

    private fun generateParams(msgId: String, isBlocked: Boolean): Map<String, Any> {
        return mapOf(
                paramMsgId to msgId,
                paramBlockType to BlockType.Promo.value,
                paramIsBlocked to isBlocked
        )
    }

    private val query = """
        mutation chatToggleBlockChat($$paramMsgId: String!, $$paramBlockType: String!, $$paramIsBlocked: Boolean!){
          chatToggleBlockChat(messageID:$$paramMsgId, blockType: $$paramBlockType, isBlocked: $$paramIsBlocked){
        		success
        		block_status{
        			is_blocked
        			is_promo_blocked
        			blocked_until
        		}
        	}
        }
    """.trimIndent()

    enum class BlockType(val value: String) {
        Personal("1"),
        Promo("2")
    }

}