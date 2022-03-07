package com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.GetPinnedMessageResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
@GqlQuery(GetPinnedMessagesUseCase.QUERY_NAME, GetPinnedMessagesUseCase.QUERY)
class GetPinnedMessagesUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<GetPinnedMessageResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    init {
        setGraphqlQuery(GetPinnedMessagesUseCaseQuery.GQL_QUERY)
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetPinnedMessageResponse::class.java)
    }

    override suspend fun executeOnBackground(): GetPinnedMessageResponse = withContext(dispatchers.io) {
        super.executeOnBackground()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        const val QUERY_NAME = "GetPinnedMessagesUseCaseQuery"

        const val QUERY = """
            query GetBroadcasterPinnedMessage(${"$$PARAM_CHANNEL_ID"}: String!) {
                broadcasterGetPinMessages($PARAM_CHANNEL_ID: ${"$$PARAM_CHANNEL_ID"}) {
                    pinMessages {
                        ID
                        message
                        status {
                            ID
                        }
                    }
                }
            }
        """

        fun createParams(channelId: String): Map<String, Any> {
            return mapOf(
                PARAM_CHANNEL_ID to channelId
            )
        }
    }
}