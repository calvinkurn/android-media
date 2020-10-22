package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category (
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("parent_id")
        @Expose
        var parentId: String = "",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("description")
        @Expose
        var description: String = "",

        @SerializedName("meta_title")
        @Expose
        var metaTitle: String = "",

        @SerializedName("meta_description")
        @Expose
        var metaDescription: String = "",

        @SerializedName("url")
        @Expose
        var url: String = "",

        @SerializedName("media_url")
        @Expose
        var mediaUrl: String = "",

        @SerializedName("seo_url")
        @Expose
        var seoUrl: String = "",

        @SerializedName("priority")
        @Expose
        var priority: String = "",

        @SerializedName("status")
        @Expose
        var status: Int = 0,

        @SerializedName("message")
        @Expose
        var message: String = "",

        @SerializedName("code")
        @Expose
        var code: String = "",

        @SerializedName("message_error")
        @Expose
        var messageError: String = "",

        @SerializedName("web_url")
        @Expose
        var webUrl: String = "",

        @SerializedName("app_url")
        @Expose
        var appUrl: String = "",

        @SerializedName("is_card")
        @Expose
        var isCard: Int = 0,

        @SerializedName("is_hidden")
        @Expose
        var isHidden: Int = 0,

        @SerializedName("inactiveMediaUrl")
        @Expose
        var inactiveMediaUrl: String = ""
) : Parcelable