package com.tokopedia.autocomplete.initialstate.testinstance

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.autocomplete.initialstate.InitialStateData

private val initialStateCommonJSON = """
  [
    {
      "id": "recent_search",
      "header": "Terakhir Dicari",
      "label_action": "Hapus Semua",
      "items": [
        {
          "template": "list_single_line",
          "applink": "tokopedia://search?q=sepatu&source=universe&st=product",
          "url": "/search?q=sepatu&source=universe&st=product",
          "title": "sepatu",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://search?q=kaos&source=universe&st=product",
          "url": "/search?q=kaos&source=universe&st=product",
          "title": "kaos",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://search?q=android&source=universe&st=product",
          "url": "/search?q=android&source=universe&st=product",
          "title": "android",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://search?q=samsung&source=universe&st=product",
          "url": "/search?q=samsung&source=universe&st=product",
          "title": "samsung",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://search?q=iphone&source=universe&st=product",
          "url": "/search?q=iphone&source=universe&st=product",
          "title": "iphone",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_double_line",
          "applink": "tokopedia://shop/8384142?source=universe&st=product",
          "url": "/mizanbookc?source=universe&st=product",
          "title": "MizanBookCorner",
          "label": "Toko",
          "shortcut_url": "https://shortcut",
          "type": "shop"
        }
      ]
    },
    {
      "id": "recent_view",
      "header": "Terakhir Dilihat",
      "label_action": "",
      "items": [
        {
            "id": 567995183,
            "template": "",
            "image_url": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/24/856804/856804_c2875819-25d5-4af9-9fce-77f30c24a44f_2048_2048.jpg",
            "applink": "tokopedia://product/567995183",
            "url": "/ofan-store8/oppo-a5-2020-ram-3-64gb-garansi-resmi-oppo-indonesia-hitam",
            "title": "",
            "subtitle": "",
            "icon_title": "",
            "icon_subtitle": "",
            "label": "",
            "label_type": "",
            "shortcut_image": ""
        }
      ]
    },
    {
      "id": "popular_search",
      "header": "Pencarian Populer",
      "label_action": "Refresh",
      "items": [
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "https://www.tokopedia.com/",
          "url": "https://www.tokopedia.com/",
          "title": "popular 1"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://shop/2107964",
          "url": "https://www.tokopedia.com/discovery/sandisk",
          "title": "campaign test"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://search?q=sepatu&source=universe&st=product",
          "url": "/search?q=sepatu&source=universe&st=product",
          "title": "sepatu"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://search?q=new&source=universe&st=product",
          "url": "/search?q=new&source=universe&st=product",
          "title": "new"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://searchp?q=kaos&source=universe&st=product",
          "url": "/search?q=kaos&source=universe&st=product",
          "title": "kaos"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://search?q=diego&source=universe&st=product",
          "url": "/search?q=diego&source=universe&st=product",
          "title": "diego"
        }
      ]
    }
  ]
""".trimIndent().replace("\n", "")

internal val initialStateCommonResponse = Gson().fromJson<List<InitialStateData>>(initialStateCommonJSON, (object: TypeToken<List<InitialStateData>>() { }).type)