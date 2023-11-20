package com.tokopedia.catalog.domain

import com.tokopedia.catalog.domain.model.CatalogSearchProductForReimaganeResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetProductListFromSearchQuery", GetProductListFromSearchUseCase.QUERY)
class GetProductListFromSearchUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<CatalogSearchProductForReimaganeResponse>(repository) {

    companion object {

        const val QUERY = """
                query SearchProduct(${'$'}params: String!) {
                    ace_search_product_v4(params: ${'$'}params) {
                        header {
                            totalData
                            totalDataText
                            defaultView
                            responseCode
                            errorMessage
                            additionalParams
                            keywordProcess
                        }
                        data {
                            isQuerySafe
                            autocompleteApplink
                            redirection {
                                redirectApplink
                            }
                            products {
                                id
                                name
                                ads {
                                    id
                                    productClickUrl
                                    productWishlistUrl
                                    productViewUrl
                                }
                                shop {
                                    id
                                    name
                                    city
                                    url
                                    isOfficial
                                    isPowerBadge
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
                                ratingAverage
                                priceInt
                                originalPrice
                                discountPercentage
                                warehouseIdDefault
                                boosterList
                                source_engine
                                minOrder
                                url
                                childs
                                labelGroups {
                                    title
                                    position
                                    type
                                    url
                                }
                                labelGroupVariant {
                                    title
                                    type
                                    type_variant
                                    hex_color
                                }
                                badges {
                                    title
                                    imageUrl
                                    show
                                }
                                wishlist
                            }
                        }
                    }
                }
        """

    }

    init {
        setGraphqlQuery(GetProductListFromSearchQuery())
        setTypeClass(CatalogSearchProductForReimaganeResponse::class.java)
    }

    suspend fun execute(param: RequestParams): CatalogSearchProductForReimaganeResponse {
        val paramProductListing = RequestParams.create()
        paramProductListing.putString("params", param?.getString(CatalogConstant.PRODUCT_PARAMS, ""))

        setRequestParams(paramProductListing.parameters)
        return executeOnBackground()
    }
}
