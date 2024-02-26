package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionUnifyTTSTracking(
    @SerializedName("group_id")
    @Expose
    val groupId: String = "",

    @SerializedName("sug_type")
    @Expose
    val sugType: String = "",
)
