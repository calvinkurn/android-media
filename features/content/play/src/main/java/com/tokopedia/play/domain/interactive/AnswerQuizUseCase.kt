package com.tokopedia.play.domain.interactive

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.interactive.AnswerQuizResponse
import javax.inject.Inject

/**
 * @author by astidhiyaa on 30/03/22
 */
@GqlQuery(AnswerQuizUseCase.QUERY_NAME, AnswerQuizUseCase.QUERY)
class AnswerQuizUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<AnswerQuizResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(AnswerQuizUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(AnswerQuizResponse::class.java)
    }

    companion object {
        private const val INTERACTIVE_ID = "interactiveID"
        private const val CHOICE_ID = "quizChoiceID"

        const val QUERY_NAME = "AnswerQuizUseCaseQuery"
        const val QUERY = """
            mutation playAnswerQuiz${'$'}$INTERACTIVE_ID: String, ${'$'}$CHOICE_ID: String){
                playInteractiveAnswerQuiz(
                    req: {
                        $INTERACTIVE_ID: ${'$'}$INTERACTIVE_ID,
                        $CHOICE_ID: ${'$'}$CHOICE_ID,
                    }
                ) {
                    correctAnswerID
                }
            }
        """

        fun createParam(interactiveId: String, choiceId: String): HashMap<String, Any> {
            return hashMapOf(
                INTERACTIVE_ID to interactiveId,
                CHOICE_ID to choiceId
            )
        }
    }
}
