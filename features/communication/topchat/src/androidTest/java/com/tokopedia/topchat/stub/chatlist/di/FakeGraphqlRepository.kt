package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import timber.log.Timber

class FakeGraphqlRepository : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        Timber.d("Pass through FakeGraphql $requests")
        return when (GqlQueryParser.parse(requests).first()) {
            "notifications" -> {
                val mockedNotif = NotificationsPojo().apply {
                    notification = NotificationsPojo.Notification(
                        NotificationsPojo.Notification.Chat(19, 45)
                    )
                }
                GqlMockUtil.createSuccessResponse(mockedNotif)
            }
            else -> throw IllegalArgumentException()
        }
    }
}

sealed class Config {
    object Default : Config()
    data class WithResponse(val response: Any) : Config()
    object Error : Config()
    object Delay : Config()
}
