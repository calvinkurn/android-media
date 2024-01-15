package com.tokopedia.stories.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import javax.inject.Inject

class StoriesDetailsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<StoriesDetailsUseCase.Request, StoriesDetailsResponseModel>(dispatchers.io) {

    override suspend fun execute(params: Request): StoriesDetailsResponseModel {
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
            query contentStoryDetails(${"$$PARAM_REQUEST"}: ContentStoryDetailsRequest!) {
              contentStoryDetails($PARAM_REQUEST: ${"$$PARAM_REQUEST"}){
               stories{
                    id
                    slug
                    status
                    category
                    publishedAt
                    author {
                      id
                      encryptedID
                      type
                      isLive
                      hasStory
                      isUnseenStoryExist
                      name
                      badgeURL
                      thumbnailURL
                      webLink
                      appLink
                    }
                    media {
                      type
                      link
                    }
                    webLink
                    appLink
                    totalProducts
                    totalProductsFmt
                    interaction {
                      shareable
                      reportable
                      deletable
                      editable
                    }
                    meta {
                      shareTitle
                      shareDescription
                      shareTextDescription
                      shareImage
                      hasSeen
                      activityTracker
                      templateTracker
                    }
                  }
               meta{
                 selectedStoryIndex
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
    )
}
