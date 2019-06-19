package com.tokopedia.notifications.model

import com.google.gson.annotations.SerializedName

/**
 * @author lalit.singh
 */
data class Media(
        @SerializedName("fallback_url")
        var fallbackUrl: String,

        @SerializedName("high_quality_url")
        var highQuality: String,

        @SerializedName("medium_quality_url")
        var mediumQuality: String,

        @SerializedName("low_quality_url")
        var lowQuality: String,

        @SerializedName("display_url")
        var displayUrl: String
)
