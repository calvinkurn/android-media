package com.tokopedia.creation.common.upload.domain.usecase.stories

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesAddMediaRequest
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesAddMediaResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
class StoriesAddMediaUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<StoriesAddMediaRequest, StoriesAddMediaResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: StoriesAddMediaRequest): StoriesAddMediaResponse {
        return repository.request(graphqlQuery(), params.buildRequestParam())
    }

    companion object {
        private const val PARAM_REQ = "req"

        private const val QUERY = """
            mutation contentCreatorStoryAddMedia(
                ${"$$PARAM_REQ"}: ContentCreatorStoryAddMediaRequest!
            ) {
                contentCreatorStoryAddMedia(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    mediaID
                }
            }
        """
    }
}
