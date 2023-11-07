package com.tokopedia.product.addedit.description.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.description.domain.model.GetYoutubeVideoSnippetResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetYoutubeVideoSnippetUseCase @Inject constructor(
    private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<GetYoutubeVideoSnippetResponse>() {

    companion object {
        const val SQUAD = "Squad"
        const val USECASE = "Usecase"
        const val VIDEO_IDS = "VideoIDs"
        const val PRODUCT_ADD_VALUE = "product_add"
        const val PRODUCT_EDIT_VALUE = "product_edit"
        const val ANDROID_CONSUMER_VALUE = "android consumer"
        const val ANDROID_SELLERAPP_VALUE = "android sellerapp"

        const val QUERY = """
            query getYoutubeVideoSnippet(${'$'}videoId: [String], ${'$'}source: SourceType!) {
              GetYoutubeVideoSnippet(req: {VideoIDs: ${'$'}videoId, Source: ${'$'}source}) {
                Items {
                  ID
                  Snippet {
                    Title
                    Description
                    Thumbnails {
                      Default {
                        URL
                        Width
                        Height
                      }
                    }
                    PublishedAt
                    ChannelTitle
                  }
                  ContentDetails {
                    Dimension
                    Definition
                    LicensedContent
                    Projection
                    ContentRating {
                      YtRating
                    }
                  }
                }
              }
            }"""

        fun createParams(
            videoIds: List<String>,
            squad: String,
            useCase: String
        ): RequestParams = RequestParams.create().apply {
            putString(SQUAD, squad)
            putString(USECASE, useCase)
            putObject(VIDEO_IDS, videoIds)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GetYoutubeVideoSnippetResponse {
        val getYoutubeSnippet = GraphqlRequest(QUERY, GetYoutubeVideoSnippetResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(getYoutubeSnippet)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GetYoutubeVideoSnippetResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData(GetYoutubeVideoSnippetResponse::class.java)
            }
        } else {
            throw MessageErrorException(
                error.mapNotNull {
                    it.message
                }.joinToString(separator = ", ")
            )
        }
    }


}
