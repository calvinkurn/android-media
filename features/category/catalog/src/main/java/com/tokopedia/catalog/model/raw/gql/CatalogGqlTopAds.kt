package com.tokopedia.catalog.model.raw.gql

const val CATALOG_GQL_TOP_ADS: String ="""query displayTopAds(${'$'}params: String) {
  productAds: displayAdsV3(displayParams: ${'$'}params) {
    status {
      error_code
      message
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
        campaign {
          discount_percentage
          original_price
        }
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
        labels {
          color
          title
        }
        label_group {
          position
          type
          title
        }
      free_ongkir{
        is_active
        img_url
      }
        top_label
        bottom_label
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