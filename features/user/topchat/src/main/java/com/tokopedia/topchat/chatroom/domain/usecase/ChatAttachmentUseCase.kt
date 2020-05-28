package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChatAttachmentUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatAttachmentResponse>,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    private val paramMsgId = "msgId"
    private val paramLimit = "AttachmentIDs"

    fun getAttachments(
            msgId: Int,
            attachmentId: String,
            onSuccess: (ArrayMap<String, Attachment>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(msgId, attachmentId)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatAttachmentResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val mapAttachment = map(response)
                    onSuccess(mapAttachment)
                },
                {
                    onError(it)
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

    private fun map(response: ChatAttachmentResponse): ArrayMap<String, Attachment> {
        val map = ArrayMap<String, Attachment>()
        for (attachment in response.chatAttachments.list) {
            map[attachment.id] = attachment
        }
        return map
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