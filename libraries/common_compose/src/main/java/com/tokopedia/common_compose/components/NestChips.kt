package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.extensions.applyIf
import com.tokopedia.common_compose.extensions.dashedStroke
import com.tokopedia.common_compose.extensions.noRippleClickable
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
        override val isCircle: Boolean = false
    ) : NestChipsLeft

    data class NetworkImage(
        val url: String,
        val contentDescription: String? = null,
        override val isCircle: Boolean = false
    ) : NestChipsLeft

    data class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
        val color: androidx.compose.ui.graphics.Color? = null,
        val contentDescription: String? = null,
        override val isCircle: Boolean = false
    ) : NestChipsLeft
}

enum class NestChipsSize(
    val height: Dp,
    val rightSize: Dp,
    val paddingHorizontal: Dp,
    val cornerRadius: Dp,
    val maxLines: Int
) {
    Small(32.dp, 16.dp, 8.dp, 10.dp, 1),
    Medium(40.dp, 16.dp, 8.dp, 12.dp, 1),
    Large(48.dp, 24.dp, 12.dp, 8.dp, 2)
}

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

    val rightPainter = when (right) {
        is NestChipsRight.Chevron -> {
            painterResource(id = iconUnifyR.drawable.iconunify_chevron_down)
        }
        is NestChipsRight.Clear -> {
            painterResource(id = iconUnifyR.drawable.iconunify_clear)
        }
        else -> null
    }

    val paddingStart = when (left) {
        is NestChipsLeft.NetworkImage -> 4.dp
        else -> size.paddingHorizontal
    }

    val chevronColor = if (state == NestChipsState.Selected) {
        NestTheme.colors.GN._500
    } else {
        NestTheme.colors.NN._900
    }

    Surface(
        color = backgroundColor,
        modifier = modifier
            .requiredHeight(size.height)
            .applyIf(isDashed) {
                dashedStroke(1.dp, size.cornerRadius, borderColor, 4.dp)
            }
            .clip(RoundedCornerShape(size.cornerRadius))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = paddingStart, end = size.paddingHorizontal)
        ) {
            NestChipsLeft(left = left, chipsSize = size, Modifier.padding(end = 8.dp))
            NestTypography(
                text = text,
                textStyle = NestTheme.typography.display2.copy(color = textColor),
                maxLines = size.maxLines
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
                            .requiredSize(size.rightSize)
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

    left?.let {
        when (it) {
            is NestChipsLeft.Painter -> {
                Image(
                    painter = it.painter,
                    colorFilter = it.color?.let { color -> ColorFilter.tint(color) },
                    contentDescription = it.contentDescription,
                    modifier = leftModifier
                )
            }
            is NestChipsLeft.NetworkImage -> {
                NestImage(
                    imageUrl = it.url,
                    modifier = leftModifier
                )
            }
            is NestChipsLeft.Color -> {
                Surface(
                    modifier = leftModifier,
                    color = it.color
                ) {}
            }
        }
    }
}

@Preview("All Chips Samples")
@Preview("All Chips Samples (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AllSample() {
    val context = LocalContext.current

    var size by remember { mutableStateOf(NestChipsSize.Small) }
    var state by remember { mutableStateOf(NestChipsState.Default) }
    var isDashed by remember { mutableStateOf(false) }

    NestTheme {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Size")
            Row {
                NestChipsSize.values().forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = it == size,
                            onClick = {
                                size = it
                            }
                        )
                        Text(text = it.name)
                    }
                }
            }

            Text("State")
            LazyVerticalGrid(columns = GridCells.Adaptive(130.dp)) {
                items(NestChipsState.values()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = it == state,
                            onClick = {
                                state = it
                            }
                        )
                        Text(text = it.name)
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dashed")
                Checkbox(checked = isDashed, onCheckedChange = { isDashed = !isDashed })
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                NestChips(
                    text = "Chips Naked",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    onClick = {
                        Toast.makeText(context, "Chips Naked Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Right Chevron",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    right = NestChipsRight.Chevron {
                        Toast.makeText(context, "Chips Right Chevron - Chevron Clicked", Toast.LENGTH_SHORT).show()
                    },
                    onClick = {
                        Toast.makeText(context, "Chips Right Chevron Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Right Clear",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    right = NestChipsRight.Clear {
                        Toast.makeText(context, "Chips Right Clear - Clear Clicked", Toast.LENGTH_SHORT).show()
                    },
                    onClick = {
                        Toast.makeText(context, "Chips Right Clear Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Left Color",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    left = NestChipsLeft.Color(NestTheme.colors.RN._400),
                    onClick = {
                        Toast.makeText(context, "Chips Left Color Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Left Color (Circle)",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    left = NestChipsLeft.Color(NestTheme.colors.RN._400, isCircle = true),
                    onClick = {
                        Toast.makeText(context, "Chips Left Color (Circle) Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Left Painter",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    left = NestChipsLeft.Painter(
                        painterResource(id = iconUnifyR.drawable.iconunify_bell_filled),
                        Color.Blue
                    ),
                    onClick = {
                        Toast.makeText(context, "Chips Left Painter Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Left Painter",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    left = NestChipsLeft.Painter(
                        painterResource(id = iconUnifyR.drawable.iconunify_bell_filled),
                        Color.Blue,
                        isCircle = true
                    ),
                    onClick = {
                        Toast.makeText(context, "Chips Left Painter (Circle) Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Left Network Image",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    left = NestChipsLeft.NetworkImage("https://news.stanford.edu/wp-content/uploads/2020/10/Birds_Culture-1-copy.jpg"),
                    onClick = {
                        Toast.makeText(context, "Chips Left Network Image Clicked", Toast.LENGTH_SHORT).show()
                    }
                )

                NestChips(
                    text = "Chips Left Network Image (Circle)",
                    isDashed = isDashed,
                    state = state,
                    size = size,
                    left = NestChipsLeft.NetworkImage(
                        "https://news.stanford.edu/wp-content/uploads/2020/10/Birds_Culture-1-copy.jpg",
                        isCircle = true
                    ),
                    onClick = {
                        Toast.makeText(context, "Chips Left Network Image (Circle) Clicked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
