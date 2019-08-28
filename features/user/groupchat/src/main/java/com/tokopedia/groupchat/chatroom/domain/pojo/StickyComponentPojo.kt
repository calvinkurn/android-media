package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by StevenFredian on 03/05/18.
 */

data class StickyComponentPojo(
        
        @SerializedName("sticky_component")
        @Expose
        var stickyComponent: StickyComponentData = StickyComponentData(),

        @SerializedName("sticky_components")
        @Expose
        var stickyComponents: List<StickyComponentData> = arrayListOf()
)

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
        var relatedButton: Int = 0,
        @SerializedName("attribute_data")
        @Expose
        var attributeData: JsonObject? = null
){
        constructor() : this("")
}

class AttributeStickyComponentData (
        @SerializedName("shop_id")
        @Expose
        var shopId: String = "",
        @SerializedName("product_id")
        @Expose
        var productId: String = "",
        @SerializedName("min_quantity")
        @Expose
        var minQuantity: Int = 0,
        @SerializedName("shop_name")
        @Expose
        var shopName: String = ""
)
