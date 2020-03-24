package com.tokopedia.contactus.inboxticket2.data.model

import com.google.gson.annotations.SerializedName

class ChipInboxDetails {
    @SerializedName(value = "chipGetInboxDetail", alternate = ["chipSubmitRatingCSAT", "chipCloseTicketByUser"])
    var chipGetInboxDetail: ChipGetInboxDetail? = null

}