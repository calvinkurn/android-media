package com.tokopedia.search.result.presentation.presenter.profile.testinstance

import com.google.gson.Gson
import com.tokopedia.search.result.domain.model.SearchProfileModel

private val searchProfileModelEmptyResultWithRecommendationJSON by lazy {
    """
    {
        "aceSearchProfile": {
            "query": "dariand",
            "source": "search",
            "count": 0,
            "count_text": "0",
            "has_next": false,
            "profiles": [],
            "top_profile": [
            {
                "id": 14570355,
                "name": "dian ariandy",
                "avatar": "https://ecs7.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-15.jpg",
                "username": "dariandy89",
                "followed": false,
                "iskol": false,
                "isaffiliate": true,
                "followers": 0,
                "post_count": 0
            },
            {
                "id": 38414531,
                "name": "Desti Ariandy",
                "avatar": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2019/11/19/38414531/38414531_510a4df1-ac0e-4fe7-88d6-9d3e3376f798",
                "username": "dariandy",
                "followed": false,
                "iskol": false,
                "isaffiliate": true,
                "followers": 0,
                "post_count": 0
            },
            {
                "id": 63274655,
                "name": "Daffa Arianda Saputra",
                "avatar": "https://ecs7.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-16.jpg",
                "username": "darianda",
                "followed": false,
                "iskol": false,
                "isaffiliate": true,
                "followers": 0,
                "post_count": 0
            }
            ]
        }
    }
""".trimIndent().replace("\n", "")
}

internal val searchProfileModelEmptyResultWithRecommendation by lazy {
    Gson().fromJson(searchProfileModelEmptyResultWithRecommendationJSON, SearchProfileModel::class.java)
}