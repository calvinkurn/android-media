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

class NestChips private constructor() {

    enum class State {
        Default,
        Selected,
        Disabled,
        Alternate
    }

    sealed interface RightIcon {

        object None : RightIcon
        data class Chevron(val onClicked: () -> Unit) : RightIcon
        data class Clear(val onClicked: () -> Unit) : RightIcon
    }

    sealed interface Image {

        object None : Image
        data class Url(val url: String, val contentDescription: String) : Image
        data class Resource(@DrawableRes val resource: Int, val contentDescription: String) : Image
    }
}

@Composable
fun NestChips(
    text: String,
    modifier: Modifier = Modifier,
    isDashed: Boolean = false,
    state: NestChips.State = NestChips.State.Default,
    rightIcon: NestChips.RightIcon = NestChips.RightIcon.None,
    image: NestChips.Image = NestChips.Image.None,
    size: Size = Size.SMALL,
    onClick: () -> Unit = {}
) {
    val textColor = when (state) {
        NestChips.State.Default -> NestTheme.colors.NN._600
        NestChips.State.Selected, NestChips.State.Alternate -> NestTheme.colors.GN._500
        NestChips.State.Disabled -> NestTheme.colors.NN._400
    }

    val borderColor = when (state) {
        NestChips.State.Default -> NestTheme.colors.NN._300
        NestChips.State.Selected, NestChips.State.Alternate -> NestTheme.colors.GN._400
        NestChips.State.Disabled -> NestTheme.colors.NN._50
    }

    val backgroundColor = when (state) {
        NestChips.State.Default, NestChips.State.Alternate -> NestTheme.colors.NN._0
        NestChips.State.Selected -> NestTheme.colors.GN._50
        NestChips.State.Disabled -> NestTheme.colors.NN._50
    }

    val height = when (size) {
        Size.SMALL -> 32.dp
        Size.MEDIUM -> 40.dp
        Size.LARGE -> 48.dp
    }
    
    val rightIconPainter = when (rightIcon) {
        is NestChips.RightIcon.Chevron -> {
            painterResource(id = iconUnifyR.drawable.iconunify_chevron_down)
        }
        is NestChips.RightIcon.Clear -> {
            painterResource(id = iconUnifyR.drawable.iconunify_clear)
        }
        else -> null
    }

    val rightIconSize = when (size) {
        Size.SMALL, Size.MEDIUM -> 16.dp
        Size.LARGE -> 24.dp
    }

    val rightIconOnClicked = when (rightIcon) {
        is NestChips.RightIcon.Chevron -> rightIcon.onClicked
        is NestChips.RightIcon.Clear -> rightIcon.onClicked
        else -> {{}}
    }

    val paddingStart = when {
        image is NestChips.Image.Url -> 4.dp
        size == Size.LARGE -> 12.dp
        else -> 8.dp
    }
    val paddingEnd = when (size) {
        Size.LARGE -> 12.dp
        else -> 8.dp
    }

    val chevronColor = if (state == NestChips.State.Selected) {
        NestTheme.colors.GN._500
    } else {
        NestTheme.colors.NN._900
    }

    val cornerRadius = when (size) {
        Size.SMALL -> 10.dp
        Size.MEDIUM -> 12.dp
        Size.LARGE -> 8.dp
    } // px tbd
    val maxLines = if (size == Size.LARGE) 2 else 1

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
    image: NestChips.Image,
    chipsSize: Size,
    modifier: Modifier = Modifier
) {
    val imageSize = if (chipsSize == Size.LARGE && image is NestChips.Image.Url) {
        40.dp
    } else {
        24.dp
    }

    val imageModifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .requiredSize(imageSize)

    when (image) {
        is NestChips.Image.Resource -> {
            Image(
                painter = painterResource(id = image.resource),
                contentDescription = image.contentDescription,
                modifier = imageModifier
            )
        }
        is NestChips.Image.Url -> {
            NestImage(
                imageUrl = image.url,
                modifier = imageModifier
            )
        }
        else -> {}
    }
}

enum class Size { SMALL, MEDIUM, LARGE }





@Preview("chip preview")
@Preview("chip preview (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChip() {
    NestTheme {
        Surface {
            var state by remember { mutableStateOf(NestChips.State.Default) }
            var size by remember { mutableStateOf(Size.SMALL) }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = size == Size.SMALL, onCheckedChange = { size = Size.SMALL })
                    Text("S", fontWeight = FontWeight.Bold)
                    Checkbox(
                        checked = size == Size.MEDIUM,
                        onCheckedChange = { size = Size.MEDIUM })
                    Text("M", fontWeight = FontWeight.Bold)
                    Checkbox(checked = size == Size.LARGE, onCheckedChange = { size = Size.LARGE })
                    Text("L", fontWeight = FontWeight.Bold)
                }
                NestChips(text = "Normal", state = state, size = size) {
                    state = if (state == NestChips.State.Default) {
                        NestChips.State.Selected
                    } else {
                        NestChips.State.Selected
                    }
                }
                NestChips(text = "Chevron", state = NestChips.State.Default, size = size)
            }
        }
    }
}

@Preview("State Preview")
@Preview("State Preview (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChipState(
    @PreviewParameter(NestChipsStatePreviewParameterProvider::class) state: NestChips.State,
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
    @PreviewParameter(NestChipsRightIconPreviewParameterProvider::class) rightIcon: NestChips.RightIcon,
) {
    NestTheme {
        Column {
            NestChips(
                text = "Chips Small",
                size = Size.SMALL,
                rightIcon = rightIcon,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Medium",
                size = Size.MEDIUM,
                rightIcon = rightIcon,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Large",
                size = Size.LARGE,
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
                size = Size.MEDIUM,
                isDashed = false,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Dashed",
                size = Size.MEDIUM,
                isDashed = true,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

private class NestChipsStatePreviewParameterProvider : PreviewParameterProvider<NestChips.State> {
    override val values = NestChips.State.values().asSequence()
}

private class NestChipsRightIconPreviewParameterProvider : PreviewParameterProvider<NestChips.RightIcon> {
    override val values = sequenceOf(
        NestChips.RightIcon.Chevron {},
        NestChips.RightIcon.Clear {},
    )
}
