package com.tokopedia.search.result.domain.usecase.getinspirationcarouselchips

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.usecase.searchproduct.sreParams
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetInspirationCarouselChipsProductsUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val reimagineRollence: ReimagineRollence = ReimagineRollence()
): UseCase<InspirationCarouselChipsProductModel>() {

    @GqlQuery("GetInspirationCarouselChipProductsQuery", GQL_QUERY)
    override fun createObservable(
        requestParams: RequestParams
    ): Observable<InspirationCarouselChipsProductModel> {
        val params = UrlParamUtils.generateUrlParamString(requestParams.parameters) + sreParams(
            reimagineRollence.search3ProductCard().isReimagineProductCard()
        )
        val graphqlRequest = GraphqlRequest(
            GetInspirationCarouselChipProductsQuery(),
            InspirationCarouselChipsProductModel::class.java,
            mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(listOf(graphqlRequest))

        return graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map {
                it.getData(InspirationCarouselChipsProductModel::class.java)
                    ?: InspirationCarouselChipsProductModel()
            }
    }

    companion object {
        internal const val GQL_QUERY = """
            query searchProductCarouselByIdentifier(${'$'}params: String!) {
              searchProductCarouselByIdentifier(param: ${'$'}params){
                product {
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
                        styles { 
                            key 
                            value 
                        }
                    }
                    original_price
                    discount_percentage
                    badges {
                        title
                        image_url
                        show
                    }
                    shop {
                        name
                        city
                    }
                    ads {
                        id
                        productClickUrl
                        productWishlistUrl
                        productViewUrl
                    }
                }
              }
            }
        """
    }
}
