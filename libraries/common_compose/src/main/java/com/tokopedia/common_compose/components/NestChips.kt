package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
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
import com.tokopedia.unifyprinciples.R as unifyR

enum class NestChipsState { Default, Selected, Disabled, Alternate }

sealed interface NestChipsRight {

    data class Chevron(val onClicked: () -> Unit) : NestChipsRight
    data class Clear(val onClicked: () -> Unit) : NestChipsRight
}

sealed interface NestChipsLeft {

    val isCircle: Boolean

    data class Color(
        val color: androidx.compose.ui.graphics.Color,
        override val isCircle: Boolean = false,
    ) : NestChipsLeft

    data class NetworkImage(
        val url: String,
        val contentDescription: String? = null,
        override val isCircle: Boolean = false,
    ) : NestChipsLeft

    data class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
        val contentDescription: String? = null,
        override val isCircle: Boolean = false,
    ) : NestChipsLeft
}

enum class NestChipsSize { Small, Medium, Large }

@Composable
fun NestChips(
    text: String,
    modifier: Modifier = Modifier,
    isDashed: Boolean = false,
    state: NestChipsState = NestChipsState.Default,
    right: NestChipsRight? = null,
    left: NestChipsLeft? = null,
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
    
    val rightPainter = when (right) {
        is NestChipsRight.Chevron -> {
            painterResource(id = iconUnifyR.drawable.iconunify_chevron_down)
        }
        is NestChipsRight.Clear -> {
            painterResource(id = iconUnifyR.drawable.iconunify_clear)
        }
        else -> null
    }

    val rightSize = when (size) {
        NestChipsSize.Small, NestChipsSize.Medium -> 16.dp
        NestChipsSize.Large -> 24.dp
    }

    val rightOnClicked = when (right) {
        is NestChipsRight.Chevron -> right.onClicked
        is NestChipsRight.Clear -> right.onClicked
        else -> {{}}
    }

    val paddingStart = when {
        left is NestChipsLeft.NetworkImage -> 4.dp
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
            NestChipsLeft(left = left, chipsSize = size, Modifier.padding(end = 8.dp))
            NestTypography(
                text = text,
                textStyle = NestTheme.typography.display2.copy(color = textColor),
                maxLines = maxLines
            )
            if (rightPainter != null) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = rightPainter,
                    contentDescription = "Dropdown Icon",
                    tint = chevronColor,
                    modifier = Modifier
                        .requiredSize(rightSize)
                        .clickable { rightOnClicked() }
                )
            }
        }
    }
}

@Composable
private fun NestChipsLeft(
    left: NestChipsLeft?,
    chipsSize: NestChipsSize,
    modifier: Modifier = Modifier
) {
    val leftSize = if (chipsSize == NestChipsSize.Large && left is NestChipsLeft.NetworkImage) {
        40.dp
    } else {
        24.dp
    }

    val leftModifier = modifier
        .clip(if (left?.isCircle == true) CircleShape else RoundedCornerShape(8.dp))
        .requiredSize(leftSize)

    when (left) {
        is NestChipsLeft.Painter -> {
            Image(
                painter = left.painter,
                contentDescription = left.contentDescription,
                modifier = leftModifier
            )
        }
        is NestChipsLeft.NetworkImage -> {
            NestImage(
                imageUrl = left.url,
                modifier = leftModifier
            )
        }
        is NestChipsLeft.Color -> {
            Surface(
                modifier = leftModifier,
                color = left.color,
            ) {}
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

@Preview("Right Preview")
@Preview("Right (Dark) Preview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChipRight(
    @PreviewParameter(NestChipsRightPreviewParameterProvider::class) right: NestChipsRight,
) {
    NestTheme {
        Column {
            NestChips(
                text = "Chips Small",
                size = NestChipsSize.Small,
                right = right,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Medium",
                size = NestChipsSize.Medium,
                right = right,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips Large",
                size = NestChipsSize.Large,
                right = right,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview("Image Preview")
@Preview("Image (Dark) Preview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChipLeft(
    @PreviewParameter(NestChipsSizePreviewParameterProvider::class) size: NestChipsSize,
) {
    NestTheme {
        Column {
            NestChips(
                text = "Chips Without Left",
                size = size,
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips With Color",
                size = size,
                left = NestChipsLeft.Color(colorResource(id = unifyR.color.Unify_R400)),
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips With Color (Circle)",
                size = size,
                left = NestChipsLeft.Color(colorResource(id = unifyR.color.Unify_R400), isCircle = true),
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips With Painter",
                size = size,
                left = NestChipsLeft.Painter(painterResource(id = iconUnifyR.drawable.iconunify_bell_filled)),
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips With Painter (Circle)",
                size = size,
                left = NestChipsLeft.Painter(painterResource(id = iconUnifyR.drawable.iconunify_bell_filled), isCircle = true),
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips With Network Image",
                size = size,
                left = NestChipsLeft.NetworkImage(url = "https://news.stanford.edu/wp-content/uploads/2020/10/Birds_Culture-1-copy.jpg"),
                modifier = Modifier.padding(8.dp)
            )

            NestChips(
                text = "Chips With Network Image (Circle)",
                size = size,
                left = NestChipsLeft.NetworkImage(url = "https://news.stanford.edu/wp-content/uploads/2020/10/Birds_Culture-1-copy.jpg", isCircle = true),
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

private class NestChipsRightPreviewParameterProvider : PreviewParameterProvider<NestChipsRight> {
    override val values = sequenceOf(
        NestChipsRight.Chevron {},
        NestChipsRight.Clear {},
    )
}

private class NestChipsSizePreviewParameterProvider : PreviewParameterProvider<NestChipsSize> {
    override val values = NestChipsSize.values().asSequence()
}
