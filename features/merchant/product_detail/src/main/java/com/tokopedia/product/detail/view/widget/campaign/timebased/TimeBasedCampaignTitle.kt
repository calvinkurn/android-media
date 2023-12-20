package com.tokopedia.product.detail.view.widget.campaign.timebased

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
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.widget.campaign.component.CampaignImage

@Composable
fun TimeBasedCampaignTitle(title: String, logoUrl: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (logoUrl.isNotBlank()) {
            CampaignImage(
                url = logoUrl,
                modifier = Modifier
                    .height(16.dp)
                    .wrapContentWidth()
            )
        } else {
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
