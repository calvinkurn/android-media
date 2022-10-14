package com.tokopedia.common_compose.principles

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTypography(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN600)
) {
    Text(
        text = text,
        modifier = modifier,
        style = textStyle
    )
}

@Preview(name = "Typography")
@Composable
fun NestTypographyPreview() {
    NestTypography(
        text = "Flash Sale",
        Modifier
    )
}

@Preview(name = "Typography (Bold)")
@Composable
fun NestTypographyBoldPreview() {
    NestTypography(
        text = "Flash Sale",
        Modifier,
        textStyle = NestTheme.typography.display3.copy(fontWeight = FontWeight.Bold)
    )
}