package com.tokopedia.product.detail.view.viewholder.campaign.ui.model

import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef

/**
 * Created by Yehezkiel on 10/08/20
 */
data class UpcomingCampaignUiModel(
    val campaignID: String = "",
    val campaignType: String = "",
    val campaignTypeName: String = "",
    val campaignLogo: String = "",
    val notifyMe: Boolean = false,
    val bgColorUpcoming: String = "",
    val upcomingType: String = "",
    val ribbonCopy: String = "",
    val startDate: String = "",
    val isOwner: Boolean = false
) {

    val hasValue
        get() = campaignID.isNotBlank()

    val isNpl
        get() = upcomingType == ProductUpcomingTypeDef.UPCOMING_NPL

    val showReminderButton
        get() = !isOwner && !isNpl
}
