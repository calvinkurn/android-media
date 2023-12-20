package com.tokopedia.product.detail.view.widget.campaign.regular

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.widget.campaign.component.CampaignName
import com.tokopedia.product.detail.view.widget.campaign.component.backgroundColor

/**
 * Created by yovi.putra on 20/12/23"
 * Project name: android-unify
 **/

@Composable
fun RegularCampaign(
    title: String,
    logoUrl: String,
    backgroundColorString: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .backgroundColor(colorString = backgroundColorString)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CampaignName(title = title, logoUrl = logoUrl, logoHeight = 14.dp, textColor = NestNN.light._950)
    }
}

@Preview
@Composable
fun RegularCampaignPreview() {
    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            val logoUrl = "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg"
            RegularCampaign(title = "Serbu Offial Store", logoUrl = logoUrl, backgroundColorString = "DDD8FF, 47DB6D")
            RegularCampaign(title = "Serbu Offial Store", logoUrl = "", backgroundColorString = "DDD8FF")
            RegularCampaign(title = "Serbu Offial Store", logoUrl = logoUrl, backgroundColorString = "   DDD8FF")
            RegularCampaign(title = "Serbu Offial Store", logoUrl = "", backgroundColorString = "DDD8FF")
            RegularCampaign(title = "Serbu Offial Store", logoUrl = logoUrl, backgroundColorString = "#DDD8FF, 47DB6D,DDD8FF,")
        }
    }
}
