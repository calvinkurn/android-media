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
          "applink": "tokopedia://getInitialStateData?q=sepatu&source=universe&st=product",
          "url": "/getInitialStateData?q=sepatu&source=universe&st=product",
          "title": "sepatu",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://getInitialStateData?q=kaos&source=universe&st=product",
          "url": "/getInitialStateData?q=kaos&source=universe&st=product",
          "title": "kaos",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://getInitialStateData?q=android&source=universe&st=product",
          "url": "/getInitialStateData?q=android&source=universe&st=product",
          "title": "android",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://getInitialStateData?q=samsung&source=universe&st=product",
          "url": "/getInitialStateData?q=samsung&source=universe&st=product",
          "title": "samsung",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        },
        {
          "template": "list_single_line",
          "applink": "tokopedia://getInitialStateData?q=iphone&source=universe&st=product",
          "url": "/getInitialStateData?q=iphone&source=universe&st=product",
          "title": "iphone",
          "label": "keyword",
          "shortcut_url": "https://shortcut"
        }
      ]
    },
    {
      "id": "recent_view",
      "header": "Terakhir Dilihat",
      "label_action": "",
      "items": []
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
          "applink": "tokopedia://getInitialStateData?q=sepatu&source=universe&st=product",
          "url": "/getInitialStateData?q=sepatu&source=universe&st=product",
          "title": "sepatu"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://getInitialStateData?q=new&source=universe&st=product",
          "url": "/getInitialStateData?q=new&source=universe&st=product",
          "title": "new"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://getInitialStateData?q=kaos&source=universe&st=product",
          "url": "/getInitialStateData?q=kaos&source=universe&st=product",
          "title": "kaos"
        },
        {
          "template": "",
          "image_url": "https://ecs7.tokopedia.net/img/search_autocomplete/popular-icon.png",
          "applink": "tokopedia://getInitialStateData?q=diego&source=universe&st=product",
          "url": "/getInitialStateData?q=diego&source=universe&st=product",
          "title": "diego"
        }
      ]
    }
  ]
""".trimIndent().replace("\n", "")

internal val initialStateCommonResponse = Gson().fromJson<List<InitialStateData>>(initialStateCommonJSON, (object: TypeToken<List<InitialStateData>>() { }).type)