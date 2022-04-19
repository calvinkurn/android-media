package com.tokopedia.search.mock

import org.json.JSONObject

internal val cpmJsonString = """
  {
    "status": {
      "error_code": 0,
      "message": "OK"
    },
    "header": {
      "process_time": 0.049827741,
      "total_data": 1
    },
    "data": [
      {
        "id": "36627880",
        "ad_ref_key": "36627880",
        "redirect": "https://www.tokopedia.com/myiphonestore1",
        "headline": {
          "name": "MY IPHONESTORE",
          "image": {
            "full_url": ""
          },
          "shop": {
            "id": "5233813",
            "name": "MY IPHONESTORE",
            "domain": "myiphonestore1",
            "tagline": "RAMAH, CEPAT DAN BERKUALITAS",
            "slogan": "remai",
            "location": "Kota Administrasi Jakarta Utara",
            "city": "Kota Administrasi Jakarta Utara",
            "gold_shop": true,
            "gold_shop_badge": true,
            "shop_is_official": false,
            "product": [
              {
                "id": "450081210",
                "name": "OPPO A 37 2+16GB",
                "price_format": "Rp 1.050.000",
                "applinks": "tokopedia://product/450081210",
                "image_product": {
                  "product_id": "450081210",
                  "product_name": "OPPO A 37 2+16GB",
                  "image_url": "",
                  "image_click_url": ""
                }
              }
            ],
            "image_shop": {
              "cover": "",
              "s_url": "",
              "xs_url": "",
              "cover_ecs": "",
              "s_ecs": "",
              "xs_ecs": ""
            }
          },
          "badges": [
            {
              "image_url": "https://ecs7.tokopedia.net/ta/icon/badge/PM-Badge-80.png",
              "show": true,
              "title": "Power Merchant"
            }
          ],
          "button_text": "Kunjungi Toko",
          "promoted_text": "Promoted by",
          "description": "RAMAH, CEPAT DAN BERKUALITAS",
          "uri": "https://www.tokopedia.com/myiphonestore1"
        },
        "applinks": "tokopedia://shop/5233813"
      }
    ]
  }
""".trimIndent().replace("\n", "")

val cpmJSONObject: JSONObject = JSONObject(cpmJsonString)