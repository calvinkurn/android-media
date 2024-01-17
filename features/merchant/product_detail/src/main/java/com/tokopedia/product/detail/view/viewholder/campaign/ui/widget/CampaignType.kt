package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.mega.MegaCampaign
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.regular.RegularCampaign
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.timebased.ongoing.OngoingCampaignComposeUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.timebased.upcoming.UpcomingCampaignComposeUiModel
import com.tokopedia.product.detail.view.widget.campaign.timebased.ongoing.OngoingCampaign
import com.tokopedia.product.detail.view.widget.timebased.upcoming.UpcomingCampaign

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

    data class Mega(
        val title: String,
        val logoUrl: String,
        val superGraphicUrl: String,
        val backgroundColorString: String
    ) : CampaignType {
        @Composable
        override fun Content() = MegaCampaign(
            title = title,
            logoUrl = logoUrl,
            superGraphicUrl = superGraphicUrl,
            backgroundColorString = backgroundColorString
        )
    }

    data class Regular(
        val title: String,
        val logoUrl: String,
        val backgroundColorString: String
    ) : CampaignType {
        @Composable
        override fun Content() = RegularCampaign(
            title = title,
            logoUrl = logoUrl,
            backgroundColorString = backgroundColorString
        )
    }

    object None : CampaignType {
        @Composable
        override fun Content() {
        }
    }
}
