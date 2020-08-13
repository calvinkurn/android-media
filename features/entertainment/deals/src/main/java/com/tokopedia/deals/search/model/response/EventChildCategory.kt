package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CuratedData (
        @SerializedName("event_child_category")
        @Expose
        var eventChildCategory: EventChildCategory = EventChildCategory()
): Parcelable, DealsBaseItemDataView()

@Parcelize
data class EventChildCategory (
        @SerializedName("categories")
        @Expose
        var categories: List<Category> = arrayListOf()
): Parcelable