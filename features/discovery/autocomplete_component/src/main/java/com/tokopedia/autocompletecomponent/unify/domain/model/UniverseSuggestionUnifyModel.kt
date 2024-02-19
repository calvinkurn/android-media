package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class UniverseSuggestionUnifyModel(
    @SerializedName("data")
    @Expose
    val `data`: List<SuggestionUnify> = listOf(),

    @SerializedName("flags")
    @Expose
    val flags: List<SuggestionUnifyFlag> = listOf(),

    @SerializedName("header")
    @Expose
    val header: SuggestionUnifyHeader = SuggestionUnifyHeader(),

    @SerializedName("headlineAds")
    @Expose
    var cpmModel: CpmModel = CpmModel()
)
