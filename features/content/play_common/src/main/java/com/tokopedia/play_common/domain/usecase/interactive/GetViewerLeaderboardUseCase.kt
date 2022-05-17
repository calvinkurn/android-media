package com.tokopedia.play_common.domain.usecase.interactive

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/04/22
 */
class GetViewerLeaderboardUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetLeaderboardSlotResponse>(gqlRepository) {

    init {
        setGraphqlQuery(QUERY)
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetLeaderboardSlotResponse::class.java)
    }

    fun createParams(channelId: String): Map<String, Any> = mapOf(
        PARAM_CHANNEL_ID to channelId
    )

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        const val QUERY = """
            query getLeaderboardViewer(${"$${PARAM_CHANNEL_ID}"}: String!){
              playInteractiveViewerGetLeaderboardWithSlot(req:{
                channelID: ${"$${PARAM_CHANNEL_ID}"}
              }){
                slots{
                  __typename
                  ... on PlayInteractiveViewerLeaderboardGiveaway{
                    title
                    winners{
                      userName: username
                      imageURL
                    }
                    otherParticipantCountText
                    otherParticipantCount
                    emptyLeaderboardCopyText
                  }
                  ... on PlayInteractiveViewerLeaderboardQuiz{
                    question
                    reward
                    user_choice: userChoice
                    choices {
                      id
                      text
                      isCorrect: isCorrectAnswer
                    }
                    winners: winner {
                      userName: username
                      imageURL
                    }
                    interactiveID
                    otherParticipantCountText
                    otherParticipantCount
                    emptyLeaderboardCopyText
                  }
                }
              }
            }
        """
    }
}