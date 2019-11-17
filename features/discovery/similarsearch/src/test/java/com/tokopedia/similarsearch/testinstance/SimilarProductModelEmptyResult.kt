package com.tokopedia.similarsearch.testinstance

import com.google.gson.Gson
import com.tokopedia.similarsearch.SimilarProductModel

private val similarProductModelEmptyResultJSONString = """
{
    "similar_products_image_search": {
      "data": {
        "products": [],
        "originalProduct": {
          "id": 553354058,
          "name": "SAMSUNG Galaxy A70 ~ 8 / 128 Garansi Resmi Sein",
          "url": "https://www.tokopedia.com/krisv/samsung-galaxy-a70-8-128-garansi-resmi-sein-biru?trkid=f%3DCa0000L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D32_q%3D_po%3D1_catid%3D3054&whid=0",
          "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/8/29/2779938/2779938_0ce25b8a-e5ca-4aca-8e44-4530cf907169_1080_1080.jpg",
          "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/8/29/2779938/2779938_0ce25b8a-e5ca-4aca-8e44-4530cf907169_1080_1080.jpg",
          "price": "Rp 5.675.000",
          "shop": {
            "id": 2779938,
            "is_gold_shop": false,
            "location": "Jakarta",
            "city": "Jakarta",
            "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/2779938",
            "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=2779938",
            "is_official": false
          },
          "badges": [
            {
              "title": "Power Badge",
              "image_url": "https://ecs7.tokopedia.net/img/power_merchant_badge.png"
            }
          ],
          "category_id": 65,
          "category_name": "Handphone & Tablet",
          "rating": 0,
          "count_review": 0,
          "original_price": "",
          "discount_expired_time": "",
          "discount_start_time": "",
          "discount_percentage": 0,
          "wishlist": false
        }
      }
    }
}
""".trimIndent().replace("\n", "")

internal fun getSimilarProductModelEmptyResult() = Gson().fromJson(similarProductModelEmptyResultJSONString, SimilarProductModel::class.java)