package com.tokopedia.search.result.domain.usecase.getlocalsearchrecommendation

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.AceSearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1

class GetLocalSearchRecommendationUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
): UseCase<SearchProductModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val graphqlRequestList = listOf(
            createGraphqlRequest(requestParams)
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
    }

    @GqlQuery("LocalSearchRecommendation", LOCAL_SEARCH_QUERY)
    private fun createGraphqlRequest(requestParams: RequestParams) =
        GraphqlRequest(
            LocalSearchRecommendation.GQL_QUERY,
            AceSearchProductModel::class.java,
            mapOf(KEY_PARAMS to UrlParamUtils.generateUrlParamString(requestParams.parameters))
        )

    companion object {
        private const val LOCAL_SEARCH_QUERY = """
            query SearchProduct(${'$'}params: String!) {
                ace_search_product_v4(params: ${'$'}params) {
                    header {
                        totalData
                    }
                    data {
                        products {
                            id
                            name
                            shop {
                                id
                                name
                                city
                                rating_average
                            }
                            freeOngkir {
                                isActive
                                imgUrl
                            }
                            imageUrl
                            imageUrl300
                            imageUrl700
                            price
                            priceInt
                            priceRange
                            categoryId
                            categoryName
                            categoryBreadcrumb
                            rating
                            ratingAverage
                            countReview
                            priceInt
                            originalPrice
                            discountPercentage
                            warehouseIdDefault
                            boosterList
                            source_engine
                            labelGroups {
                                title
                                position
                                type
                            }
                            badges {
                                title
                                imageUrl
                                show
                            }
                            wishlist
                            count_sold
                        }
                    }
                }
            }
        """
    }
}