package com.tokopedia.stories.internal.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.internal.model.StoriesGroupsResponseModel
import javax.inject.Inject

class StoriesGroupsUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<StoriesGroupsUseCase.Request, StoriesGroupsResponseModel>(dispatchers.io) {

    override suspend fun execute(params: Request): StoriesGroupsResponseModel {
        val request = createRequestParams(params)
        return graphqlRepository.request(graphqlQuery(), request)
    }

    private fun createRequestParams(params: Request): Map<String, Any> {
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
                    author {
                        hasStory
                        isUnseenStoryExist
                    }
                  }
                meta{
                  selectedGroupIndex
                }
              }
            }
        """
    }

    data class Request(
        @SerializedName("authorID")
        val authorID: String,
        @SerializedName("authorType")
        val authorType: String,
        @SerializedName("source")
        val source: String,
        @SerializedName("sourceID")
        val sourceID: String,
        @SerializedName("entrypoint")
        val entryPoint: String,
        @SerializedName("limit")
        val limit: Int = 20,
        @SerializedName("cursor")
        val cursor: String = "",
    )
}
