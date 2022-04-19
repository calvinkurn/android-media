package com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.AddPinnedMessageResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
@GqlQuery(AddPinnedMessageUseCase.QUERY_NAME, AddPinnedMessageUseCase.QUERY)
class AddPinnedMessageUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<AddPinnedMessageResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    init {
        setGraphqlQuery(AddPinnedMessageUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(AddPinnedMessageResponse::class.java)
    }

    override suspend fun executeOnBackground(): AddPinnedMessageResponse = withContext(dispatchers.io) {
        super.executeOnBackground()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_TITLE = "title"
        private const val PARAM_MESSAGE = "message"
        const val QUERY_NAME = "AddPinnedMessageUseCaseQuery"

        const val QUERY = """
            mutation AddBroadcasterPinnedMessage(
                ${"$$PARAM_CHANNEL_ID"}: String!,
                ${"$$PARAM_TITLE"}: String!
            ) {
                broadcasterAddPinMessages(
                    req: {
                        $PARAM_CHANNEL_ID: ${"$$PARAM_CHANNEL_ID"},
                        pinMessages: [
                            {
                                status: 1,
                                $PARAM_TITLE: ${"$$PARAM_TITLE"},
                                $PARAM_MESSAGE: ${"$$PARAM_TITLE"},
                                weight: 1
                            }
                        ]
                    }
                ) {
                    messageIDs
                }
            }
        """

        fun createParams(
            channelId: String,
            title: String,
        ): Map<String, Any> {
            return mapOf(
                PARAM_CHANNEL_ID to channelId,
                PARAM_TITLE to title,
            )
        }
    }
}