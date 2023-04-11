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
    @SerializedName("restrictions")
    val restrictions: List<FeedXRestriction> = emptyList(),
    var reminder: FeedASGCUpcomingReminderStatus = FeedASGCUpcomingReminderStatus.Off(id.toLongOrZero())
) {
    val campaignId: Long
        get() = id.toLongOrZero()

    val isUpcoming: Boolean
        get() = status == UPCOMING

    val isOngoing: Boolean
        get() = status == ONGOING

    val isRilisanSpl: Boolean
        get() = name == ASGC_RILISAN_SPECIAL ||  name == RILISAN_SPECIAL

    val isFlashSaleToko: Boolean
        get() = name == ASGC_FLASH_SALE_TOKO || name == FLASH_SALE_TOKO

    val isRSFollowersRestrictionOn: Boolean
        get() = restrictions.firstOrNull()?.let {
            it.label == FOLLOWERS_ONLY_RESTRICTION && it.isActive
        } ?: false

    companion object {
        private const val ASGC_RILISAN_SPECIAL = "asgc_rilisan_spesial"//Rilisan Spesial
        private const val RILISAN_SPECIAL = "Rilisan Spesial"
        private const val ASGC_FLASH_SALE_TOKO = "asgc_flash_sale_toko"
        private const val FLASH_SALE_TOKO = "Flash Sale Toko"
        private const val FOLLOWERS_ONLY_RESTRICTION = "followers_only"
        const val ONGOING = "ongoing"
        const val UPCOMING = "upcoming"
    }
}

sealed class FeedASGCUpcomingReminderStatus {
    data class On(val campaignId: Long) : FeedASGCUpcomingReminderStatus()
    data class Off(val campaignId: Long) : FeedASGCUpcomingReminderStatus()
    object Unknown : FeedASGCUpcomingReminderStatus()
}

fun FeedASGCUpcomingReminderStatus.reversed(campaignId: Long): FeedASGCUpcomingReminderStatus =
    if (this is FeedASGCUpcomingReminderStatus.Off) FeedASGCUpcomingReminderStatus.On(campaignId) else FeedASGCUpcomingReminderStatus.Off(
        campaignId
    )




