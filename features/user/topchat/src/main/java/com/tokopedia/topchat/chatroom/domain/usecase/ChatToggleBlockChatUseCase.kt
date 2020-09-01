package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
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

    fun blockChat(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParams(messageId, BlockType.Personal, true)
        request(params, onSuccess, onError)
    }


    fun unBlockChat(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParams(messageId, BlockType.Personal, false)
        request(params, onSuccess, onError)
    }

    fun blockPromo(
            msgId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (isPreviousRequestRunning()) return
        val params = generateParams(msgId, BlockType.Promo, true)
        promoStateChangerJob = request(params, onSuccess, onError)
    }

    fun allowPromo(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (isPreviousRequestRunning()) return
        val params = generateParams(messageId, BlockType.Promo, false)
        promoStateChangerJob = request(params, onSuccess, onError)
    }

    private fun request(
            params: Map<String, Any>,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
    ): Job {
        return launchCatchError(dispatchers.IO,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatSettingsResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.Main) {
                        onSuccess(response)
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

    private fun generateParams(
            msgId: String,
            blockType: BlockType,
            isBlocked: Boolean
    ): Map<String, Any> {
        return mapOf(
                paramMsgId to msgId,
                paramBlockType to blockType.value,
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