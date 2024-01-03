package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget

import androidx.compose.runtime.Composable
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.mega.MegaCampaign
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.regular.RegularCampaign
import com.tokopedia.product.detail.view.widget.campaign.timebased.ongoing.OngoingCampaign
import com.tokopedia.product.detail.view.widget.timebased.upcoming.UpcomingCampaign

/**
 * Created by yovi.putra on 21/12/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun CampaignRibbonCompose(type: CampaignType) = NestTheme {
    when (type) {
        is CampaignType.OnGoing -> OngoingCampaign(
            uiModel = type.data,
            onTimerFinish = type.onTimerFinish
        )

        is CampaignType.UpComing -> UpcomingCampaign(
            uiModel = type.data,
            onTimerFinish = type.onTimerFinish,
            onClickRemindMe = type.onClickRemindMe
        )

        is CampaignType.Mega -> MegaCampaign(
            title = type.title,
            logoUrl = type.logoUrl,
            superGraphicUrl = type.superGraphicUrl,
            backgroundColorString = type.backgroundColorString
        )

        is CampaignType.Regular -> RegularCampaign(
            title = type.title,
            logoUrl = type.logoUrl,
            backgroundColorString = type.backgroundColorString
        )
    }
}
