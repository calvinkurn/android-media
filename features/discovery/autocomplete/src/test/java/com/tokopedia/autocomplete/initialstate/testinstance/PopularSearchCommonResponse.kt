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
      "applink": "tokopedia://getInitialStateData?q=sepatu&source=universe&st=product",
      "url": "/getInitialStateData?q=sepatu&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "kaos",
      "title": "kaos",
      "applink": "tokopedia://getInitialStateData?q=kaos&source=universe&st=product",
      "url": "/getInitialStateData?q=kaos&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "diego",
      "title": "diego",
      "applink": "tokopedia://getInitialStateData?q=diego&source=universe&st=product",
      "url": "/getInitialStateData?q=diego&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "new",
      "title": "new",
      "applink": "tokopedia://getInitialStateData?q=new&source=universe&st=product",
      "url": "/getInitialStateData?q=new&source=universe&st=product",
      "image_url": ""
    },
    {
      "keyword": "amild",
      "title": "amild",
      "applink": "tokopedia://getInitialStateData?q=amild&source=universe&st=product",
      "url": "/getInitialStateData?q=amild&source=universe&st=product",
      "image_url": ""
    }
  ]
""".trimIndent().replace("\n", "")

internal val popularSearchCommonResponse = Gson().fromJson<List<InitialStateItem>>(popularSearchCommonJSON, (object: TypeToken<List<InitialStateItem>>() { }).type)