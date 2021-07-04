package com.tokopedia.discovery2.data.gqlraw

const val GQL_CPM: String = """query DisplayAds(${'$'}params: String) {
    displayAdsV3(displayParams: ${'$'}params) {
      header {
        process_time
        total_data
      }
      data {
        id
        applinks
        ad_click_url
        headline {
          image {
            full_url
            full_ecs
          }
          badges {
            image_url
            show
          }
          button_text
          uri
          name
          promoted_text
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
    pm_pro_shop
    shop_is_official
    product {
        id
        name
        price_format
        applinks
        image_product{
            product_id
            product_name
            image_url
            image_click_url
        }
        label_group {
            title
            position
            type
            url
        }
        free_ongkir {
            is_active
            img_url
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
        }
      }
    }
}"""