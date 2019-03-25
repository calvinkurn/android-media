package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by StevenFredian on 03/05/18.
 */

data class StickyComponentPojo(
        
        @SerializedName("sticky_component")
        @Expose
        var stickyComponent: StickyComponentData = StickyComponentData()
) {
}

class StickyComponentData (
        @SerializedName("component_id")
        @Expose
        var componentId: String = "",
        @SerializedName("component_type")
        @Expose
        var componentType: String = "",
        @SerializedName("image_url")
        @Expose
        var imageUrl: String = "",
        @SerializedName("link_url")
        @Expose
        var linkUrl: String = "",
        @SerializedName("primary_text")
        @Expose
        var primaryText: String = "",
        @SerializedName("secondary_text")
        @Expose
        var secondaryText: String = "",
        @SerializedName("sticky_time_in_seconds")
        @Expose
        var stickyTime: Int = 0,
        @SerializedName("related_button_id")
        @Expose
        var relatedButton: Int = 0
){
        constructor() : this("")
}
