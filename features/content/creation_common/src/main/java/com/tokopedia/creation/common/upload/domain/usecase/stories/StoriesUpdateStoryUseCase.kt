package com.tokopedia.creation.common.upload.domain.usecase.stories

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryRequest
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
class StoriesUpdateStoryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<StoriesUpdateStoryRequest, StoriesUpdateStoryResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: StoriesUpdateStoryRequest): StoriesUpdateStoryResponse {
        return repository.request(graphqlQuery(), params.buildRequestParam())
    }

    companion object {
        private const val PARAM_REQ = "req"

        private const val QUERY = """
            mutation contentCreatorStoryUpdateStory(
                ${"$$PARAM_REQ"}: ContentCreatorStoryUpdateStoryRequest!,
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
