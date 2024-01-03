package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun CampaignName(
    title: String,
    logoUrl: String,
    modifier: Modifier = Modifier,
    logoHeight: Dp = 16.dp,
    textColor: Color = Color.White
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (logoUrl.isNotBlank()) {
            CampaignImage(
                url = logoUrl,
                modifier = Modifier
                    .height(logoHeight)
                    .wrapContentWidth()
            )
        } else {
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.display3.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
