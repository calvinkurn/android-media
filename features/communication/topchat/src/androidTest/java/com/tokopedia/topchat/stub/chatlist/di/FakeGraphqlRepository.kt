package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.stub.chatlist.data.GqlResponseStub
import com.tokopedia.topchat.stub.chatlist.data.ResponseStub
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
            GqlResponseStub.blastSellerMetaDataResponse.query -> {
                shouldThrow(GqlResponseStub.blastSellerMetaDataResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.blastSellerMetaDataResponse.responseObject
                )
            }
            else -> throw IllegalArgumentException()
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}

sealed class Config {
    object Default : Config()
    data class WithResponse(val response: Any) : Config()
    object Error : Config()
    object Delay : Config()
}
