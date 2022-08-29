package com.tokopedia.deals.pdp.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsProductEventContent (
    @SuppressLint("Invalid Data Type")
    @SerializedName("event_content_by_id")
    @Expose
    val eventContentById: EventContentById = EventContentById()
)

data class EventContentById (
    @SerializedName("data")
    @Expose
    val data: EventContentInnerData = EventContentInnerData()
)

data class EventContentInnerData (
    @SerializedName("section_data")
    @Expose
    val sectionDatas: List<EventContentSectionData> = listOf()
)

data class EventContentSectionData (
    @SerializedName("content")
    @Expose
    val contents: List<EventContent> = listOf()
)

data class EventContent (
    @SerializedName("value_text")
    @Expose
    val valueText: String = ""
)
