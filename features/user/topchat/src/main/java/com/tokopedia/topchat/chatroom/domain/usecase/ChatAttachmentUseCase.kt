package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class ChatAttachmentUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatAttachmentResponse>,
        private val mapper: ChatAttachmentMapper,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    private val paramMsgId = "msgId"
    private val paramLimit = "AttachmentIDs"

    fun safeCancel() {
        if (coroutineContext.isActive) {
            gqlUseCase.cancelJobs()
            cancel()
        }
    }

    fun getAttachments(
            msgId: Long,
            attachmentId: String,
            onSuccess: (ArrayMap<String, Attachment>) -> Unit,
            onError: (Throwable, ArrayMap<String, Attachment>) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val params = generateParams(msgId, attachmentId)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatAttachmentResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val mapAttachment = mapper.map(response)
                    withContext(dispatchers.main) {
                        onSuccess(mapAttachment)
                    }
                },
                {
                    val mapErrorAttachment = mapper.mapError(attachmentId)
                    withContext(dispatchers.main) {
                        onError(it, mapErrorAttachment)
                    }
                }
        )
    }

    private fun generateParams(
            msgId: Long,
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