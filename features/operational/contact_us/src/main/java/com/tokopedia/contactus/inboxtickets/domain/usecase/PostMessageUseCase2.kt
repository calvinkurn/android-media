package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.domain.StepTwoResponse
import com.tokopedia.contactus.inboxtickets.domain.usecase.param.PostMessage2Param
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

private const val TICKET_REPLY_ATTACH: String = """
    mutation ticketReplyAttach(${'$'}ticketID: String, ${'$'}userID: String!, ${'$'}postKey: String, ${'$'}fileUploaded: String) {
  ticket_reply_attach(ticketID: ${'$'}ticketID, userID: ${'$'}userID, postKey: ${'$'}postKey, fileUploaded: ${'$'}fileUploaded) {
    data {
      status
      is_success
    }
  }
}"""

class PostMessageUseCase2 @Inject internal constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<PostMessage2Param, StepTwoResponse>(dispatchers.io)  {

    fun createRequestParams(
        ticketId: String,
        userId: String,
        fileUploaded: String,
        postKey: String
    ): PostMessage2Param {
        return PostMessage2Param(userId, fileUploaded, postKey, ticketId)
    }

    override fun graphqlQuery(): String {
        return TICKET_REPLY_ATTACH
    }

    override suspend fun execute(params: PostMessage2Param): StepTwoResponse {
        return repository.request(graphqlQuery(), params)
    }
}
