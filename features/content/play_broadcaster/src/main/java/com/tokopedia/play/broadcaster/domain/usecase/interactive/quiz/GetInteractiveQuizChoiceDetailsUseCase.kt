package com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveQuizChoiceDetailResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * created by andriyan on 05/10/22
 */
@GqlQuery(GetInteractiveQuizChoiceDetailsUseCase.QUERY_NAME, GetInteractiveQuizChoiceDetailsUseCase.QUERY)
class GetInteractiveQuizChoiceDetailsUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<GetInteractiveQuizChoiceDetailResponse>(
    gqlRepository,
    HIGH_RETRY_COUNT
) {

    init {
        setGraphqlQuery(GetInteractiveQuizChoiceDetailsUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetInteractiveQuizChoiceDetailResponse::class.java)
    }

    companion object {
        private const val PARAM_CHOICE_ID = "choiceId"
        private const val PARAM_CURSOR = "cursor"
        const val QUERY_NAME = "GetInteractiveQuizChoiceDetailsUseCaseQuery"
        const val QUERY = """
            query GetInteractiveQuizChoiceDetailsUseCase(
                ${'$'}$PARAM_CHOICE_ID: String!,
                ${'$'}$PARAM_CURSOR: String!
            ) {
              playInteractiveGetChoiceDetails(req:{
                choiceID: ${"$${PARAM_CHOICE_ID}"},
                cursor: ${"$${PARAM_CURSOR}"}
              }){
                cursor
                choice{
                  id
                  text
                  isCorrectAnswer
                  participantCount
                }
                winners{
                  id
                  firstName
                  imageURL
                }
                participants{
                  id
                  firstName
                  imageURL
                }
              }
            }
        """

        fun createParams(choiceId: String, cursor:String = ""): Map<String, Any> = mapOf(
            PARAM_CHOICE_ID to choiceId,
            PARAM_CURSOR to cursor
        )
    }

}