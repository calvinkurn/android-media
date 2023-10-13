package com.tokopedia.stories.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import javax.inject.Inject

class StoriesGroupsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<StoriesRequestModel, StoriesGroupsResponseModel>(dispatchers.io) {

    override suspend fun execute(params: StoriesRequestModel): StoriesGroupsResponseModel {
        val request = createRequestParams(params)
        return graphqlRepository.request(graphqlQuery(), request)
    }

    private fun createRequestParams(params: StoriesRequestModel): Map<String, Any> {
        return mapOf(PARAM_REQUEST to params)
    }

    companion object {
        private const val PARAM_REQUEST = "request"
    }

    override fun graphqlQuery(): String {
        return """
            query ContentStoryGroups(${"$${PARAM_REQUEST}"}: ContentStoryGroupsRequest!) {
              contentStoryGroups($PARAM_REQUEST: ${"$${PARAM_REQUEST}"}){
               groups{
                    type
                    value
                    name
                    image
                    appLink
                    webLink
                  }
                meta{
                  selectedGroupIndex
                }
              }
            }
        """
    }
}
