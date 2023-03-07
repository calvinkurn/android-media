package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class DeleteSearchHistoryResponse(
    @SerializedName("universe_delete_recent_search")
    val universeDeleteRecentSearch: UniverseDeleteRecentSearch = UniverseDeleteRecentSearch()
)

data class UniverseDeleteRecentSearch(
    @SerializedName("status")
    val status: String = ""
)
