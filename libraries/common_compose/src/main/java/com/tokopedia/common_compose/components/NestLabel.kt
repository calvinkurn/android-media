package com.tokopedia.common_compose.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestLabel(
    modifier: Modifier = Modifier,
    labelText: String,
    labelType: NestLabelType
) {

    val backgroundColor = labelType.toBackgroundColor()
    val textColor = labelType.toTextColor()

    Surface(
        modifier = modifier.height(20.dp),
        shape = RoundedCornerShape(3.dp),
        color = backgroundColor
    ) {
        NestTypography(
            text = labelText,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            textStyle = NestTheme.typography.small.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            ),
            maxLines = 1
        )
    }
}

enum class NestLabelType {
    HIGHLIGHT_LIGHT_GREEN,
    HIGHLIGHT_LIGHT_BLUE,
    HIGHLIGHT_LIGHT_ORANGE,
    HIGHLIGHT_LIGHT_RED,
    HIGHLIGHT_LIGHT_TEAL,
    HIGHLIGHT_LIGHT_GREY,

    HIGHLIGHT_DARK_GREEN,
    HIGHLIGHT_DARK_BLUE,
    HIGHLIGHT_DARK_ORANGE,
    HIGHLIGHT_DARK_RED,
    HIGHLIGHT_DARK_TEAL,
    HIGHLIGHT_DARK_GREY,

    HIGHLIGHT_DARK_IMAGE_LABEL,

    GENERAL_ORANGE,
    GENERAL_RED,
    GENERAL_BLUE,
    GENERAL_GREEN,
    GENERAL_TEAL,
    GENERAL_GREY
}

@Composable
private fun NestLabelType.toBackgroundColor(): Color {
    return when (this) {
        NestLabelType.HIGHLIGHT_LIGHT_GREEN -> NestTheme.colors.GN._100
        NestLabelType.HIGHLIGHT_LIGHT_ORANGE -> NestTheme.colors.YN._100
        NestLabelType.HIGHLIGHT_LIGHT_GREY -> NestTheme.colors.NN._100
        NestLabelType.HIGHLIGHT_LIGHT_RED -> NestTheme.colors.RN._100
        NestLabelType.HIGHLIGHT_LIGHT_BLUE -> NestTheme.colors.BN._100
        NestLabelType.HIGHLIGHT_LIGHT_TEAL -> NestTheme.colors.TN._100

        NestLabelType.HIGHLIGHT_DARK_GREEN -> NestTheme.colors.GN._500
        NestLabelType.HIGHLIGHT_DARK_BLUE -> NestTheme.colors.BN._500
        NestLabelType.HIGHLIGHT_DARK_ORANGE -> NestTheme.colors.YN._500
        NestLabelType.HIGHLIGHT_DARK_RED -> NestTheme.colors.RN._500
        NestLabelType.HIGHLIGHT_DARK_TEAL -> NestTheme.colors.TN._500
        NestLabelType.HIGHLIGHT_DARK_GREY -> if (isSystemInDarkTheme()) NestTheme.colors.NN._400 else NestTheme.colors.NN._600

        NestLabelType.HIGHLIGHT_DARK_IMAGE_LABEL -> {
            val imageLabelColorDark = Color(0xB3E4EBF5) //NN100 70%
            val imageLabelColorLight = Color(0xB32E3137) //NN900 70%

            if (isSystemInDarkTheme()) {
                imageLabelColorDark
            } else {
                imageLabelColorLight
            }
        }
        NestLabelType.GENERAL_ORANGE -> Color.Transparent
        NestLabelType.GENERAL_RED -> Color.Transparent
        NestLabelType.GENERAL_BLUE -> Color.Transparent
        NestLabelType.GENERAL_GREEN -> Color.Transparent
        NestLabelType.GENERAL_TEAL -> Color.Transparent
        NestLabelType.GENERAL_GREY -> Color.Transparent
    }
}

@Composable
private fun NestLabelType.toTextColor(): Color {
    return when (this) {
        NestLabelType.HIGHLIGHT_LIGHT_GREEN -> if (isSystemInDarkTheme()) NestTheme.colors.GN._800 else NestTheme.colors.GN._500
        NestLabelType.HIGHLIGHT_LIGHT_ORANGE -> if (isSystemInDarkTheme()) NestTheme.colors.YN._800 else NestTheme.colors.YN._500
        NestLabelType.HIGHLIGHT_LIGHT_GREY -> if (isSystemInDarkTheme()) NestTheme.colors.NN._800 else NestTheme.colors.NN._600
        NestLabelType.HIGHLIGHT_LIGHT_RED -> if (isSystemInDarkTheme()) NestTheme.colors.RN._800 else NestTheme.colors.RN._500
        NestLabelType.HIGHLIGHT_LIGHT_BLUE -> if (isSystemInDarkTheme()) NestTheme.colors.BN._800 else NestTheme.colors.BN._500
        NestLabelType.HIGHLIGHT_LIGHT_TEAL -> if (isSystemInDarkTheme()) NestTheme.colors.TN._800 else NestTheme.colors.TN._500

        NestLabelType.HIGHLIGHT_DARK_GREEN -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0
        NestLabelType.HIGHLIGHT_DARK_BLUE -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0
        NestLabelType.HIGHLIGHT_DARK_ORANGE -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0
        NestLabelType.HIGHLIGHT_DARK_RED -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0
        NestLabelType.HIGHLIGHT_DARK_TEAL -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0
        NestLabelType.HIGHLIGHT_DARK_GREY -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0

        NestLabelType.HIGHLIGHT_DARK_IMAGE_LABEL -> if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0

        NestLabelType.GENERAL_ORANGE -> if (isSystemInDarkTheme()) NestTheme.colors.YN._800 else NestTheme.colors.YN._500
        NestLabelType.GENERAL_RED -> if (isSystemInDarkTheme()) NestTheme.colors.RN._800 else NestTheme.colors.RN._500
        NestLabelType.GENERAL_BLUE -> if (isSystemInDarkTheme()) NestTheme.colors.BN._800 else NestTheme.colors.BN._500
        NestLabelType.GENERAL_GREEN -> if (isSystemInDarkTheme()) NestTheme.colors.GN._800 else NestTheme.colors.GN._500
        NestLabelType.GENERAL_TEAL -> if (isSystemInDarkTheme()) NestTheme.colors.TN._800 else NestTheme.colors.TN._500
        NestLabelType.GENERAL_GREY -> if (isSystemInDarkTheme()) NestTheme.colors.NN._800 else NestTheme.colors.NN._500
    }
}

//region Preview: Device preview

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF222329,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun NestLabelPreview() {
    NestTheme {
        Column {
            NestHeader(title = "NestLabel Preview")
            NestLabelList()
        }
    }
}

@Composable
private fun NestLabelList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        NestTypography(text = "Highlight Variant - Light", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NestLabel(
                labelText = "Light - Green",
                labelType = NestLabelType.HIGHLIGHT_LIGHT_GREEN
            )
            NestLabel(
                labelText = "Light - Orange",
                labelType = NestLabelType.HIGHLIGHT_LIGHT_ORANGE
            )
            NestLabel(
                labelText = "Light - Grey",
                labelType = NestLabelType.HIGHLIGHT_LIGHT_GREY
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NestLabel(
                labelText = "Light - Red",
                labelType = NestLabelType.HIGHLIGHT_LIGHT_RED
            )
            NestLabel(
                labelText = "Light - Blue",
                labelType = NestLabelType.HIGHLIGHT_LIGHT_BLUE
            )
            NestLabel(
                labelText = "Light - Teal",
                labelType = NestLabelType.HIGHLIGHT_LIGHT_TEAL
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        NestTypography(text = "Highlight Variant - Dark", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NestLabel(
                labelText = "Dark - Green",
                labelType = NestLabelType.HIGHLIGHT_DARK_GREEN
            )
            NestLabel(
                labelText = "Dark - Orange",
                labelType = NestLabelType.HIGHLIGHT_DARK_ORANGE
            )
            NestLabel(
                labelText = "Dark - Grey",
                labelType = NestLabelType.HIGHLIGHT_DARK_GREY
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NestLabel(
                labelText = "Dark - Red",
                labelType = NestLabelType.HIGHLIGHT_DARK_RED
            )
            NestLabel(
                labelText = "Dark - Blue",
                labelType = NestLabelType.HIGHLIGHT_DARK_BLUE
            )
            NestLabel(
                labelText = "Dark - Teal",
                labelType = NestLabelType.HIGHLIGHT_DARK_TEAL
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        NestTypography(text = "General Variant", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NestLabel(
                labelText = "General - Orange",
                labelType = NestLabelType.GENERAL_ORANGE
            )
            NestLabel(
                labelText = "General - Red",
                labelType = NestLabelType.GENERAL_RED
            )
            NestLabel(
                labelText = "General - Blue",
                labelType = NestLabelType.GENERAL_BLUE
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NestLabel(
                labelText = "General - Green",
                labelType = NestLabelType.GENERAL_GREEN
            )
            NestLabel(
                labelText = "General - Teal",
                labelType = NestLabelType.GENERAL_TEAL
            )
            NestLabel(
                labelText = "General - Grey",
                labelType = NestLabelType.GENERAL_GREY
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        NestTypography(text = "Label on Image", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NestTheme.colors.NN._300),
                    contentAlignment = Alignment.Center,
                ) {
                    NestTypography(text = "Sample image", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                }
                NestLabel(
                    modifier = Modifier.offset(y = (-8).dp, x = (-8).dp),
                    labelText = "Dark - Label on Image",
                    labelType = NestLabelType.HIGHLIGHT_DARK_IMAGE_LABEL
                )
            }
        }
    }
}
//endregion
