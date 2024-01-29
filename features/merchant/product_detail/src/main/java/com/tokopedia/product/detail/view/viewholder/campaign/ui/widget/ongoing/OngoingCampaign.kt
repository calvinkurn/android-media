package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.ongoing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.tokopedia.product.detail.view.util.asHtmlLink
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignStockPercentage
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignTimer
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignTitle
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.PaymentSpecific
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.campaignBackgroundColor
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.upcoming.secondToMs

/**
 * Created by yovi.putra on 19/12/23"
 * Project name: android-unify
 **/

@Composable
fun OngoingCampaign(uiModel: OngoingCampaignComposeUiModel, onTimerFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .campaignBackgroundColor(colorString = uiModel.backgroundColorString)
    ) {
        OngoingContent(uiModel = uiModel, onTimerFinish = onTimerFinish)

        if (uiModel.paymentSpecific.isNotBlank()) {
            PaymentSpecific(description = uiModel.paymentSpecific)
        }
    }
}

@Composable
private fun OngoingContent(uiModel: OngoingCampaignComposeUiModel, onTimerFinish: () -> Unit) {
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
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CampaignTitle(title = uiModel.title, logoUrl = uiModel.logoUrl, logoHeight = 14.dp)

            CampaignStock(uiModel)
        }
        CampaignCountDown(uiModel, onTimerFinish)
    }
}

@Composable
private fun CampaignCountDown(
    uiModel: OngoingCampaignComposeUiModel,
    onTimerFinish: () -> Unit
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp)
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

@Composable
private fun CampaignStock(uiModel: OngoingCampaignComposeUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CampaignStockPercentage(
            modifier = Modifier
                .wrapContentHeight()
                .width(64.dp),
            value = uiModel.stockPercentage
        )
        NestTypography(
            text = uiModel.stockLabel.asHtmlLink,
            textStyle = NestTheme.typography.small.copy(
                color = Color.White
            ),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun OngoingCampaignPreview() {
    val data = OngoingCampaignComposeUiModel(
        logoUrl = "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg",
        title = "Kerja Keras",
        endTimeUnix = 1703086697,
        timerLabel = "Berakhir dalam",
        stockPercentage = 30,
        stockLabel = "Habiss",
        backgroundColorString = "862E31",
        paymentSpecific = "Kusus bayar dengan OVO"
    )

    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            OngoingCampaign(data.copy(stockPercentage = 30)) {}
            OngoingCampaign(data.copy(stockPercentage = 75)) {}
            OngoingCampaign(data.copy(logoUrl = "")) {}
            OngoingCampaign(data.copy(paymentSpecific = "")) {}
        }
    }
}
