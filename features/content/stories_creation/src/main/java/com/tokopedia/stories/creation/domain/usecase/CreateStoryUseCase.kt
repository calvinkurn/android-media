package com.tokopedia.stories.creation.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.creation.model.CreateStoryRequest
import com.tokopedia.stories.creation.model.CreateStoryResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
@GqlQuery(CreateStoryUseCase.QUERY_NAME, CreateStoryUseCase.QUERY)
class CreateStoryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<CreateStoryRequest, CreateStoryResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = ContentCreatorStoryCreateStoryQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: CreateStoryRequest): CreateStoryResponse {
        return repository.request(gqlQuery, params)
    }

    companion object {
        private const val PARAM_REQ = "req"

        const val QUERY_NAME = "ContentCreatorStoryCreateStoryQuery"
        const val QUERY = """
            mutation contentCreatorStoryCreateStory(
                ${"$$PARAM_REQ"}: contentCreatorStoryCreateStoryRequest!
            ) {
                contentCreatorStoryCreateStory(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    storyID
                }
            }
        """
    }
}
