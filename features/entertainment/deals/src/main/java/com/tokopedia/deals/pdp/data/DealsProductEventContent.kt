package com.tokopedia.deals.pdp.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DealsProductEventContent(
    @SuppressLint("Invalid Data Type")
    @SerializedName("event_content_by_id")
    val eventContentById: EventContentById = EventContentById()
)

data class EventContentById(
    @SerializedName("data")
    val data: EventContentInnerData = EventContentInnerData()
)

data class EventContentInnerData(
    @SerializedName("section_data")
    val sectionDatas: List<EventContentSectionData> = emptyList()
)

data class EventContentSectionData(
    @SerializedName("content")
    val contents: List<EventContent> = emptyList()
)

data class EventContent(
    @SerializedName("value_text")
    val valueText: String = ""
)
