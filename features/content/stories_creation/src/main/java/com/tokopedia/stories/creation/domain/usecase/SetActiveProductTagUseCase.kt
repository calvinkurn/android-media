package com.tokopedia.stories.creation.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.creation.model.SetActiveProductTagRequest
import com.tokopedia.stories.creation.model.SetActiveProductTagResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
@GqlQuery(SetActiveProductTagUseCase.QUERY_NAME, SetActiveProductTagUseCase.QUERY)
class SetActiveProductTagUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<SetActiveProductTagRequest, SetActiveProductTagResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = ContentCreatorStorySetActiveProductTagsQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: SetActiveProductTagRequest): SetActiveProductTagResponse {
        return repository.request(gqlQuery, params)
    }

    companion object {
        private const val PARAM_REQ = "req"

        const val QUERY_NAME = "ContentCreatorStorySetActiveProductTagsQuery"
        const val QUERY = """
            mutation contentCreatorStorySetActiveProductTags(
                ${"$$PARAM_REQ"}: ContentCreatorStorySetActiveProductTagsRequest!
            ) {
                contentCreatorStorySetActiveProductTags(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    productIDs
                }
            }
        """
    }
}
