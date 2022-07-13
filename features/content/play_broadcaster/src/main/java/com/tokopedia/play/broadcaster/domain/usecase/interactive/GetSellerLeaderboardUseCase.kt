package com.tokopedia.play.broadcaster.domain.usecase.interactive

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * created by andriyan on 05/18/22
 */
@GqlQuery(GetSellerLeaderboardUseCase.QUERY_NAME, GetSellerLeaderboardUseCase.QUERY)
class GetSellerLeaderboardUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<GetSellerLeaderboardSlotResponse>(
    gqlRepository,
    HIGH_RETRY_COUNT
) {

    init {
        setGraphqlQuery(GetSellerLeaderboardUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSellerLeaderboardSlotResponse::class.java)
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        const val QUERY_NAME = "GetSellerLeaderboardUseCaseQuery"
        const val QUERY = """
            query getLeaderboardSeller(${"$${PARAM_CHANNEL_ID}"}: String!){
              playInteractiveSellerGetLeaderboardWithSlot(req:{
                channelID: ${"$${PARAM_CHANNEL_ID}"}
              }){
                slots{
                  __typename
                  ... on PlayInteractiveSellerLeaderboardGiveaway{
                    title
                    winners{
                      userID
                      userName
                      imageURL
                    }
                    otherParticipantCountText
                    otherParticipantCount
                  }
                  ... on PlayInteractiveSellerLeaderboardQuiz{
                    interactiveID
                    question
                    reward
                    choices {
                        id
                        text
                        isCorrectAnswer
                        participantCount
                    }
                    winners {
                      userID
                      userName
                      imageURL
                    }
                    otherParticipantCountText
                    otherParticipantCount
                  }
                }
                config {
                  topchatMessage
                  topchatMessageQuiz
                }
              }
            }
        """

        fun createParams(channelID: String): Map<String, Any> = mapOf(
            PARAM_CHANNEL_ID to channelID,
        )
    }
}