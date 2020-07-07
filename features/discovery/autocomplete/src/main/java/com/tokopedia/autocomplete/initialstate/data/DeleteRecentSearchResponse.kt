package com.tokopedia.autocomplete.initialstate.data

import com.google.gson.annotations.SerializedName

data class DeleteRecentSearchResponse(
        @SerializedName("universe_delete_recent_search")
        val data: DeleteRecentSearchUniverse = DeleteRecentSearchUniverse()
) {
        data class DeleteRecentSearchUniverse(
                @SerializedName("status")
                val status: String = ""
        ) {
                val isSuccess: Boolean
                        get() = status.equals("success", true)
        }
}