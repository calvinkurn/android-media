package com.tokopedia.tkpd.comparison

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun ColumnHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 32.dp),
        text = text,
        style = NestTheme.typography.heading6,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ExpDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp),
        color = Color.LightGray
    )
}
