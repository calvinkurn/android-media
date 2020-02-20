package com.tokopedia.autocomplete.initialstate.testinstance

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.autocomplete.initialstate.InitialStateItem

private val popularSearchCommonJSON = """
  [
    {
      "keyword": "popular 1",
      "title": "popular 1",
      "applink": "https://www.tokopedia.com/",
      "url": "https://www.tokopedia.com/",
      "image_url": ""
    },
    {
      "keyword": "sepatu",
      "title": "sepatu",
      "applink": "tokopedia://search?q=sepatu&source=universe&st=product",
      "url": "/search?q=sepatu&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "kaos",
      "title": "kaos",
      "applink": "tokopedia://search?q=kaos&source=universe&st=product",
      "url": "/search?q=kaos&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "diego",
      "title": "diego",
      "applink": "tokopedia://search?q=diego&source=universe&st=product",
      "url": "/search?q=diego&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "new",
      "title": "new",
      "applink": "tokopedia://search?q=new&source=universe&st=product",
      "url": "/search?q=new&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "amild",
      "title": "amild",
      "applink": "tokopedia://search?q=amild&source=universe&st=product",
      "url": "/search?q=amild&source=universe&st=product",
      "image_url": ""
    }
  ]
""".trimIndent().replace("\n", "")

internal val popularSearchCommonResponse = Gson().fromJson<List<InitialStateItem>>(popularSearchCommonJSON, (object: TypeToken<List<InitialStateItem>>() { }).type)