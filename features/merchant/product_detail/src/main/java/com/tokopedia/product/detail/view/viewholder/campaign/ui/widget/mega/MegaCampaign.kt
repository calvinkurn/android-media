package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.mega

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignImage
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.CampaignName
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component.campaignBackgroundColor

/**
 * Created by yovi.putra on 20/12/23"
 * Project name: android-unify
 **/

@Composable
fun MegaCampaign(
    title: String,
    logoUrl: String,
    superGraphicUrl: String,
    backgroundColorString: String
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .campaignBackgroundColor(colorString = backgroundColorString)
    ) {
        val (logo, superGraphic) = createRefs()

        CampaignImage(
            url = superGraphicUrl,
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .constrainAs(superGraphic) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )

        CampaignName(
            title = title,
            logoUrl = logoUrl,
            logoHeight = 16.dp,
            textColor = NestNN.light._950,
            modifier = Modifier
                .wrapContentWidth()
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )
    }
}

@Preview
@Composable
fun MegaCampaignPreview() {
    val logoUrl =
        "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg"
    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            MegaCampaign(
                title = "Ramadhan Extra",
                logoUrl = logoUrl,
                superGraphicUrl = logoUrl,
                backgroundColorString = "16754E, 47DB6D"
            )

            MegaCampaign(
                title = "Ramadhan Extra",
                logoUrl = "",
                superGraphicUrl = logoUrl,
                backgroundColorString = "16754E, 47DB6D"
            )
        }
    }
}
