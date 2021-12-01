package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import javax.inject.Inject

class ChatBackgroundUseCaseNew @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Unit, ChatBackgroundResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
            query chatBackground{
              chatBackground{
                urlImage
                urlImageDarkMode
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): ChatBackgroundResponse {
        return repository.request(graphqlQuery(), params)
    }

}