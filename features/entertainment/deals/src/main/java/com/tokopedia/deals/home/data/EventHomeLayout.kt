package com.tokopedia.deals.home.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.deals.common.model.response.EventProductDetail

/**
 * @author by jessica on 18/06/20
 */

data class EventHomeLayout(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",

        @SerializedName("category_url")
        @Expose
        val categoryUrl: String = "",

        @SerializedName("is_card")
        @Expose
        val isCard: Int = 0,

        @SerializedName("is_hidden")
        @Expose
        val isHidden: Int = 0,

        @SerializedName("priority")
        @Expose
        val priority: String = "",

        @SerializedName("media_url")
        @Expose
        val mediaUrl: String = "",

        @SerializedName("inactive_media_url")
        @Expose
        val inactiveMediaUrl: String = "",

        @SerializedName("count")
        @Expose
        val count: String = "",

        @SerializedName("items")
        @Expose
        val productDetails: List<EventProductDetail> = listOf(),

        @SerializedName("web_url")
        @Expose
        val webUrl: String = "",

        @SerializedName("app_url")
        @Expose
        val appUrl: String = ""
)