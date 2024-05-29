package com.tokopedia.search.result.domain.usecase.searchproduct.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    TopAdsQuery.QUERY_NAME,
    TopAdsQuery.TOPADS_PRODUCT_QUERY
)
object TopAdsQuery {

    const val QUERY_NAME = "TopAdsProduct"

    const val TOPADS_PRODUCT_QUERY = """
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
                    creative_id
                    log_extra
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
                        price_range
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
                            style {
                                key
                                value
                            }
                        }
                        customvideo_url
                        parent_id
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
}
