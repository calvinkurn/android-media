package com.tokopedia.product.detail.view.widget.timebased.upcoming

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
import com.tokopedia.product.detail.view.widget.campaign.component.CampaignName
import com.tokopedia.product.detail.view.widget.campaign.component.CampaignTimer
import com.tokopedia.product.detail.view.widget.campaign.component.PaymentSpecific
import com.tokopedia.product.detail.view.widget.campaign.component.RemindMeButton
import com.tokopedia.product.detail.view.widget.campaign.component.campaignBackgroundColor
import com.tokopedia.product.detail.view.widget.campaign.timebased.ongoing.UpcomingCampaignUiModel
import com.tokopedia.product.detail.view.widget.campaign.timebased.ongoing.secondToMs

/**
 * Created by yovi.putra on 19/12/23"
 * Project name: android-unify
 **/

@Composable
fun UpcomingCampaign(
    uiModel: UpcomingCampaignUiModel,
    onTimerFinish: () -> Unit,
    onClickRemindMe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .campaignBackgroundColor(colorString = uiModel.backgroundColorString)
    ) {
        UpcomingContent(uiModel, onTimerFinish, onClickRemindMe)

        if (uiModel.paymentSpecific.isNotBlank()) {
            PaymentSpecific(description = uiModel.paymentSpecific)
        }
    }
}

@Composable
private fun UpcomingContent(
    uiModel: UpcomingCampaignUiModel,
    onTimerFinish: () -> Unit,
    onClickRemindMe: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f, true),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            CampaignName(title = uiModel.title, uiModel.logoUrl, logoHeight = 14.dp)

            UpcomingCountDown(uiModel, onTimerFinish)
        }

        RemindMeButton(
            text = uiModel.labelButton,
            onClick = onClickRemindMe,
            modifier = Modifier.height(32.dp)
        )
    }
}

@Composable
private fun UpcomingCountDown(
    uiModel: UpcomingCampaignUiModel,
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
    val data = UpcomingCampaignUiModel(
        logoUrl = "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg",
        title = "Kerja Keras",
        endTimeUnix = 1703173097,
        timerLabel = "Akan datang",
        labelButton = "Ingatkan saya",
        paymentSpecific = "",
        backgroundColorString = ""
    )
    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            UpcomingCampaign(data, onTimerFinish = {}, onClickRemindMe = {})
            UpcomingCampaign(data.copy(paymentSpecific = "Khusus pembayaran OVO"), onTimerFinish = {}, onClickRemindMe = {})
            UpcomingCampaign(
                data.copy(logoUrl = ""),
                onTimerFinish = {},
                onClickRemindMe = {}
            )
        }
    }
}
