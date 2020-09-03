package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.domain.model.AceSearchProductModel
import com.tokopedia.search.result.domain.model.ProductTopAdsModel

internal fun createAceSearchProductRequest(params: String) =
        GraphqlRequest(
                ACE_SEARCH_PRODUCT_QUERY,
                AceSearchProductModel::class.java,
                mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

internal fun createTopAdsProductRequest(params: String) =
        GraphqlRequest(
                TOPADS_PRODUCT_QUERY,
                ProductTopAdsModel::class.java,
                mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

private const val ACE_SEARCH_PRODUCT_QUERY = """
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
                ticker {
                    text
                    query
                    typeId
                }
                related {
                    relatedKeyword
                    position
                    otherRelated {
                        keyword
                        url
                        applink
                        product {
                            id
                            name
                            price
                            imageUrl
                            rating
                            countReview
                            url
                            applink
                            priceStr
                            wishlist
                            shop {
                                city
                            }
                            badges {
                                imageUrl
                                show
                            }
                            freeOngkir {
                                isActive
                                imgUrl
                            }
                        }
                    }
                }
                suggestion {
                    suggestion
                    query
                    text
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

private const val TOPADS_PRODUCT_QUERY = """
    query ProductAds(${'$'}params: String!) {
        productAds: displayAdsV3(displayParams: ${'$'}params) {
            status {
                error_code
                message
            }
            header {
                process_time
                total_data
            }
            data{
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
                    category_breadcrumb
                    wishlist
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
                shop{
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
    }
"""