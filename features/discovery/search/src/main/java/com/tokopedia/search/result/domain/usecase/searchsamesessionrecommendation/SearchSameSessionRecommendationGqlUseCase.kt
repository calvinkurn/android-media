package com.tokopedia.search.result.domain.usecase.searchsamesessionrecommendation

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SearchSameSessionRecommendationGqlUseCase(
    private val graphqlUseCase: GraphqlUseCase,
) : UseCase<SearchSameSessionRecommendationModel>() {
    companion object {
        private const val SAME_SEARCH_RECOMMENDATION_QUERY = """
            query searchSameSessionRecommendation(${'$'}params: String!) {
                searchSameSessionRecommendation(params: ${'$'}params) {
                    data {
                        title
                        component_id
                        tracking_option
                        products {
                            id
                            name
                            price
                            price_str
                            image_url
                            rating
                            count_review
                            url
                            applink
                            description
                            rating_average
                            component_id
                            label_groups {
                              title
                              type
                              position
                              url
                            }
                            original_price
                            discount_percentage
                            label
                            discount
                            badges {
                              title
                              image_url
                              show
                            }
                            shop {
                                name
                                city
                            }
                            freeOngkir {
                                isActive
                                image_url
                            }
                            ads {
                                id
                                productClickUrl
                                productWishlistUrl
                                productViewUrl
                            }
                            customvideo_url
                            bundle_id
                        }
                        feedback {
                            title
                            component_id
                            tracking_option
                            data {
                                name
                                applink
                                url
                                image_url
                                component_id
                                title_on_click
                                message_on_click
                            }
                        }
                    }
                }
            }
        """
    }

    override fun createObservable(
        requestParams: RequestParams
    ): Observable<SearchSameSessionRecommendationModel> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createSameSessionRecommendationRequest(requestParams))

        return graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map(::getSameSessionRecommendationModel)
    }


    private fun getSameSessionRecommendationModel(graphqlResponse: GraphqlResponse?) =
        graphqlResponse
            ?.getData<SearchSameSessionRecommendationResponse>(
                SearchSameSessionRecommendationResponse::class.java
            )
            ?.response
            ?.data

    @GqlQuery("SameSessionRecommendation", SAME_SEARCH_RECOMMENDATION_QUERY)
    private fun createSameSessionRecommendationRequest(requestParams: RequestParams): GraphqlRequest {
        val params = UrlParamUtils.generateUrlParamString(requestParams.parameters)
        return GraphqlRequest(
            SameSessionRecommendation(),
            SearchSameSessionRecommendationResponse::class.java,
            mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )
    }

    private data class SearchSameSessionRecommendationResponse(
        @SerializedName("searchSameSessionRecommendation")
        val response: ResponseModel,
    ) {
        data class ResponseModel(
            @SerializedName("data")
            val data: SearchSameSessionRecommendationModel,
        )
    }
}
