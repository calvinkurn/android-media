package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.unsendreply.UnsendReplyResponse
import com.tokopedia.topchat.chatroom.domain.usecase.UnsendReplyUseCase.Param.Companion.MSG_ID
import com.tokopedia.topchat.chatroom.domain.usecase.UnsendReplyUseCase.Param.Companion.REPLY_IDS
import com.tokopedia.topchat.chatroom.domain.usecase.UnsendReplyUseCase.Param.Companion.REPLY_TIMES
import javax.inject.Inject

open class UnsendReplyUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UnsendReplyUseCase.Param, UnsendReplyResponse>(dispatcher.io) {

    override suspend fun execute(params: Param): UnsendReplyResponse {
        val param = generateParam(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun generateParam(param: Param): Map<String, Any> {
        return mapOf(
            MSG_ID to param.msgID,
            REPLY_IDS to param.replyIDs,
            REPLY_TIMES to param.replyTimes,
        )
    }

    override fun graphqlQuery(): String = """
        mutation unsendReply(
            $$MSG_ID: Int!,
            $$REPLY_IDS: String,
            $$REPLY_TIMES: String,
        ) {
            unsendReply(
                $MSG_ID: $$MSG_ID, 
                $REPLY_IDS: $$REPLY_IDS, 
                $REPLY_TIMES: $$REPLY_TIMES
            ) {
                success
            }
        }
        """

    class Param(
        val msgID: Long = 0,
        val replyIDs: String = "",
        val replyTimes: String = ""
    ) {
        companion object {
            const val MSG_ID = "msgID"
            const val REPLY_IDS = "replyIDs"
            const val REPLY_TIMES = "replyTimes"
        }
    }

}