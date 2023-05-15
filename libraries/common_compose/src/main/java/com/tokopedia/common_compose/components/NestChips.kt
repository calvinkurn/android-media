package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.extensions.applyIf
import com.tokopedia.common_compose.extensions.dashedStroke
import com.tokopedia.common_compose.principles.NestImage
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R as iconUnifyR

enum class NestChipsState { Default, Selected, Disabled, Alternate }

sealed interface NestChipsRight {

    object None : NestChipsRight
    data class Chevron(val onClicked: () -> Unit) : NestChipsRight
    data class Clear(val onClicked: () -> Unit) : NestChipsRight
}

sealed interface NestChipsImage {

    object None : NestChipsImage
    data class Url(val url: String, val contentDescription: String) : NestChipsImage
    data class Resource(@DrawableRes val resource: Int, val contentDescription: String) : NestChipsImage
}

enum class NestChipsSize { Small, Medium, Large }

@Composable
fun NestChips(
    text: String,
    modifier: Modifier = Modifier,
    isDashed: Boolean = false,
    state: NestChipsState = NestChipsState.Default,
    rightIcon: NestChipsRight = NestChipsRight.None,
    image: NestChipsImage = NestChipsImage.None,
    size: NestChipsSize = NestChipsSize.Small,
    onClick: () -> Unit = {}
) {
    val textColor = when (state) {
        NestChipsState.Default -> NestTheme.colors.NN._600
        NestChipsState.Selected, NestChipsState.Alternate -> NestTheme.colors.GN._500
        NestChipsState.Disabled -> NestTheme.colors.NN._400
    }

    val borderColor = when (state) {
        NestChipsState.Default -> NestTheme.colors.NN._300
        NestChipsState.Selected, NestChipsState.Alternate -> NestTheme.colors.GN._400
        NestChipsState.Disabled -> NestTheme.colors.NN._50
    }

    val backgroundColor = when (state) {
        NestChipsState.Default, NestChipsState.Alternate -> NestTheme.colors.NN._0
        NestChipsState.Selected -> NestTheme.colors.GN._50
        NestChipsState.Disabled -> NestTheme.colors.NN._50
    }

    val height = when (size) {
        NestChipsSize.Small -> 32.dp
        NestChipsSize.Medium -> 40.dp
        NestChipsSize.Large -> 48.dp
    }
    
    val rightIconPainter = when (rightIcon) {
        is NestChipsRight.Chevron -> {
            painterResource(id = iconUnifyR.drawable.iconunify_chevron_down)
        }
        is NestChipsRight.Clear -> {
            painterResource(id = iconUnifyR.drawable.iconunify_clear)
        }
        else -> null
    }

    val rightIconSize = when (size) {
        NestChipsSize.Small, NestChipsSize.Medium -> 16.dp
        NestChipsSize.Large -> 24.dp
    }

    val rightIconOnClicked = when (rightIcon) {
        is NestChipsRight.Chevron -> rightIcon.onClicked
        is NestChipsRight.Clear -> rightIcon.onClicked
        else -> {{}}
    }

    val paddingStart = when {
        image is NestChipsImage.Url -> 4.dp
        size == NestChipsSize.Large -> 12.dp
        else -> 8.dp
    }
    val paddingEnd = when (size) {
        NestChipsSize.Large -> 12.dp
        else -> 8.dp
    }

    val chevronColor = if (state == NestChipsState.Selected) {
        NestTheme.colors.GN._500
    } else {
        NestTheme.colors.NN._900
    }

    val cornerRadius = when (size) {
        NestChipsSize.Small -> 10.dp
        NestChipsSize.Medium -> 12.dp
        NestChipsSize.Large -> 8.dp
    }
    val maxLines = if (size == NestChipsSize.Large) 2 else 1

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier
            .requiredHeight(height)
            .clickable { onClick() }
            .applyIf(isDashed) {
                dashedStroke(1.dp, cornerRadius, borderColor, 4.dp)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = paddingStart, end = paddingEnd),
        ) {
            NestChipsImage(image = image, chipsSize = size, Modifier.padding(end = 8.dp))
            NestTypography(
                text = text,
                textStyle = NestTheme.typography.display2.copy(color = textColor),
                maxLines = maxLines
            )
            if (rightIconPainter != null) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = rightIconPainter,
                    contentDescription = "Dropdown Icon",
                    tint = chevronColor,
                    modifier = Modifier
                        .requiredSize(rightIconSize)
                        .clickable { rightIconOnClicked() }
                )
            }
        }
    }
}

@Composable
private fun NestChipsImage(
    image: NestChipsImage,
    chipsSize: NestChipsSize,
    modifier: Modifier = Modifier
) {
    val imageSize = if (chipsSize == NestChipsSize.Large && image is NestChipsImage.Url) {
        40.dp
    } else {
        24.dp
    }

    val imageModifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .requiredSize(imageSize)

    when (image) {
        is NestChipsImage.Resource -> {
            Image(
                painter = painterResource(id = image.resource),
                contentDescription = image.contentDescription,
                modifier = imageModifier
            )
        }
        is NestChipsImage.Url -> {
            NestImage(
                imageUrl = image.url,
                modifier = imageModifier
            )
        }
        else -> {}
    }
}

@Preview("chip preview")
@Preview("chip preview (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChip() {
    NestTheme {
        Surface {
            var state by remember { mutableStateOf(NestChipsState.Default) }
            var size by remember { mutableStateOf(NestChipsSize.Small) }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = size == NestChipsSize.Small, onCheckedChange = { size = NestChipsSize.Small })
                    Text("S", fontWeight = FontWeight.Bold)
                    Checkbox(
                        checked = size == NestChipsSize.Medium,
                        onCheckedChange = { size = NestChipsSize.Medium })
                    Text("M", fontWeight = FontWeight.Bold)
                    Checkbox(checked = size == NestChipsSize.Large, onCheckedChange = { size = NestChipsSize.Large })
                    Text("L", fontWeight = FontWeight.Bold)
                }
                NestChips(text = "Normal", state = state, size = size) {
                    state = if (state == NestChipsState.Default) {
                        NestChipsState.Selected
                    } else {
                        NestChipsState.Selected
                    }
                }
                NestChips(text = "Chevron", state = NestChipsState.Default, size = size)
            }
        }
    }
}

@Preview("State Preview")
@Preview("State Preview (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChipState(
    @PreviewParameter(NestChipsStatePreviewParameterProvider::class) state: NestChipsState,
) {
    NestTheme {
        NestChips(
            text = "Chips Label",
            state = state,
            modifier = Modifier.height(70.dp)
        )
    }
}

@Preview("Right Icon Preview")
@Preview("Right Icon (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChipRightIcon(
    @PreviewParameter(NestChipsRightIconPreviewParameterProvider::class) rightIcon: NestChipsRight,
) {
    NestTheme {
        Column {
            NestChips(
                text = "Chips Small",
                size = NestChipsSize.Small,
                rightIcon = rightIcon,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Medium",
                size = NestChipsSize.Medium,
                rightIcon = rightIcon,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Large",
                size = NestChipsSize.Large,
                rightIcon = rightIcon,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview("Dashed")
@Preview("Dashed (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChipDashed() {
    NestTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NestChips(
                text = "Chips Not Dashed",
                size = NestChipsSize.Medium,
                isDashed = false,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Dashed",
                size = NestChipsSize.Medium,
                isDashed = true,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

private class NestChipsStatePreviewParameterProvider : PreviewParameterProvider<NestChipsState> {
    override val values = NestChipsState.values().asSequence()
}

private class NestChipsRightIconPreviewParameterProvider : PreviewParameterProvider<NestChipsRight> {
    override val values = sequenceOf(
        NestChipsRight.Chevron {},
        NestChipsRight.Clear {},
    )
}
