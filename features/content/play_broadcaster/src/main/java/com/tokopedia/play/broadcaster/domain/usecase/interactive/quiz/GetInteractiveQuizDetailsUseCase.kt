package com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizDetailResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * created by andriyan on 04/20/22
 */
@GqlQuery(GetInteractiveQuizDetailsUseCase.QUERY_NAME, GetInteractiveQuizDetailsUseCase.QUERY)
class GetInteractiveQuizDetailsUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<GetInteractiveQuizDetailResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    init {
        setGraphqlQuery(GetInteractiveQuizDetailsUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetInteractiveQuizDetailResponse::class.java)
    }

    companion object {
        private const val PARAM_INTERACTIVE_ID = "interactiveId"
        const val QUERY_NAME = "GetInteractiveQuizDetailsUseCaseQuery"
        const val QUERY = """
            query GetInteractiveQuizDetailsUseCase(
                ${'$'}$PARAM_INTERACTIVE_ID: String!
            ) {
              playInteractiveGetQuizDetails(req:{
                interactiveID: ${"$$PARAM_INTERACTIVE_ID"}
              }){
                question
                reward
                countdownEnd
                choices{
                  id
                  text
                  isCorrectAnswer
                  participantCount
                }
              }
            }
        """

        fun createParams(interactiveId: String): Map<String, Any> = mapOf(
            PARAM_INTERACTIVE_ID to interactiveId
        )
    }

}