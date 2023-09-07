package com.tokopedia.stories.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.domain.model.track.StoriesTrackActivityResponseModel
import javax.inject.Inject

class StoriesTrackActivityUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<StoriesTrackActivityRequestModel, StoriesTrackActivityResponseModel>(dispatchers.io) {

    override suspend fun execute(params: StoriesTrackActivityRequestModel): StoriesTrackActivityResponseModel {
        val request = createRequestParams(params)
        return graphqlRepository.request(graphqlQuery(), request)
    }

    private fun createRequestParams(params: StoriesTrackActivityRequestModel): Map<String, Any> {
        return mapOf(PARAM_REQUEST to params)
    }

    companion object {
        private const val PARAM_REQUEST = "request"
    }

    override fun graphqlQuery(): String {
        return """
            query contentStoryDetails(${"$${PARAM_REQUEST}"}: ContentTrackActivityRequest!) {
              mutation {
                contentTrackActivity(${PARAM_REQUEST}: ${"$${PARAM_REQUEST}"}){
                  success
                }
              }
            }
        """
    }
}
