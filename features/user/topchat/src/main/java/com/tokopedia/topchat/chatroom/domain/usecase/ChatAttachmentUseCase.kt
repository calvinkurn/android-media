package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChatAttachmentUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatAttachmentResponse>,
        private val mapper: ChatAttachmentMapper,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    private val paramMsgId = "msgId"
    private val paramLimit = "AttachmentIDs"

    fun safeCancel() {
        if (coroutineContext.isActive) {
            gqlUseCase.cancelJobs()
            cancel()
        }
    }

    fun getAttachments(
            msgId: Int,
            attachmentId: String,
            onSuccess: (ArrayMap<String, Attachment>) -> Unit,
            onError: (Throwable, ArrayMap<String, Attachment>) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(msgId, attachmentId)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatAttachmentResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val mapAttachment = mapper.map(response)
                    withContext(dispatchers.Main) {
                        onSuccess(mapAttachment)
                    }
                },
                {
                    val mapErrorAttachment = mapper.mapError(attachmentId)
                    withContext(dispatchers.Main) {
                        onError(it, mapErrorAttachment)
                    }
                }
        )
    }

    private fun generateParams(
            msgId: Int,
            attachmentId: String
    ): Map<String, Any> {
        return mapOf(
                paramMsgId to msgId,
                paramLimit to attachmentId
        )
    }

    val query = """
        query chatAttachments($$paramMsgId: Int!, $$paramLimit: String) {
          chatAttachments(msgId: $$paramMsgId, AttachmentIDs: $$paramLimit) {
            list {
             id
              type
              attributes
              fallback {
                message
              	html
              }
              isActual
            }
          }
        }
    """.trimIndent()
}