package com.tokopedia.stories.creation.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.creation.model.SetActiveProductTagRequest
import com.tokopedia.stories.creation.model.SetActiveProductTagResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
class SetActiveProductTagUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<SetActiveProductTagRequest, SetActiveProductTagResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: SetActiveProductTagRequest): SetActiveProductTagResponse {
        return repository.request(graphqlQuery(), params.buildRequestParam())
    }

    companion object {
        private const val PARAM_REQ = "req"

        private const val QUERY = """
            mutation contentCreatorSetActiveProductTag(
                ${"$$PARAM_REQ"}: contentCreatorSetActiveProductTagRequest!,
            ) {
                contentCreatorSetActiveProductTag(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    productIDs
                }
            }
        """
    }
}
