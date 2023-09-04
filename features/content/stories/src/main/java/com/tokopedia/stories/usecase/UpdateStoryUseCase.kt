package com.tokopedia.stories.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.uimodel.StoryActionType
import com.tokopedia.stories.usecase.response.UpdateStoryResponse
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/08/23
 */
class UpdateStoryUseCase @Inject constructor(
    private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<UpdateStoryUseCase.Param, UpdateStoryResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: Param): UpdateStoryResponse {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    data class Param(
        val storyId: String,
        val action: StoryActionType,
    ) {
        fun convertToMap(): Map<String, Any> {
            return mapOf(
                REQ_PARAM to mapOf<String, Any>(
                    STORY_ID_PARAM to storyId,
                    STATUS_PARAM to action.value,
                )
            )
        }
    }

    companion object {
        private const val REQ_PARAM = "req"
        private const val STORY_ID_PARAM = "storyID"
        private const val STATUS_PARAM = "status"

        private const val QUERY = """
            mutation updateStory(${'$'} storyId: String!, ${'$'}storyType: Int!, ${'$'}authorId: String!, ${'$'}authorType: Int!, ${'$'}action: Int!, ${'$'}mediaId: String!, ${'$'}publishedAt: String!){
                contentStoryUpdateStory (
                   $REQ_PARAM: {
                       $STORY_ID_PARAM: ${'$'}storyId,
                       $STATUS_PARAM :${'$'}action,
                   }
                ) {
                    storyID
                }
            }
        """
    }
}
