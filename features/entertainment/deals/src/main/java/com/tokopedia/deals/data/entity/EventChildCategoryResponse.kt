package com.tokopedia.deals.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CuratedData (
        @SerializedName("event_child_category")
        var eventChildCategory: EventChildCategory = EventChildCategory()
): Parcelable, DealsBaseItemDataView()

@Parcelize
data class EventChildCategory (
        @SerializedName("categories")
        var categories: List<Category> = arrayListOf()
): Parcelable

@Parcelize
data class Category (
    @SerializedName("id")
    var id: String = "",

    @SerializedName("parent_id")
    var parentId: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("meta_title")
    var metaTitle: String = "",

    @SerializedName("meta_description")
    var metaDescription: String = "",

    @SerializedName("url")
    var url: String = "",

    @SerializedName("media_url")
    var mediaUrl: String = "",

    @SerializedName("seo_url")
    var seoUrl: String = "",

    @SerializedName("priority")
    var priority: String = "",

    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("code")
    var code: String = "",

    @SerializedName("message_error")
    var messageError: String = "",

    @SerializedName("web_url")
    var webUrl: String = "",

    @SerializedName("app_url")
    var appUrl: String = "",

    @SerializedName("is_card")
    var isCard: Int = 0,

    @SerializedName("is_hidden")
    var isHidden: Int = 0,

    @SerializedName("inactiveMediaUrl")
    var inactiveMediaUrl: String = ""
) : Parcelable
