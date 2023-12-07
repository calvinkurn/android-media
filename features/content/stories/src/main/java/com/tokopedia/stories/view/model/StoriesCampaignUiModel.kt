package com.tokopedia.stories.view.model

import java.util.*

/**
 * @author by astidhiyaa on 05/09/23
 */
sealed class StoriesCampaignUiModel {
    data class Ongoing(
        val title: String,
        val endTime: Date?
    ) : StoriesCampaignUiModel()

    data class Upcoming(
        val title: String,
        val startTime: Date?
    ) : StoriesCampaignUiModel()

    object Unknown : StoriesCampaignUiModel()
}

internal val StoriesCampaignUiModel.isOngoing : Boolean
    get() = this is StoriesCampaignUiModel.Ongoing

internal val StoriesCampaignUiModel.isNotAvailable : Boolean
    get() = this is StoriesCampaignUiModel.Unknown

