package com.tokopedia.search.result.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.domain.model.SearchInspirationCarouselModel

object InspirationCarouselQuery {

    const val SEARCH_INSPIRATION_CAROUSEL_QUERY = """
        query SearchInspirationCarousel(${'$'}params: String!) {
            searchInspirationCarouselV2(params: ${'$'}params) {
                data {
                    title
                    type
                    position
                    layout
                    tracking_option
                    options {
                        title
                        subtitle
                        icon_subtitle
                        url
                        applink
                        banner_image_url
                        banner_link_url
                        banner_applink_url
                        identifier
                        meta
                        component_id
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
                            category_id
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
                                id
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
                            parent_id
                            min_order
                            stockbar {
                                stock
                                original_stock
                                percentage_value
                                value
                                color
                            }
                            warehouse_id_default
                        }
                        card_button {
                            title
                            applink
                        }
                        bundle {
                            shop {
                                name
                                url
                            }
                            count_sold
                            price
                            original_price
                            discount
                            discount_percentage
                        }
                    }
                }
            }
        }
    """

    @GqlQuery("InspirationCarousel", SEARCH_INSPIRATION_CAROUSEL_QUERY)
    fun createSearchInspirationCarouselRequest(params: String): GraphqlRequest =
        GraphqlRequest(
            InspirationCarousel(),
            SearchInspirationCarouselModel::class.java,
            mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )
}
