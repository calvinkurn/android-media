package com.tokopedia.search.result.presentation.presenter.profile.testinstance

import com.google.gson.Gson
import com.tokopedia.search.result.domain.model.SearchProfileModel

private val searchProfileModelEmptyResultJSON by lazy {
    """
    {
        "aceSearchProfile": {
            "query": "dariand",
            "source": "search",
            "count": 0,
            "count_text": "0",
            "has_next": false,
            "profiles": [],
            "top_profile": []
        }
    }
""".trimIndent().replace("\n", "")
}

internal val searchProfileModelEmptyResult by lazy {
    Gson().fromJson(searchProfileModelEmptyResultJSON, SearchProfileModel::class.java)
}