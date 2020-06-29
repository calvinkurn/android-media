package com.tokopedia.autocomplete.suggestion.testinstance

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.autocomplete.suggestion.data.SuggestionUniverse

private val suggestionCommonJSON = """
  {
    "data": {
        "id": "suggestion",
        "name": "SUGGESTION",
        "items": [
          {
            "template": "list_single_line",
            "type": "curated",
            "applink": "tokopedia://shop/2400899",
            "url": "/samsung",
            "title": "Samsung Mobile Indonesia",
            "subtitle": "",
            "icon_title": "",
            "icon_subtitle": "",
            "shortcut_image": "",
            "image_url": "https://ecs7.tokopedia.net/img/shops-1/2017/12/22/2400899/2400899_a1a81e3e-9067-4685-a62f-7be063df8377.png",
            "url_tracker": "",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "list_single_line",
            "type": "keyword",
            "applink": "tokopedia://search?q=samsung&source=universe&st=product",
            "url": "/search?q=samsung&source=universe&st=product",
            "title": "samsung",
            "subtitle": "",
            "icon_title": "",
            "icon_subtitle": "",
            "shortcut_image": "https://ecs7.tokopedia.net/img/autocomplete/ic_to_top_big.png",
            "image_url": "https://ecs7.tokopedia.net/img/autocomplete/search-icon-3x.png",
            "url_tracker": "",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "header",
            "type": "",
            "applink": "",
            "url": "",
            "title": "Toko",
            "subtitle": "",
            "icon_title": "",
            "icon_subtitle": "",
            "shortcut_image": "",
            "image_url": "",
            "url_tracker": "",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "list_double_line",
            "type": "shop",
            "applink": "tokopedia://shop/2400899?source=universe&st=product",
            "url": "/samsung?source=universe&st=product",
            "title": "Samsung Mobile Indonesia",
            "subtitle": "Jakarta",
            "icon_title": "https://ecs7.tokopedia.net/img/official_store_badge.png",
            "icon_subtitle": "https://ecs7.tokopedia.net/img/autocomplete/location.png",
            "shortcut_image": "",
            "image_url": "https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2017/12/22/2400899/2400899_a1a81e3e-9067-4685-a62f-7be063df8377.png",
            "url_tracker": "https://ace.tokopedia.com/tracker/v1?id=2400899&user_id=0&device=&unique_id=d41d8cd98f00b204e9800998ecf8427e&type=shop",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "list_double_line",
            "type": "shop",
            "applink": "tokopedia://shop/2386090?source=universe&st=product",
            "url": "/samsung-digital?source=universe&st=product",
            "title": "Samsung Electronic",
            "subtitle": "Jakarta",
            "icon_title": "https://ecs7.tokopedia.net/img/official_store_badge.png",
            "icon_subtitle": "https://ecs7.tokopedia.net/img/autocomplete/location.png",
            "shortcut_image": "",
            "image_url": "https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2017/8/18/2386090/2386090_26490abf-220e-40dc-a4fd-ffbea6cde12b.jpg",
            "url_tracker": "https://ace.tokopedia.com/tracker/v1?id=2386090&user_id=0&device=&unique_id=d41d8cd98f00b204e9800998ecf8427e&type=shop",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "header",
            "type": "",
            "applink": "",
            "url": "",
            "title": "Profil",
            "subtitle": "",
            "icon_title": "",
            "icon_subtitle": "",
            "shortcut_image": "",
            "image_url": "",
            "url_tracker": "",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "list_double_line",
            "type": "profile",
            "applink": "tokopedia://people/6256819",
            "url": "/people/6256819",
            "title": "Dwi Samsung",
            "subtitle": "@dsamsung",
            "icon_title": "",
            "icon_subtitle": "",
            "shortcut_image": "",
            "image_url": "https://ecs7.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-12.jpg",
            "url_tracker": "",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          },
          {
            "template": "list_double_line",
            "type": "profile",
            "applink": "tokopedia://people/5853939",
            "url": "/people/5853939",
            "title": "power bank samsung",
            "subtitle": "@powerb",
            "icon_title": "",
            "icon_subtitle": "",
            "shortcut_image": "",
            "image_url": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2019/12/2/5853939/5853939_230b5d98-a24c-45c7-b0ef-dfe637cfb838.jpg",
            "url_tracker": "",
            "label": "",
            "label_type": "",
            "tracking": {
              "code": ""
            }
          }
        ]
      },
      "top_shops": []
  }
""".trimIndent().replace("\n", "")

internal val suggestionCommonResponse = Gson().fromJson<SuggestionUniverse>(suggestionCommonJSON, (object: TypeToken<SuggestionUniverse>() { }).type)