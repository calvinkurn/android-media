package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero

data class FeedXCampaign(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("shortName")
    var shortName: String = "",
    @SerializedName("startTime")
    var startTime: String = "",
    @SerializedName("endTime")
    var endTime: String = "",
    var reminder: FeedASGCUpcomingReminderStatus = FeedASGCUpcomingReminderStatus.Off(id.toLongOrZero())
) {
    val campaignId: Long
        get() = id.toLongOrZero()

    val isUpcoming: Boolean
        get() = status == Upcoming

    val isRilisanSpl: Boolean
        get() = name == ASGC_RILISAN_SPECIAL

    companion object {
        private const val Upcoming = "upcoming"
        private const val ASGC_RILISAN_SPECIAL = "asgc_rilisan_spesial"
    }
}

sealed class FeedASGCUpcomingReminderStatus {
    data class On(val campaignId: Long) : FeedASGCUpcomingReminderStatus()
    data class Off(val campaignId: Long) : FeedASGCUpcomingReminderStatus()
    object Unknown : FeedASGCUpcomingReminderStatus()
}



