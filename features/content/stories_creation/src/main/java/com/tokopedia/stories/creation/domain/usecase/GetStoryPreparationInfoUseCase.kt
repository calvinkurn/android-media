package com.tokopedia.stories.creation.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.creation.model.GetStoryPreparationInfoRequest
import com.tokopedia.stories.creation.model.GetStoryPreparationInfoResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
@GqlQuery(GetStoryPreparationInfoUseCase.QUERY_NAME, GetStoryPreparationInfoUseCase.QUERY)
class GetStoryPreparationInfoUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetStoryPreparationInfoRequest, GetStoryPreparationInfoResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = ContentCreatorStoryGetPreparationInfoQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: GetStoryPreparationInfoRequest): GetStoryPreparationInfoResponse {
        return repository.request(gqlQuery, params)
    }

    companion object {
        private const val PARAM_REQ = "req"

        const val QUERY_NAME = "ContentCreatorStoryGetPreparationInfoQuery"
        const val QUERY = """
            query contentCreatorStoryGetPreparationInfo(
                ${"$$PARAM_REQ"}: ContentCreatorStoryGetPreparationInfoRequest!,
            ) {
                contentCreatorStoryGetPreparationInfo(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    config
                }
            }
        """
    }
}
