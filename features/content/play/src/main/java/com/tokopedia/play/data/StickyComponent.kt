package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */

data class StickyComponent(
        @SerializedName("component_type")
        val componentType: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("link_url")
        val linkUrl: String = "",

        @SerializedName("primary_text")
        val primaryText: String = "",

        @SerializedName("secondary_text")
        val secondaryText: String = "",

        @SerializedName("sticky_time_in_seconds")
        val stickyTimeInSeconds: Int = 0
) {
    data class Response(
            @SerializedName("sticky_component")
            val stickyComponent: StickyComponent = StickyComponent()
    )
}