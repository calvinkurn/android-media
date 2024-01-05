package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.view.util.asHtmlLink

@Composable
fun PaymentSpecific(description: String, modifier: Modifier = Modifier) {
    NestTypography(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = NestNN.light._900.copy(alpha = 0.48f))
            .padding(horizontal = 16.dp, vertical = 4.dp),
        text = description.asHtmlLink,
        textStyle = NestTheme.typography.small.copy(
            color = Color.White
        ),
        overflow = TextOverflow.Ellipsis,
        maxLines = Int.ONE
    )
}
