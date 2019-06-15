package com.tokopedia.notifications.model

import com.google.gson.annotations.SerializedName

/**
 * @author lalit.singh
 */
data class Grid(
        @SerializedName("appLink")
        var appLink: String? = null,

        @SerializedName("img")
        var img: String? = null
)
