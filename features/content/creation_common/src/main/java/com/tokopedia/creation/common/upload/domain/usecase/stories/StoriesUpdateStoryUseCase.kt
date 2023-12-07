package com.tokopedia.creation.common.upload.domain.usecase.stories

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryRequest
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
@GqlQuery(StoriesUpdateStoryUseCase.QUERY_NAME, StoriesUpdateStoryUseCase.QUERY)
class StoriesUpdateStoryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<StoriesUpdateStoryRequest, StoriesUpdateStoryResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = ContentCreatorStoryUpdateStoryQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: StoriesUpdateStoryRequest): StoriesUpdateStoryResponse {
        return repository.request(gqlQuery, params)
    }

    companion object {
        private const val PARAM_REQ = "req"

        const val QUERY_NAME = "ContentCreatorStoryUpdateStoryQuery"
        const val QUERY = """
            mutation contentCreatorStoryUpdateStory(
                ${"$$PARAM_REQ"}: ContentCreatorStoryUpdateStoryRequest!
            ) {
                contentCreatorStoryUpdateStory(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    storyID
                }
            }
        """
    }
}
