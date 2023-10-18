package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName

data class ChipInboxDetails(
    @SerializedName(
        value = "chipGetInboxDetail",
        alternate = ["chipSubmitRatingCSAT", "chipCloseTicketByUser"]
    )
    var chipGetInboxDetail: ChipGetInboxDetail? = null
) {
    fun getInboxDetail() = chipGetInboxDetail ?: ChipGetInboxDetail()
}
