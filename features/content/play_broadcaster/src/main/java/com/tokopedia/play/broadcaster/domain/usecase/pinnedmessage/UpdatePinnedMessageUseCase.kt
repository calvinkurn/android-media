package com.tokopedia.play.broadcaster.domain.usecase.pinnedmessage

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.AddPinnedMessageResponse
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.GetPinnedMessageResponse
import com.tokopedia.play.broadcaster.domain.model.pinnedmessage.UpdatePinnedMessageResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
@GqlQuery(UpdatePinnedMessageUseCase.QUERY_NAME, UpdatePinnedMessageUseCase.QUERY)
class UpdatePinnedMessageUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<UpdatePinnedMessageResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    init {
        setGraphqlQuery(UpdatePinnedMessageUseCaseQuery.GQL_QUERY)
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UpdatePinnedMessageResponse::class.java)
    }

    override suspend fun executeOnBackground(): UpdatePinnedMessageResponse = withContext(dispatchers.io) {
        super.executeOnBackground()
    }

    companion object {
        private const val PARAM_ID = "ID"
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_TITLE = "title"
        private const val PARAM_MESSAGE = "message"
        private const val PARAM_STATUS = "status"
        const val QUERY_NAME = "UpdatePinnedMessageUseCaseQuery"

        const val QUERY = """
            mutation updateBroadcasterPinnedMessage(
                ${"$$PARAM_ID"}: Int64!,
                ${"$$PARAM_CHANNEL_ID"}: Int64!,
                ${"$$PARAM_TITLE"}: String!,
                ${"$$PARAM_STATUS"}: Int!
            ) {
                broadcasterUpdatePinMessage(
                    req: {
                        $PARAM_ID: ${"$$PARAM_ID"},
                        $PARAM_CHANNEL_ID: ${"$$PARAM_CHANNEL_ID"},
                        $PARAM_STATUS: ${"$$PARAM_STATUS"},
                        $PARAM_TITLE: ${"$$PARAM_TITLE"},
                        $PARAM_MESSAGE: ${"$$PARAM_TITLE"},
                        weight: 1
                    }
                ) {
                    ID
                }
            }
        """

        fun createParams(
            id: Long,
            channelId: Long,
            title: String,
            isActive: Boolean,
        ): Map<String, Any> {
            return mapOf(
                PARAM_ID to id,
                PARAM_CHANNEL_ID to channelId,
                PARAM_TITLE to title,
                PARAM_STATUS to if (isActive) 1 else 0,
            )
        }
    }
}