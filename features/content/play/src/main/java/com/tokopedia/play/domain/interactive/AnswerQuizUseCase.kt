package com.tokopedia.play.domain.interactive

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.interactive.AnswerQuizResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 30/03/22
 */
@GqlQuery(AnswerQuizUseCase.QUERY_NAME, AnswerQuizUseCase.QUERY)
class AnswerQuizUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<AnswerQuizResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(AnswerQuizUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )
        setTypeClass(AnswerQuizResponse::class.java)
    }

    fun createParam(interactiveId: String, choiceId: String): Map<String, Any> {
        return mapOf(
            INPUT to hashMapOf(
                INTERACTIVE_ID to interactiveId,
                CHOICE_ID to choiceId
            )
        )
    }

    companion object {
        private const val INTERACTIVE_ID = "interactiveID"
        private const val CHOICE_ID = "quizChoiceID"
        private const val INPUT = "input"

        const val QUERY_NAME = "AnswerQuizUseCaseQuery"
        const val QUERY = """
            mutation playAnswerQuiz(${'$'}input: PlayInteractiveAnswerQuizRequest!){
                playInteractiveAnswerQuiz(
                    input:${'$'}input
                ) {
                    correctAnswerID
                }
            }
        """
    }
}
