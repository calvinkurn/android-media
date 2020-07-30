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
            gold_shop_badge
            slogan
            product {
              id
              name
              applinks
              price_format
              image_product {
                image_click_url
                image_url
              }
            }
          }
        }
      }
    }
}"""