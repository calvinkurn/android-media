package com.tokopedia.universal_sharing.model

import com.tokopedia.linker.LinkerManager
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.util.DateUtil

/**
 * @param startTime in unix form
 * @param endTime in unix form
 */
data class PersonalizedCampaignModel(
    val ongoingCampaignName: String = "",
    val upcomingCampaignName: String = "",
    val price: String = "",
    val isThematicCampaign: Boolean = false,
    val discountPercentage: Float = 0F,
    val startTime: Long = 0L,
    val endTime: Long = 0L
) {

    /**
     * if [startTime] is not [ZERO_UNIX] then the campaign is not started
     * if [endTime] is not [ZERO_UNIX] then the campaign is on going
     */
    fun getCampaignStatus(): CampaignStatus {
        return if (startTime == ZERO_UNIX && endTime != ZERO_UNIX) {
            if (DateUtil.timeIsUnderThresholdMinute(endTime, THRESHOLD_ENDSOON)) {
                CampaignStatus.END_SOON
            } else if (DateUtil.timeIsUnderThresholdWeek(endTime, THRESHOLD_END_A_WEEK)) {
                CampaignStatus.END_BY_A_WEEK
            } else {
                CampaignStatus.ON_GOING
            }
        } else if (startTime != ZERO_UNIX) {
            CampaignStatus.UPCOMING
        } else {
            CampaignStatus.NO_CAMPAIGN
        }
    }

    /**
     * get campaign name based on [getCampaignStatus]
     * @return [ongoingCampaignName] when [getCampaignStatus] == [CampaignStatus.END_SOON] or [CampaignStatus.ON_GOING]
     * @return [upcomingCampaignName] when [getCampaignStatus] == [CampaignStatus.UPCOMING]
     */
    fun getCampaignName(): String {
        return when (getCampaignStatus()) {
            CampaignStatus.NO_CAMPAIGN -> ""
            CampaignStatus.ON_GOING -> ongoingCampaignName
            CampaignStatus.END_SOON -> ongoingCampaignName
            CampaignStatus.UPCOMING -> upcomingCampaignName
            CampaignStatus.END_BY_A_WEEK -> ongoingCampaignName
        }
    }

    fun getMinuteLeft(): Long = DateUtil.getMinuteLeft(endTime)

    fun getEndDateCampaign(): String = DateUtil.getDateCampaign(endTime)

    fun getStartDateCampaign(): String = DateUtil.getDateCampaign(startTime)

    fun getDiscountString(): String = discountPercentage.toInt().toString() + "%%"

    fun isPersonalizedCampaignActive(): Boolean {
        return (!isThematicCampaign
            && !(startTime == 0L
            && endTime == 0L))
    }

    fun getPersonalizedMessage(): String {
        var personalizedMessage = ""
        val context = LinkerManager.getInstance().context
        when (getCampaignStatus()) {
            CampaignStatus.UPCOMING -> {
                personalizedMessage = if (discountPercentage != 0F) {
                    context.getString(
                        R.string.personalized_campaign_message_upcoming_discount,
                        getStartDateCampaign(),
                        getDiscountString(),
                        price
                    )
                } else {
                    context.getString(
                        R.string.personalized_campaign_message_upcoming_without_discount,
                        getStartDateCampaign()
                    )
                }
            }

            CampaignStatus.ON_GOING -> {
                personalizedMessage = if (discountPercentage != 0F) {
                    context.getString(
                        R.string.personalized_campaign_message_ongoing_discount,
                        getDiscountString(),
                        price
                    )
                } else {
                    context.getString(
                        R.string.personalized_campaign_message_ongoing_without_disc,
                        price
                    )
                }
            }

            CampaignStatus.END_SOON -> {
                personalizedMessage = if (discountPercentage != 0F) {
                    context.getString(
                        R.string.personalized_campaign_message_endsoon_discount,
                        getMinuteLeft().toString(),
                        getDiscountString(),
                        price
                    )
                } else {
                    context.getString(
                        R.string.personalized_campaign_message_endsoon_without_disc,
                        getMinuteLeft().toString(),
                        getDiscountString()
                    )
                }
            }

            CampaignStatus.END_BY_A_WEEK -> {
                personalizedMessage = if (discountPercentage != 0F) {
                    context.getString(
                        R.string.personalized_campaign_message_endweek_discount,
                        getDiscountString(),
                        price
                    )
                } else {
                    context.getString(
                        R.string.personalized_campaign_message_ongoing_without_disc,
                        price
                    )
                }
            }

            CampaignStatus.NO_CAMPAIGN -> {
                /* no-op */
            }
        }
        return personalizedMessage
    }

    fun getPersonalizedImage(): String {
        var personalizedImage = ""
        val context = LinkerManager.getInstance().context
        when (getCampaignStatus()) {
            CampaignStatus.UPCOMING -> {
                personalizedImage = context.getString(
                    R.string.start_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(startTime)
                )
            }

            CampaignStatus.ON_GOING -> {
                personalizedImage = context.getString(
                    R.string.ongoing_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(endTime)
                )
            }

            CampaignStatus.END_SOON -> {
                personalizedImage = context.getString(
                    R.string.ongoing_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(endTime)
                )
            }

            CampaignStatus.END_BY_A_WEEK -> {
                personalizedImage = context.getString(
                    R.string.ongoing_personalized_campaign_info,
                    DateUtil.getDateCampaignInfo(endTime)
                )
            }

            CampaignStatus.NO_CAMPAIGN -> {
                /* no-op */
            }
        }
        return personalizedImage
    }

    companion object {
        private const val ZERO_UNIX = 0L

        // threshold for end soon campaign in minute
        private const val THRESHOLD_ENDSOON = 60L
        private const val THRESHOLD_END_A_WEEK = 7L
    }
}

enum class CampaignStatus {
    END_SOON,
    END_BY_A_WEEK,
    UPCOMING,
    ON_GOING,
    NO_CAMPAIGN
}
