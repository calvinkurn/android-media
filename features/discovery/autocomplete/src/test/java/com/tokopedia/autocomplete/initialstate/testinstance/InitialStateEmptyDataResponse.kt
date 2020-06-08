package com.tokopedia.autocomplete.initialstate.testinstance

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.autocomplete.initialstate.InitialStateData

private val initialStateEmptyDataJSON = """
  [
    {
      "id": "recent_search",
      "header": "Terakhir Dicari",
      "label_action": "Hapus Semua",
      "items": []
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
      "items": []
    }
  ]
""".trimIndent().replace("\n", "")

internal val initialStateEmptyDataResponse = Gson().fromJson<List<InitialStateData>>(initialStateEmptyDataJSON, (object: TypeToken<List<InitialStateData>>() { }).type)