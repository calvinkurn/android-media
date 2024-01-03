package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget

import androidx.compose.runtime.Immutable
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.timebased.ongoing.OngoingCampaignComposeUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.timebased.upcoming.UpcomingCampaignComposeUiModel

/**
 * Created by yovi.putra on 21/12/23"
 * Project name: android-tokopedia-core
 **/

@Immutable
sealed interface CampaignType {

    data class OnGoing(
        val data: OngoingCampaignComposeUiModel,
        val onTimerFinish: () -> Unit
    ) : CampaignType

    data class UpComing(
        val data: UpcomingCampaignComposeUiModel,
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

    object None : CampaignType
}
