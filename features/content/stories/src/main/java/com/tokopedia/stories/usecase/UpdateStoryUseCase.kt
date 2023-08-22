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
) : CoroutineUseCase<UpdateStoryUseCase.ReqParam, UpdateStoryResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: ReqParam): UpdateStoryResponse {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    data class ReqParam(
        val storyId: String,
        val storyType: StoryType,
        val storyAuthor: StoryAuthor,
        val action: StoryActionType,
        val mediaId: String,
        val publishedAt: String,
    ) {
        fun convertToMap(): Map<String, Any> =
            mapOf(
                REQ_PARAM to mapOf(
                    STORY_ID_PARAM to storyId,
                    STORY_TYPE_PARAM to storyType.value,
                    AUTHOR_ID_PARAM to storyAuthor.id,
                    AUTHOR_TYPE_PARAM to storyAuthor.type.value,
                    STATUS_PARAM to action.value,
                    MEDIA_ID_PARAM to mediaId,
                    PUBLISHED_AT_PARAM to publishedAt,
                )
            )
    }

    companion object {
        private const val REQ_PARAM = "req"
        private const val STORY_ID_PARAM = "storyID"
        private const val STORY_TYPE_PARAM = "storyType"
        private const val AUTHOR_ID_PARAM = "authorID"
        private const val AUTHOR_TYPE_PARAM = "authorType"
        private const val STATUS_PARAM = "status"
        private const val MEDIA_ID_PARAM = "activeMediaID"
        private const val PUBLISHED_AT_PARAM = "publishedAt"

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
