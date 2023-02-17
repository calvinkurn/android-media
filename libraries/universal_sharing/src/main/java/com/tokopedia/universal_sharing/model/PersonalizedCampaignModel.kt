package com.tokopedia.universal_sharing.model

import com.tokopedia.universal_sharing.util.DateUtil

/**
 * @param startTime in unix form
 * @param endTime in unix form
 */
data class PersonalizedCampaignModel (
    val campaignName: String = "",
    val discountPrice: String = "",
    val discountPercentage: Float = 0F,
    val startTime: Long = 0L,
    val endTime: Long = 0L
) {

    /**
     * if [startTime] is not [ZERO_UNIX] then the campaign is not started
     * if [endTime] is not [ZERO_UNIX] then the campaign is on going
     */
    fun getCampaignStatus(): CampaignStatus {
        if (startTime == ZERO_UNIX && endTime != ZERO_UNIX) {
            if (DateUtil.timeIsUnderThresholdMinute(endTime, THRESHOLD_ENDSOON)) {
                return CampaignStatus.END_SOON
            } else {
                return CampaignStatus.ON_GOING
            }
        } else if (startTime != ZERO_UNIX) {
            return CampaignStatus.UPCOMING
        } else {
            return CampaignStatus.NO_CAMPAIGN
        }
    }

    fun getStartHourString(): String = DateUtil.getHour(startTime)

    fun getMinuteLeft(): Long = DateUtil.getMinuteLeft(endTime)

    fun getEndHourString(): String = DateUtil.getHour(endTime)

    fun getUpcomingString(): String = DateUtil.getDateUpcoming(startTime)

    fun getDiscountString(): String = discountPercentage.toInt().toString() + "%%"

    companion object {
        private const val ZERO_UNIX = 0L

        // threshold for endsoon campaign in minute
        private const val THRESHOLD_ENDSOON = 15L
    }
}

enum class CampaignStatus {
    END_SOON,
    UPCOMING,
    ON_GOING,
    NO_CAMPAIGN
}
