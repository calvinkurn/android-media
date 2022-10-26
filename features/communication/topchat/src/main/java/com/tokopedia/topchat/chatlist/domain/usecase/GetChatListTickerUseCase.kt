package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatlist.domain.pojo.chatlistticker.ChatListTickerResponse
import javax.inject.Inject

class GetChatListTickerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
): CoroutineUseCase<Unit, ChatListTickerResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query getChatListTicker {
             chatlistTicker {
                tickerBuyer {
                  tickerType
                  message
                  enable
                }
                tickerSeller {
                  tickerType
                  message
                  enable
                }
            }
        }
        """.trimIndent()

    override suspend fun execute(params: Unit): ChatListTickerResponse {
        return repository.request(graphqlQuery(), params)
    }

}
