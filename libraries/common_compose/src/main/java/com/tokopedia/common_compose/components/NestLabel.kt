package com.tokopedia.common_compose.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestLabel(
    modifier: Modifier = Modifier,
    labelText: String,
    nestLabelType: NestLabelType
) {
    val backgroundColor = when (nestLabelType) {
        NestLabelType.HIGHLIGHT_GREEN -> NestTheme.colors.GN._100
        NestLabelType.HIGHLIGHT_ORANGE -> NestTheme.colors.YN._100
        NestLabelType.HIGHLIGHT_GREY -> NestTheme.colors.NN._100
        NestLabelType.HIGHLIGHT_RED -> NestTheme.colors.RN._100
        NestLabelType.HIGHLIGHT_BLUE -> NestTheme.colors.BN._100
        NestLabelType.HIGHLIGHT_TEAL -> NestTheme.colors.TN._100
        NestLabelType.HIGHLIGHT_IMAGE_LABEL -> if (isSystemInDarkTheme()) ImageLabelColorDark else ImageLabelColorLight
    }

    val textColor = when (nestLabelType) {
        NestLabelType.HIGHLIGHT_GREEN -> if (isSystemInDarkTheme()) NestTheme.colors.GN._500 else NestTheme.colors.GN._800
        NestLabelType.HIGHLIGHT_ORANGE -> if (isSystemInDarkTheme()) NestTheme.colors.YN._500 else NestTheme.colors.YN._800
        NestLabelType.HIGHLIGHT_GREY -> if (isSystemInDarkTheme()) NestTheme.colors.NN._600 else NestTheme.colors.NN._800
        NestLabelType.HIGHLIGHT_RED -> if (isSystemInDarkTheme()) NestTheme.colors.RN._500 else NestTheme.colors.RN._800
        NestLabelType.HIGHLIGHT_BLUE -> if (isSystemInDarkTheme()) NestTheme.colors.BN._500 else NestTheme.colors.BN._800
        NestLabelType.HIGHLIGHT_TEAL -> if (isSystemInDarkTheme()) NestTheme.colors.TN._500 else NestTheme.colors.TN._800
        NestLabelType.HIGHLIGHT_IMAGE_LABEL -> if (isSystemInDarkTheme()) NestTheme.colors.NN._0 else NestTheme.colors.NN._1000
    }

    Surface(
        modifier = modifier.height(20.dp),
        shape = RoundedCornerShape(3.dp),
        color = backgroundColor
    ) {
        Text(
            text = labelText,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            color = textColor,
            maxLines = 1,
            style = NestTheme.typography.small.copy(fontWeight = FontWeight.Bold)
        )
    }
}

enum class NestLabelType {
    HIGHLIGHT_GREEN,
    HIGHLIGHT_BLUE,
    HIGHLIGHT_ORANGE,
    HIGHLIGHT_RED,
    HIGHLIGHT_TEAL,
    HIGHLIGHT_GREY,
    HIGHLIGHT_IMAGE_LABEL,
}

//region Preview
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelGreenPreview() {
    NestTheme {
        NestLabel(
            labelText = "Green",
            nestLabelType = NestLabelType.HIGHLIGHT_GREEN
        )
    }
}
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelOrangePreview() {
    NestTheme {
        NestLabel(
            labelText = "Orange",
            nestLabelType = NestLabelType.HIGHLIGHT_ORANGE
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelGreyPreview() {
    NestTheme {
        NestLabel(
            labelText = "Grey",
            nestLabelType = NestLabelType.HIGHLIGHT_GREY
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelRedPreview() {
    NestTheme {
        NestLabel(
            labelText = "Red",
            nestLabelType = NestLabelType.HIGHLIGHT_RED
        )
    }
}
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelBluePreview() {
    NestTheme {
        NestLabel(
            labelText = "Blue",
            nestLabelType = NestLabelType.HIGHLIGHT_BLUE
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelTealPreview() {
    NestTheme {
        NestLabel(
            labelText = "Teal",
            nestLabelType = NestLabelType.HIGHLIGHT_TEAL
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLabelOnImagePreview() {
    NestTheme {
        NestLabel(
            labelText = "Label on Image",
            nestLabelType = NestLabelType.HIGHLIGHT_IMAGE_LABEL
        )
    }
}
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF222329,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun NestLabelOnDevicesPreview() {
    NestTheme {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NestLabel(labelText = "Green", nestLabelType = NestLabelType.HIGHLIGHT_GREEN)
                NestLabel(
                    labelText = "Orange",
                    nestLabelType = NestLabelType.HIGHLIGHT_ORANGE
                )
                NestLabel(labelText = "Grey", nestLabelType = NestLabelType.HIGHLIGHT_GREY)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NestLabel(labelText = "Red", nestLabelType = NestLabelType.HIGHLIGHT_RED)
                NestLabel(labelText = "Blue", nestLabelType = NestLabelType.HIGHLIGHT_BLUE)
                NestLabel(labelText = "Teal", nestLabelType = NestLabelType.HIGHLIGHT_TEAL)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NestLabel(
                    labelText = "Label on Image",
                    nestLabelType = NestLabelType.HIGHLIGHT_IMAGE_LABEL
                )
            }
        }
    }
}
//endregion

//region Custom colors for NestLabel

//NN100 70%
private val ImageLabelColorDark = Color(0xFF2E2F36)

//NN900 70%
private val ImageLabelColorLight = Color(0xB32E3137)

//endregion
