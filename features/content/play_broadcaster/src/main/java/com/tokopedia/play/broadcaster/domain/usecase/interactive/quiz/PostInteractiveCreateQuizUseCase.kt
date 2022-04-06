package com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.PostInteractiveCreateSessionResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 06, 2022
 */
class PostInteractiveCreateQuizUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<PostInteractiveCreateSessionResponse>(gqlRepository, HIGH_RETRY_COUNT) {

    private val query = """
        type Choice {
            text: String!,
            correct: Boolean
        }
        
        mutation PostInteractiveCreateQuizUseCase(
            ${"$$PARAM_CHANNEL_ID"}: String!, 
            ${"$$PARAM_QUESTION"}: String!, 
            ${"$$PARAM_PRIZE"}: String!, 
            ${"$$PARAM_RUNNING_TIME"}: Long!,
            ${"$$PARAM_CHOICES"}: [Choice]!
        ) {
          playInteractiveSellerCreateQuiz(input: {
            channelID: ${"$$PARAM_CHANNEL_ID"},
            question: ${"$$PARAM_QUESTION"},
            prize: ${"$$PARAM_PRIZE"},
            runningTime: ${"$$PARAM_RUNNING_TIME"},
            choices: ${"$$PARAM_CHOICES"},
          }){
            meta {
              status
              message
            }
          }
        }
    """

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PostInteractiveCreateSessionResponse::class.java)
    }

    suspend fun execute(
        channelId: String,
        question: String,
        prize: String,
        runningTime: Long,
        choices: List<Pair<String, Boolean>>
    ): PostInteractiveCreateSessionResponse {
        setRequestParams(
            createParams(
                channelId = channelId,
                question = question,
                prize = prize,
                runningTime = runningTime,
                choices = mapChoices(choices),
            )
        )
        return executeOnBackground()
    }

    private fun mapChoices(choices: List<Pair<String, Boolean>>): List<Choice> {
        return choices.map {
            Choice(
                text = it.first,
                correct = it.second,
            )
        }
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

        fun createParams(
            channelId: String,
            question: String,
            prize: String,
            runningTime: Long,
            choices: List<Choice>
        ): Map<String, Any> = mapOf(
            PARAM_CHANNEL_ID to channelId,
            PARAM_QUESTION to question,
            PARAM_PRIZE to prize,
            PARAM_RUNNING_TIME to runningTime,
            PARAM_CHOICES to choices,
        )
    }
}