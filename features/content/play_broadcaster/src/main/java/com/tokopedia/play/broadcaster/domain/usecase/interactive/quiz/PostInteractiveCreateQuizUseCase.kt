package com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.PostInteractiveCreateQuizResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 06, 2022
 */
@GqlQuery(PostInteractiveCreateQuizUseCase.QUERY_NAME, PostInteractiveCreateQuizUseCase.QUERY)
class PostInteractiveCreateQuizUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<PostInteractiveCreateQuizResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    init {
        setGraphqlQuery(PostInteractiveCreateQuizUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PostInteractiveCreateQuizResponse::class.java)
    }

    suspend fun execute(
        channelId: String,
        question: String,
        runningTime: Long,
        choices: List<Choice>
    ): PostInteractiveCreateQuizResponse {
        setRequestParams(
            createParams(
                channelId = channelId,
                question = question,
                runningTime = runningTime,
                choices = choices,
            )
        )
        return executeOnBackground()
    }

    data class Choice(
        val text: String,
        val correct: Boolean,
    )

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_QUESTION = "question"
        private const val PARAM_PRIZE = "prize"
        private const val PARAM_RUNNING_TIME = "runningTime"
        private const val PARAM_CHOICES = "choices"
        private const val PARAM_TEXT = "text"
        private const val PARAM_CORRECT = "correct"

        const val QUERY_NAME = "PostInteractiveCreateQuizUseCaseQuery"
        const val QUERY = """
            mutation PostInteractiveCreateQuizUseCase(
                ${"$$PARAM_CHANNEL_ID"}: String!, 
                ${"$$PARAM_QUESTION"}: String!, 
                ${"$$PARAM_RUNNING_TIME"}: Int!,
                ${"$$PARAM_CHOICES"}: [PlayInteractiveQuizChoiceInput!]!
            ) {
              playInteractiveSellerCreateQuiz(input: {
                channelID: ${"$$PARAM_CHANNEL_ID"},
                question: ${"$$PARAM_QUESTION"},
                runningTime: ${"$$PARAM_RUNNING_TIME"},
                choices: ${"$$PARAM_CHOICES"}
              }){
                meta {
                  status
                  message
                }
              }
            }
        """

        fun createParams(
            channelId: String,
            question: String,
            runningTime: Long,
            choices: List<Choice>
        ): Map<String, Any> = mapOf(
            PARAM_CHANNEL_ID to channelId,
            PARAM_QUESTION to question,
            PARAM_RUNNING_TIME to runningTime,
            PARAM_CHOICES to choices.map {
                 mapOf(
                     PARAM_TEXT to it.text,
                     PARAM_CORRECT to it.correct,
                 )
            },
        )
    }
}
