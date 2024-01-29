package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.thematic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignImage
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignTitle
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.campaignBackgroundColor

/**
 * Created by yovi.putra on 20/12/23"
 * Project name: android-unify
 **/

@Composable
fun ThematicCampaign(
    title: String,
    logoUrl: String,
    backgroundColorString: String,
    superGraphicUrl: String = ""
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .campaignBackgroundColor(colorString = backgroundColorString)
    ) {
        val (logo, superGraphic) = createRefs()
        val isMegaThematic = superGraphicUrl.isNotBlank()
        val logoTitleHeight = if (isMegaThematic) 16.dp else 14.dp

        if (isMegaThematic) {
            CampaignImage(
                url = superGraphicUrl,
                alignment = Alignment.CenterEnd,
                modifier = Modifier
                    .height(32.dp)
                    .wrapContentWidth()
                    .constrainAs(superGraphic) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }

        CampaignTitle(
            title = title,
            logoUrl = logoUrl,
            logoHeight = logoTitleHeight,
            textColor = NestNN.light._950,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun MegaCampaignPreview() {
    val logoUrl = "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg"
    val superGraphicUrl =
        "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg"
    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            ThematicCampaign(
                title = "Ramadhan Extra",
                logoUrl = logoUrl,
                superGraphicUrl = superGraphicUrl,
                backgroundColorString = "16754E, 47DB6D"
            )

            ThematicCampaign(
                title = "Ramadhan Extra",
                logoUrl = "",
                superGraphicUrl = superGraphicUrl,
                backgroundColorString = "16754E, 47DB6D"
            )

            ThematicCampaign(title = "Serbu Offial Store", logoUrl = logoUrl, backgroundColorString = "DDD8FF, 47DB6D")
            ThematicCampaign(title = "Serbu Offial Store", logoUrl = "", backgroundColorString = "DDD8FF")
            ThematicCampaign(title = "Serbu Offial Store", logoUrl = logoUrl, backgroundColorString = "   DDD8FF")
            ThematicCampaign(title = "Serbu Offial Store", logoUrl = logoUrl, backgroundColorString = "DDD8FF, 47DB6D,DDD8FF,")
        }
    }
}
