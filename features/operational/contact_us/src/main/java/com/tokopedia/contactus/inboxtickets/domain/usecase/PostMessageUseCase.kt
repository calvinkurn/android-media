package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.model.TicketReplyResponse
import com.tokopedia.contactus.inboxtickets.domain.usecase.param.PostMessageParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

const val REPLAY_TICKET = """mutation replyTicket(${'$'}ticketID: String!, ${'$'}userID: String!, ${'$'}message: String!, ${'$'}pPhoto: Int!, ${'$'}pPhotoAll: String!, ${'$'}agentReply:String!) {
  ticket_reply(ticketID: ${'$'}ticketID, userID: ${'$'}userID, message: ${'$'}message, pPhoto: ${'$'}pPhoto, pPhotoAll: ${'$'}pPhotoAll, agentReply: ${'$'}agentReply) {
    data {
      status
      post_key
    }
  }
}"""

class PostMessageUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<PostMessageParam, TicketReplyResponse>(dispatchers.io) {

    fun createRequestParams(
        id: String,
        message: String,
        photo: Int,
        photoall: String,
        agentReply: String,
        userId: String
    ): PostMessageParam {
        return PostMessageParam(id, message, photo, photoall, userId, agentReply)
    }

    override fun graphqlQuery(): String {
        return REPLAY_TICKET
    }

    override suspend fun execute(params: PostMessageParam): TicketReplyResponse {
        return repository.request(graphqlQuery(), params)
    }
}
