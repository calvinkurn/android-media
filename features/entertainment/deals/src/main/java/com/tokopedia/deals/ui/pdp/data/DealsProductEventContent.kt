package com.tokopedia.deals.ui.pdp.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DealsProductEventContent(
    @SuppressLint("Invalid Data Type")
    @SerializedName("event_content_by_id")
    val eventContentById: com.tokopedia.deals.ui.pdp.data.EventContentById = com.tokopedia.deals.ui.pdp.data.EventContentById()
)

data class EventContentById(
    @SerializedName("data")
    val data: com.tokopedia.deals.ui.pdp.data.EventContentInnerData = com.tokopedia.deals.ui.pdp.data.EventContentInnerData()
)

data class EventContentInnerData(
    @SerializedName("section_data")
    val sectionDatas: List<com.tokopedia.deals.ui.pdp.data.EventContentSectionData> = emptyList()
)

data class EventContentSectionData(
    @SerializedName("content")
    val contents: List<com.tokopedia.deals.ui.pdp.data.EventContent> = emptyList()
)

data class EventContent(
    @SerializedName("value_text")
    val valueText: String = ""
)
