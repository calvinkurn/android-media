package com.tokopedia.play_common.domain.usecase.interactive

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 02/07/21
 */
class GetInteractiveLeaderboardUseCase @Inject constructor(
        gqlRepository: GraphqlRepository,
        private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<GetInteractiveLeaderboardResponse>(gqlRepository) {

    private val query = """
        query GetSummaryLeaderboard(${"$$PARAM_CHANNEL_ID"}: String!) {
          playInteractiveGetSummaryLeaderboard(req: {
            channelID: ${"$$PARAM_CHANNEL_ID"}
          }) {
            data {
              title
              winner {
                userID
                userName
                imageURL
              }
              otherParticipantCountText
              otherParticipantCount
            }
            config {
                sellerMessage
                winnerMessage
                winnerDetail
                loserMessage
                loserDetail
            }
            summary {
                totalParticipant
            }
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetInteractiveLeaderboardResponse::class.java)
    }

    suspend fun execute(channelId: String): GetInteractiveLeaderboardResponse = withContext(dispatchers.io) {
        setRequestParams(createParams(channelId))
        return@withContext executeOnBackground()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        fun createParams(channelId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }
}