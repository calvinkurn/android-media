package com.tokopedia.product.detail.view.widget.campaign

import com.tokopedia.product.detail.view.widget.campaign.timebased.ongoing.UpcomingCampaignUiModel
import com.tokopedia.product.detail.view.widget.campaign.timebased.upcoming.OngoingCampaignUiModel

/**
 * Created by yovi.putra on 21/12/23"
 * Project name: android-tokopedia-core
 **/

sealed interface CampaignType {

    data class OnGoing(
        val data: OngoingCampaignUiModel,
        val onTimerFinish: () -> Unit
    ) : CampaignType

    data class UpComing(
        val data: UpcomingCampaignUiModel,
        val onTimerFinish: () -> Unit,
        val onClickRemindMe: () -> Unit
    ) : CampaignType

    data class Mega(
        val title: String,
        val logoUrl: String,
        val superGraphicUrl: String,
        val backgroundColorString: String
    ) : CampaignType

    data class Regular(
        val title: String,
        val logoUrl: String,
        val backgroundColorString: String
    ) : CampaignType
}
