package com.tokopedia.stories.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.uimodel.StoryActionType
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.uimodel.StoryType
import com.tokopedia.stories.usecase.response.UpdateStoryResponse
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/08/23
 */
class UpdateStoryUseCase @Inject constructor(
    private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Param, UpdateStoryResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: Param): UpdateStoryResponse {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    companion object {
        const val REQ_PARAM = "req"
        const val STORY_ID_PARAM = "storyID"
        const val STORY_TYPE_PARAM = "storyType"
        const val AUTHOR_ID_PARAM = "authorID"
        const val AUTHOR_TYPE_PARAM = "authorType"
        const val STATUS_PARAM = "status"
        const val MEDIA_ID_PARAM = "activeMediaID"
        const val PUBLISHED_AT_PARAM = "publishedAt"

        //TODO() wait finalized contract
        private const val QUERY = """
            mutation updateStory(${'$'} storyId: String!, ${'$'}storyType: Int!, ${'$'}authorId: String!, ${'$'}authorType: Int!, ${'$'}action: Int!, ${'$'}mediaId: String!, ${'$'}publishedAt: String!){
                contentStoryUpdateStory (
                   $REQ_PARAM: {
                       $STORY_ID_PARAM: ${'$'}storyId,
                       $STORY_TYPE_PARAM :${'$'}storyType,
                       $AUTHOR_ID_PARAM : ${'$'}authorId,
                       $AUTHOR_TYPE_PARAM: ${'$'}authorType,
                       $STATUS_PARAM :${'$'}action,
                       $MEDIA_ID_PARAM : ${'$'}mediaId,
                       $PUBLISHED_AT_PARAM : ${'$'}publishedAt
                   }
                ) {
                    storyID
                }
            }
        """
    }
}

data class Param(
    val storyId: String,
    val storyType: StoryType,
    val storyAuthor: StoryAuthor,
    val action: StoryActionType,
    val mediaId: String,
    val publishedAt: String,
) {
    fun convertToMap(): Map<String, Any> =
        mapOf(
            UpdateStoryUseCase.REQ_PARAM to mapOf(
                UpdateStoryUseCase.STORY_ID_PARAM to storyId,
                UpdateStoryUseCase.STORY_TYPE_PARAM to storyType.value,
                UpdateStoryUseCase.AUTHOR_ID_PARAM to storyAuthor.id,
                UpdateStoryUseCase.AUTHOR_TYPE_PARAM to storyAuthor.type.value,
                UpdateStoryUseCase.STATUS_PARAM to action.value,
                UpdateStoryUseCase.MEDIA_ID_PARAM to mediaId,
                UpdateStoryUseCase.PUBLISHED_AT_PARAM to publishedAt,
            )
        )
}
