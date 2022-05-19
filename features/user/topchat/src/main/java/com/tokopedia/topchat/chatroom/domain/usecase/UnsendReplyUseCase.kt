package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
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
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation chatUnsendReply(
            $$MSG_ID: Int!,
            $$REPLY_IDS: String,
            $$REPLY_TIMES: String,
        ) {
            chatUnsendReply(
                $MSG_ID: $$MSG_ID, 
                $REPLY_IDS: $$REPLY_IDS, 
                $REPLY_TIMES: $$REPLY_TIMES
            ) {
                success
            }
        }
        """

    class Param(
        @SerializedName(MSG_ID)
        val msgID: Long = 0,
        @SerializedName(REPLY_IDS)
        val replyIDs: String = "",
        @SerializedName(REPLY_TIMES)
        val replyTimes: String = ""
    ) : GqlParam {
        companion object {
            const val MSG_ID = "msgId"
            const val REPLY_IDS = "replyIDs"
            const val REPLY_TIMES = "replyTimeNanos"
        }
    }

}