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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestLabel(
    modifier: Modifier = Modifier,
    labelText: String,
    labelType: NestLabelType
) {
    val backgroundColor = when (labelType) {
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

        NestLabelType.HIGHLIGHT_DARK_IMAGE_LABEL -> if (isSystemInDarkTheme()) ImageLabelColorDark else ImageLabelColorLight
    }

    val textColor = when (labelType) {
        NestLabelType.HIGHLIGHT_LIGHT_GREEN -> if (isSystemInDarkTheme()) NestTheme.colors.GN._800 else NestTheme.colors.GN._500
        NestLabelType.HIGHLIGHT_LIGHT_ORANGE -> if (isSystemInDarkTheme()) NestTheme.colors.YN._800 else NestTheme.colors.YN._500
        NestLabelType.HIGHLIGHT_LIGHT_GREY -> if (isSystemInDarkTheme()) NestTheme.colors.NN._800 else NestTheme.colors.NN._500
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
    }

    Surface(
        modifier = modifier.height(20.dp),
        shape = RoundedCornerShape(3.dp),
        color = backgroundColor
    ) {
        Text(
            text = labelText,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            color = textColor,
            maxLines = 1,
            style = NestTheme.typography.small.copy(fontWeight = FontWeight.Bold)
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
}

//region Preview: Light
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelGreenPreview() {
    NestTheme {
        NestLabel(
            labelText = "Light - Green",
            labelType = NestLabelType.HIGHLIGHT_LIGHT_GREEN
        )
    }
}
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelOrangePreview() {
    NestTheme {
        NestLabel(
            labelText = "Light - Orange",
            labelType = NestLabelType.HIGHLIGHT_LIGHT_ORANGE
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelGreyPreview() {
    NestTheme {
        NestLabel(
            labelText = "Light - Grey",
            labelType = NestLabelType.HIGHLIGHT_LIGHT_GREY
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelRedPreview() {
    NestTheme {
        NestLabel(
            labelText = "Light - Red",
            labelType = NestLabelType.HIGHLIGHT_LIGHT_RED
        )
    }
}
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelBluePreview() {
    NestTheme {
        NestLabel(
            labelText = "Light - Blue",
            labelType = NestLabelType.HIGHLIGHT_LIGHT_BLUE
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelTealPreview() {
    NestTheme {
        NestLabel(
            labelText = "Light - Teal",
            labelType = NestLabelType.HIGHLIGHT_LIGHT_TEAL
        )
    }
}

//endregion

//region Preview: Dark
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestDarkLabelGreenPreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Green",
            labelType = NestLabelType.HIGHLIGHT_DARK_GREEN
        )
    }
}
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestDarkLabelOrangePreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Orange",
            labelType = NestLabelType.HIGHLIGHT_DARK_ORANGE
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestDarkLabelGreyPreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Grey",
            labelType = NestLabelType.HIGHLIGHT_DARK_GREY
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestDarkLabelRedPreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Red",
            labelType = NestLabelType.HIGHLIGHT_DARK_RED
        )
    }
}
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestDarkLabelBluePreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Blue",
            labelType = NestLabelType.HIGHLIGHT_DARK_BLUE
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestDarkLabelTealPreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Teal",
            labelType = NestLabelType.HIGHLIGHT_DARK_TEAL
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestLightLabelOnImagePreview() {
    NestTheme {
        NestLabel(
            labelText = "Dark - Label on Image",
            labelType = NestLabelType.HIGHLIGHT_DARK_IMAGE_LABEL
        )
    }
}
//endregion

//region Preview: Device preview

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            NestTypography(text = "Light Variant", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))

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
                    labelText = "Light -Teal",
                    labelType = NestLabelType.HIGHLIGHT_LIGHT_TEAL
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            NestTypography(text = "Dark Variant", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NestTheme.colors.NN._600),
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
}

//endregion

//region Custom colors for NestLabel

//NN100 70%
private val ImageLabelColorDark = Color(0xB3E4EBF5)

//NN900 70%
private val ImageLabelColorLight = Color(0xB32E3137)

//endregion
