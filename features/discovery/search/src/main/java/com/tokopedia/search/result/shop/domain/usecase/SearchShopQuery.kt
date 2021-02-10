package com.tokopedia.search.result.shop.domain.usecase

fun getSearchShopFirstPageQuery(): String {
    val params = "\$params"
    val headlineParams = "\$headline_params"
    val quickFilterParams = "\$quick_filter_params"

    return """
        query SearchShop($params: String!, $headlineParams: String, $quickFilterParams: String!) {
            ${getAceSearchShopQuery()}
            ${getHeadlineAdsQuery()}
            ${getQuickFilterQuery()}
        }
    """.trimIndent()
}

private fun getAceSearchShopQuery(): String {
    val params = "\$params"

    return """
        aceSearchShop(params: $params) {
            source
            total_shop
            search_url
            paging {
                uri_next
                uri_previous
            }
            tab_name
            shops {
                ${getShopDetailQuery()}
            }
            top_shop {
                ${getShopDetailQuery()}
            }
        }
    """.trimIndent()
}

private fun getShopDetailQuery(): String {
    return """
        shop_id
        shop_name
        shop_domain
        shop_url
        shop_applink
        shop_image
        shop_image_300
        shop_description
        shop_tag_line
        shop_location
        shop_total_transaction
        shop_total_favorite
        shop_gold_shop
        shop_is_owner
        shop_rate_speed
        shop_rate_accuracy
        shop_rate_service
        shop_status
        products {
            id
            name
            url
            applink
            price
            price_format
            image_url
        }
        voucher {
            free_shipping
            cashback {
                cashback_value
                is_percentage
            }
        }
        shop_lucky
        reputation_image_uri
        reputation_score
        is_official
        ga_key
    """.trimIndent()
}

private fun getHeadlineAdsQuery(): String {
    val headlineParams = "\$headline_params"

    return """
        headlineAds: displayAdsV3(displayParams: $headlineParams) {
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
                  product {
                    id
                    name
                    price_format
                    applinks
                    rating_average
                    label_group {
                        title
                        type
                        position
                    }
                    image_product {
                      product_id
                      product_name
                      image_url
                      image_click_url
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
              }
              applinks
            }
        }
    """.trimIndent()
}

private fun getQuickFilterQuery(): String {
    return """
        quickFilter: filter_sort_product(params:${'$'}quick_filter_params) {
            data {
              filter {
                title
                template_name
                search {
                  placeholder
                }
                options {
                  key
                  value
                  name
                  icon
                  totalData
                  valMax
                  valMin
                  hexColor
                  isPopular
                  isNew
                  Description
                }
              }
            }
        }
    """
}

fun getSearchShopLoadMoreQuery(): String {
    val params = "\$params"

    return """
        query SearchShop($params : String!) {
            ${getAceSearchShopQuery()}
        }
    """.trimIndent()
}