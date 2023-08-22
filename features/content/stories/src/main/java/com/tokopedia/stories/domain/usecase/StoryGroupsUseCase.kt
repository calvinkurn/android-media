package com.tokopedia.stories.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.domain.model.group.StoryGroupsResponseModel
import javax.inject.Inject

class StoryGroupsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<StoryRequestModel, StoryGroupsResponseModel>(dispatchers.io) {

    override suspend fun execute(params: StoryRequestModel): StoryGroupsResponseModel {
        val request = createRequestParams(params)
        return graphqlRepository.request(graphqlQuery(), request)
    }

    private fun createRequestParams(params: StoryRequestModel): Map<String, Any> {
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
