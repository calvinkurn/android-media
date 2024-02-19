package com.tokopedia.autocompletecomponent.unify.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SuggestionUnifyTracking(
    @SerializedName("code")
    @Expose
    val code: String = "",
    @SerializedName("component_id")
    @Expose
    val componentId: String = "",
    @SerializedName("tracker_url")
    @Expose
    val trackerUrl: String = "",
    @SerializedName("tracking_option")
    @Expose
    val trackingOption: Int = 0
)
