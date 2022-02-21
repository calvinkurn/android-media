package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.domain.model.AceSearchProductModel
import com.tokopedia.search.result.domain.model.HeadlineAdsModel
import com.tokopedia.search.result.domain.model.ProductTopAdsModel
import com.tokopedia.usecase.RequestParams

internal fun graphqlRequests(request: MutableList<GraphqlRequest>.() -> Unit) =
        mutableListOf<GraphqlRequest>().apply {
                request()
        }

internal fun MutableList<GraphqlRequest>.addAceSearchProductRequest(params: String) {
        add(createAceSearchProductRequest(params))
}

@GqlQuery("AceSearchProduct", ACE_SEARCH_PRODUCT_QUERY)
internal fun createAceSearchProductRequest(params: String) =
        GraphqlRequest(
                AceSearchProduct.GQL_QUERY,
                AceSearchProductModel::class.java,
                mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

internal fun MutableList<GraphqlRequest>.addProductAdsRequest(requestParams: RequestParams, params: String) {
        if (!requestParams.isSkipProductAds()) {
                add(createTopAdsProductRequest(params = params))
        }
}

@GqlQuery("TopAdsProduct", TOPADS_PRODUCT_QUERY)
internal fun createTopAdsProductRequest(params: String) =
        GraphqlRequest(
                TopAdsProduct.GQL_QUERY,
                ProductTopAdsModel::class.java,
                mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

internal fun MutableList<GraphqlRequest>.addHeadlineAdsRequest(
    requestParams: RequestParams,
    headlineParams: String,
) {
    if (!requestParams.isSkipHeadlineAds()) {
        add(createHeadlineAdsRequest(headlineParams = headlineParams))
    }
}

@GqlQuery("HeadlineAds", HEADLINE_ADS_QUERY)
internal fun createHeadlineAdsRequest(headlineParams: String) =
    GraphqlRequest(
        HeadlineAds.GQL_QUERY,
        HeadlineAdsModel::class.java,
        mapOf(SearchConstant.GQL.KEY_HEADLINE_PARAMS to headlineParams)
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
                componentId
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
                    componentId
                    trackingOption
                }
                banner {
                    position
                    text
                    applink
                    imageUrl
                }
                related {
                    relatedKeyword
                    position
                    trackingOption
                    otherRelated {
                        keyword
                        url
                        applink
                        componentId
                        product {
                            id
                            name
                            price
                            imageUrl
                            url
                            applink
                            priceStr
                            wishlist
                            ratingAverage
                            componentId
                            labelGroups {
                                title
                                position
                                type
                                url
                            }
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
                            ads {
                                id
                                productClickUrl
                                productWishlistUrl
                                productViewUrl
                            }
                        }
                    }
                }
                suggestion {
                    suggestion
                    query
                    text
                    componentId
                    trackingOption
                }
                products {
                    id
                    name
                    ads {
                        id
                        productClickUrl
                        productWishlistUrl
                        productViewUrl
                        tag
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
                    applink
                }
                violation {
                    headerText
                    descriptionText
                    imageURL
                    ctaURL
                    buttonText
                    buttonType
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
                tag
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
                    product_item_sold_payment_verified
                    product_minimum_order
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
                        url
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
                    shop_rating_avg
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

private const val HEADLINE_ADS_QUERY = """
    query HeadlineAds(${'$'}headline_params: String!) {
        headlineAds: displayAdsV3(displayParams: ${'$'}headline_params) {
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
                ad_click_url
                headline {
                    template_id
                    name
                    image {
                        full_url
                        full_ecs
                    }
                    shop {
                        id
                        name
                        domain
                        tagline
                        slogan
                        location
                        city
                        gold_shop
                        gold_shop_badge
                        shop_is_official
                        pm_pro_shop
                        merchant_vouchers
                        product {
                            id
                            name
                            price_format
                            applinks
                            product_cashback
                            product_cashback_rate
                            product_new_label
                            count_review_format
                            rating_average
                            label_group {
                                title
                                type
                                position
                                url
                            }
                            free_ongkir {
                                is_active
                                img_url
                            }
                            image_product{
                                product_id
                                product_name
                                image_url
                                image_click_url
                            }
                            campaign {
                                original_price
                                discount_percentage
                            }
                        }
                        image_shop {
                            cover
                            s_url
                            xs_url
                            cover_ecs
                            s_ecs
                            xs_ecs
                        }
                    }
                    badges {
                        image_url
                        show
                        title
                    }
                    button_text
                    promoted_text
                    description
                    uri
                    layout
                }
                applinks
            }
        }
    }
"""