package com.tokopedia.similarsearch.testinstance

import com.google.gson.Gson
import com.tokopedia.similarsearch.SimilarProductModel

private val similarProductModelOnePageJSONString = """
{
    "similar_products_image_search": {
      "data": {
        "products": [
          {
            "id": 465335395,
            "name": "Samsung Galaxy A70 - 6GB/128GB - Black - Garansi Resmi 1 Tahun SEIN",
            "url": "https://www.tokopedia.com/ucell-cempaka/samsung-galaxy-a70-6gb-128gb-black-garansi-resmi-1-tahun-sein?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D1_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/7/28/2341036/2341036_e72ffa48-156d-48bb-8ff9-14cf3f11fecd_536_536",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/7/28/2341036/2341036_e72ffa48-156d-48bb-8ff9-14cf3f11fecd_536_536",
            "price": "Rp 4.950.000",
            "shop": {
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/312816",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=312816",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
              }
            ],
            "category_id": 65,
            "category_name": "Handphone & Tablet",
            "rating": 5,
            "count_review": 7,
            "original_price": "",
            "discount_expired_time": "",
            "discount_start_time": "",
            "discount_percentage": 0,
            "wishlist": false
          },
          {
            "id": 550040718,
            "name": "Samsung Galaxy A70 Ram 8/128 GB Garansi Resmi Sein - Hitam",
            "url": "https://www.tokopedia.com/enterphone2/samsung-galaxy-a70-ram-8-128-gb-garansi-resmi-sein-hitam?trkid=f%3DCa3054L000P0W0S0Sh%2CCo0Po0Fr0Cb0_src%3Dimagesearch_page%3D1_ob%3D23_q%3D_po%3D2_catid%3D3054&whid=0",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/6/12/11126335/11126335_16b65442-e944-4c81-a57b-c2b45a2a5860_700_700",
            "image_url_700": "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/6/12/11126335/11126335_16b65442-e944-4c81-a57b-c2b45a2a5860_700_700",
            "price": "Rp 5.650.000",
            "shop": {
              "is_gold_shop": false,
              "location": "Jakarta",
              "city": "Jakarta",
              "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/1321037",
              "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=1321037",
              "is_official": true
            },
            "badges": [
              {
                "title": "Official Store",
                "image_url": "https://ecs7.tokopedia.net/img/official_store_badge.png"
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
        ]
      }
    }
}
""".trimIndent().replace("\n", "")

internal val similarProductModelOnePage = Gson().fromJson(similarProductModelOnePageJSONString, SimilarProductModel::class.java)