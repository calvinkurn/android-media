package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.extensions.applyIf
import com.tokopedia.common_compose.extensions.dashedStroke
import com.tokopedia.common_compose.extensions.noRippleClickable
import com.tokopedia.common_compose.principles.NestImage
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.common_compose.utils.NoMinimumTouchViewConfiguration
import com.tokopedia.iconunify.R as iconUnifyR

enum class NestChipsState { Default, Selected, Disabled, Alternate }

sealed interface NestChipsRight {

    val onClicked: () -> Unit
    val contentDescription: String
    data class Chevron(override val onClicked: () -> Unit) : NestChipsRight {
        override val contentDescription: String = "Dropdown"
    }
    data class Clear(override val onClicked: () -> Unit) : NestChipsRight {
        override val contentDescription: String = "Clear"
    }
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
        val color: androidx.compose.ui.graphics.Color? = null,
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
        modifier = modifier
            .requiredHeight(height)
            .applyIf(isDashed) {
                dashedStroke(1.dp, cornerRadius, borderColor, 4.dp)
            }
            .clip(RoundedCornerShape(cornerRadius))
            .clickable { onClick() }
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
            if (right != null && rightPainter != null) {
                CompositionLocalProvider(
                    LocalViewConfiguration provides NoMinimumTouchViewConfiguration()
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = rightPainter,
                        contentDescription = right.contentDescription,
                        colorFilter = ColorFilter.tint(chevronColor),
                        modifier = Modifier
                            .requiredSize(rightSize)
                            .noRippleClickable { right.onClicked() }
                    )
                }
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
                colorFilter = left.color?.let { ColorFilter.tint(it) },
                contentDescription = left.contentDescription,
                modifier = leftModifier,
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

private data class Variant(
    val state: NestChipsState,
    val right: NestChipsRight,
    val left: NestChipsLeft,
    val size: NestChipsSize
)

@Composable
private fun getSampleVariants(): List<Variant> {
    val context = LocalContext.current
    val states = NestChipsState.values().toList()
    val sizes = NestChipsSize.values().toList()
    val rights = listOf(
        NestChipsRight.Chevron {
            Toast.makeText(context, "Chevron clicked", Toast.LENGTH_SHORT).show()
        },
        NestChipsRight.Clear {
            Toast.makeText(context, "Clear / Remove clicked", Toast.LENGTH_SHORT).show()
        },
    )
    val lefts = listOf(
        NestChipsLeft.Color(NestTheme.colors.RN._400),
        NestChipsLeft.Color(NestTheme.colors.RN._400, isCircle = true),
        NestChipsLeft.Painter(
            painterResource(id = iconUnifyR.drawable.iconunify_bell_filled),
            Color.Blue,
        ),
        NestChipsLeft.Painter(
            painterResource(id = iconUnifyR.drawable.iconunify_bell_filled),
            Color.Blue,
            isCircle = true,
        ),
        NestChipsLeft.NetworkImage(url = "https://news.stanford.edu/wp-content/uploads/2020/10/Birds_Culture-1-copy.jpg"),
        NestChipsLeft.NetworkImage(
            url = "https://news.stanford.edu/wp-content/uploads/2020/10/Birds_Culture-1-copy.jpg",
            isCircle = false,
        ),
    )

    return states.flatMap { state ->
        sizes.flatMap { size ->
            rights.flatMap { right ->
                lefts.map { left ->
                    Variant(state, right, left, size)
                }
            }
        }
    }
}

@Preview("All Chips Samples")
@Preview("All Chips Samples (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AllSamples() {
    val context = LocalContext.current

    val variants = getSampleVariants()

    var isDashed by remember {
        mutableStateOf(false)
    }

    NestTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Button(onClick = { isDashed = !isDashed }) {
                NestTypography(text = "Invert Dashed")
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(variants) { variant ->
                    NestChips(
                        text = "Chips ${variant.size.name} ${variant.state.name}",
                        isDashed = isDashed,
                        state = variant.state,
                        right = variant.right,
                        left = variant.left,
                        size = variant.size,
                        onClick = {
                            Toast.makeText(context, "Chips Clicked", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
