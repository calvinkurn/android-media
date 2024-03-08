package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.upcoming

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignTimer
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignTitle
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.RemindMeButton
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.campaignBackgroundColor

/**
 * Created by yovi.putra on 19/12/23"
 * Project name: android-unify
 **/

@Composable
fun UpcomingCampaign(
    uiModel: UpcomingCampaignComposeUiModel,
    onTimerFinish: () -> Unit,
    onClickRemindMe: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .campaignBackgroundColor(colorString = uiModel.backgroundColorString)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f, true),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            CampaignTitle(title = uiModel.title, uiModel.logoUrl, logoHeight = 14.dp)

            UpcomingCountDown(uiModel, onTimerFinish)
        }

        if (uiModel.showRemainderButton) {
            RemindMeButton(
                text = uiModel.labelButton,
                onClick = onClickRemindMe,
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

@Composable
private fun UpcomingCountDown(
    uiModel: UpcomingCampaignComposeUiModel,
    onTimerFinish: () -> Unit
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestTypography(
            text = uiModel.timerLabel,
            textStyle = NestTheme.typography.small.copy(
                color = Color.White
            ),
            overflow = TextOverflow.Ellipsis
        )
        CampaignTimer(
            modifier = Modifier.wrapContentSize(),
            endTimeMs = uiModel.endTimeUnix.secondToMs,
            onFinish = onTimerFinish
        )
    }
}

@Preview
@Composable
fun UpcomingCampaignPreview() {
    val data = UpcomingCampaignComposeUiModel(
        logoUrl = "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg",
        title = "Kerja Keras",
        endTimeUnix = 1704187685,
        timerLabel = "Akan datang",
        labelButton = "Ingatkan saya",
        backgroundColorString = ""
    )
    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            UpcomingCampaign(data.copy(logoUrl = ""), onTimerFinish = {}, onClickRemindMe = {})
            UpcomingCampaign(data, onTimerFinish = {}, onClickRemindMe = {})
            UpcomingCampaign(
                data.copy(logoUrl = "", showRemainderButton = false),
                onTimerFinish = {},
                onClickRemindMe = {}
            )
            UpcomingCampaign(
                data.copy(showRemainderButton = false),
                onTimerFinish = {},
                onClickRemindMe = {}
            )
        }
    }
}
