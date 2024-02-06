package com.tokopedia.search.result.domain.usecase.searchproduct.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    HeadlineAdsQuery.QUERY_NAME,
    HeadlineAdsQuery.HEADLINE_ADS_QUERY
)
object HeadlineAdsQuery {

    const val QUERY_NAME = "HeadlineAdsProduct"

    const val HEADLINE_ADS_QUERY = """
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
                        widget_title
                        widget_image_url 
                        image {
                            full_url
                            full_ecs
                        }
                        flash_sale_campaign_detail{
                            start_time
                            end_time
                            campaign_type
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
                                stock_info {
                                    sold_stock_percentage
                                    stock_wording
                                    stock_colour
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
}
