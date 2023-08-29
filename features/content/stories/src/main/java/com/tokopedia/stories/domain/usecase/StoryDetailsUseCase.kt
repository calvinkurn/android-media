package com.tokopedia.stories.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.domain.model.detail.StoryDetailsResponseModel
import javax.inject.Inject

class StoryDetailsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<StoryRequestModel, StoryDetailsResponseModel>(dispatchers.io) {

    override suspend fun execute(params: StoryRequestModel): StoryDetailsResponseModel {
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
            query contentStoryDetails(${"$${PARAM_REQUEST}"}: ContentStoryDetailsRequest!) {
              contentStoryDetails($PARAM_REQUEST: ${"$${PARAM_REQUEST}"}){
               stories{
                    id
                    slug
                    category
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
                      activityTracker
                      templateTracker
                      shareTitle
                      shareDescription
                      shareImage
                      hasSeen
                    }
                  }
               meta{
                 selectedStoryIndex
               }
              }
            }
        """
    }
}
