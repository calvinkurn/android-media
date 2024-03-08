package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper

@Composable
fun CampaignTitle(
    title: String,
    logoUrl: String,
    modifier: Modifier = Modifier,
    logoHeight: Dp = 16.dp,
    textColor: Color = Color.White,
    fontWeight: FontWeight = FontWeight.Bold
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
            val htmlLink = rememberHtmlLink(text = title)

            NestTypography(
                text = htmlLink.toAnnotatedString(),
                textStyle = NestTheme.typography.display3.copy(
                    fontWeight = fontWeight,
                    color = textColor,
                    textAlign = TextAlign.Left
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
internal fun rememberHtmlLink(text: String) = run {
    val context = LocalContext.current
    remember(text) {
        HtmlLinkHelper(context, text).spannedString ?: ""
    }
}
