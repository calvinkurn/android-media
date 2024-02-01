package com.tokopedia.feedplus.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.data.GetContentWidgetRecommendationResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetContentWidgetRecommendationUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetContentWidgetRecommendationUseCase.Param, GetContentWidgetRecommendationResponse>(dispatchers.io) {

    private val queryObject = GetContentWidgetRecommendationUseCaseQuery()

    @GqlQuery(QUERY_NAME, QUERY)
    override fun graphqlQuery(): String {
        return queryObject.getQuery()
    }

    override suspend fun execute(params: Param): GetContentWidgetRecommendationResponse {
        return graphqlRepository.request(queryObject, params)
    }

    companion object {
        private const val PARAM_REQ = "request"

        const val QUERY_NAME = "GetContentWidgetRecommendationUseCaseQuery"
        const val QUERY = """
            query getContentWidgetRecommendation(
                ${"$$PARAM_REQ"}: ContentWidgetRecommendationRequest!
            ) {
                contentWidgetRecommendation(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    data {
                        __typename
                        ... on ContentWidgetAuthor {
                            contentID {
                                id
                                origin
                            }
                            viewsFmt
                            appLink
                            author {
                                id
                                name
                                thumbnailURL
                                appLink
                            }
                            media {
                                type
                                link
                                coverURL
                            }
                        }
                        ... on ContentWidgetBanner {
                            title
                            appLink
                            media {
                                type
                                link
                                coverURL
                            }
                        }
                    }
                }
            }
        """
    }

    data class Param(
        @SerializedName(PARAM_REQ)
        val req: Request
    ) : GqlParam {

        data class Request(
            @SerializedName("pagename")
            val pageName: String
        )

        companion object {
            fun create(pagename: String): Param {
                return Param(Request(pagename))
            }
        }
    }
}
