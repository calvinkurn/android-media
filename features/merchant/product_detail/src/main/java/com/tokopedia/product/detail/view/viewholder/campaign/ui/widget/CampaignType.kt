package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.ongoing.OngoingCampaign
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.ongoing.OngoingCampaignComposeUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.thematic.ThematicCampaign
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.upcoming.UpcomingCampaign
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.upcoming.UpcomingCampaignComposeUiModel

/**
 * Created by yovi.putra on 21/12/23"
 * Project name: android-tokopedia-core
 **/

@Immutable
sealed interface CampaignType {

    @Composable
    fun Content()

    data class OnGoing(
        val data: OngoingCampaignComposeUiModel,
        val onTimerFinish: () -> Unit
    ) : CampaignType {
        @Composable
        override fun Content() = OngoingCampaign(
            uiModel = data,
            onTimerFinish = onTimerFinish
        )
    }

    data class UpComing(
        val data: UpcomingCampaignComposeUiModel,
        val onTimerFinish: () -> Unit,
        val onClickRemindMe: () -> Unit
    ) : CampaignType {
        @Composable
        override fun Content() = UpcomingCampaign(
            uiModel = data,
            onTimerFinish = onTimerFinish,
            onClickRemindMe = onClickRemindMe
        )
    }

    data class Thematic(
        val title: String,
        val logoUrl: String,
        val superGraphicUrl: String,
        val backgroundColorString: String
    ) : CampaignType {
        @Composable
        override fun Content() = ThematicCampaign(
            title = title,
            logoUrl = logoUrl,
            superGraphicUrl = superGraphicUrl,
            backgroundColorString = backgroundColorString
        )
    }

    object None : CampaignType {
        @Composable
        override fun Content() {
        }
    }
}
