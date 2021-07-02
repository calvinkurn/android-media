package com.tokopedia.play_common.domain.usecase.interactive

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse

/**
 * Created by jegul on 02/07/21
 */
class GetInteractiveLeaderboardUseCase(
        gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetInteractiveLeaderboardResponse>(gqlRepository) {

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
            }
            config {
                sellerMessage
                winnerMessage
                winnerDetail
                loserMessage
                loserDetail
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

    suspend fun execute(channelId: String): GetInteractiveLeaderboardResponse {
        setRequestParams(createParams(channelId))
        return executeOnBackground()
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        fun createParams(channelId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }
}