package com.tokopedia.common_compose.principles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.header.NestHeaderVariant
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestHeader(
    modifier: Modifier = Modifier,
    variant: NestHeaderVariant = NestHeaderVariant.Default,
    title: String,
    showBackIcon: Boolean = true,
    onBackIconPressed: () -> Unit = {}
) {
    val headerBackground = getHeaderBackgroundColor(variant = variant)
    val contentColor = getHeaderContent(variant = variant)

    Surface(
        color = headerBackground,
        elevation = 1.dp,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            if (showBackIcon) {
                IconButton(
                    modifier = Modifier
                        .height(16.dp)
                        .width(18.dp),
                    onClick = onBackIconPressed
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = contentColor,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.display1.copy(
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            )
        }
    }
}

@Composable
private fun getHeaderBackgroundColor(variant: NestHeaderVariant) = when (variant) {
    is NestHeaderVariant.Transparent -> Color.Transparent
    else -> NestTheme.colors.NN._0
}

@Composable
private fun getHeaderContent(variant: NestHeaderVariant) = when (variant) {
    is NestHeaderVariant.Transparent -> NestNN.light._0
    else -> NestTheme.colors.NN._950
}

@Preview(name = "Header")
@Composable
fun NestHeaderPreview() {
    NestTheme(darkTheme = false) {
        Column {
            NestHeader(title = "Header Default Variant", showBackIcon = true)
            NestHeader(variant = NestHeaderVariant.Transparent, title = "Header Transparent Variant", showBackIcon = false)
        }
    }
}

@Preview(name = "Header (Dark)")
@Composable
fun NestHeaderDarkPreview() {
    NestTheme(darkTheme = true) {
        Column {
            NestHeader(title = "Header Default Variant", showBackIcon = true)
            NestHeader(variant = NestHeaderVariant.Transparent, title = "Header Transparent Variant", showBackIcon = false)
        }
    }
}
