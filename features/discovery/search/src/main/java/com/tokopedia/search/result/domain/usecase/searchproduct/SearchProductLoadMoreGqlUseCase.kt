package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1

class SearchProductLoadMoreGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse, SearchProductModel>
): UseCase<SearchProductModel>() {
    
    private val graphqlRequest = GraphqlRequest(GQL_QUERY, SearchProductModel::class.java)

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val variables = createParametersForQuery(requestParams.parameters)

        graphqlRequest.variables = variables

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()

        variables[KEY_PARAMS] = UrlParamUtils.generateUrlParamString(parameters)

        return variables
    }

    companion object {
        private const val GQL_QUERY = """
            query SearchProduct(${'$'}params: String!) {
                ace_search_product_v4(params: ${'$'}params) {
                    header {
                        totalData
                    }
                    data {
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
                        }
                    }
                }
                
                productAds: displayAdsV3(displayParams: ${'$'}params) {
                    status {
                        error_code
                        message
                    }
                    header {
                        process_time
                        total_data
                    }
                    data {
                        id
                        ad_ref_key
                        redirect
                        sticker_id
                        sticker_image
                        product_click_url
                        product_wishlist_url
                        shop_click_url
                        product {
                            id
                            name
                            wishlist
                            category_breadcrumb
                            image {
                                m_url
                                s_url
                                xs_url
                                m_ecs
                                s_ecs
                                xs_ecs
                            }
                            uri
                            relative_uri
                            price_format
                            wholesale_price {
                                price_format
                                quantity_max_format
                                quantity_min_format
                            }
                            count_talk_format
                            count_review_format
                            category {
                                id
                            }
                            product_preorder
                            product_wholesale
                            free_return
                            product_cashback
                            product_new_label
                            product_cashback_rate
                            product_rating
                            product_rating_format
                            free_ongkir {
                              is_active
                              img_url
                            }
                            campaign {
                                original_price
                                discount_percentage
                            }
                            label_group {
                                position
                                type
                                title
                            }
                        }
                        shop {
                            id
                            name
                            domain
                            location
                            city
                            gold_shop
                            gold_shop_badge
                            lucky_shop
                            uri
                            owner_id
                            is_owner
                            shop_is_official
                            badges {
                                title
                                image_url
                                show
                            }
                        }
                        applinks
                    }
                    template {
                        is_ad
                    }
                }
            }"""
    }
}