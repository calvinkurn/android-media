package com.tokopedia.common_compose.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme


@Composable
fun NestButton(
    modifier: Modifier = Modifier,
    text: String,
    variant: Variant = Variant.FILLED,
    size: Size = Size.MEDIUM,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    if (variant == Variant.FILLED) {
        FilledButton(modifier, isEnabled, text, size, isLoading, onClick)
    }

    if (variant == Variant.GHOST) {
        GhostButton(modifier, text, isEnabled, size, isLoading, onClick)
    }

    if (variant == Variant.GHOST_ALTERNATE) {
        GhostAlternateButton(modifier, text, isEnabled, size, isLoading, onClick)
    }

    if (variant == Variant.GHOST_INVERTED) {
        GhostInvertedButton(modifier, text, size, isEnabled, isLoading, onClick)
    }

    if (variant == Variant.TEXT_ONLY) {
        TextButton(modifier, text, isEnabled, isLoading, size, onClick)
    }
}

@Composable
private fun FilledButton(
    modifier: Modifier,
    isEnabled: Boolean,
    text: String,
    size: Size,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val filledTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = NestTheme.colors.GN._500,
        contentColor = if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0,
        disabledContentColor = NestTheme.colors.NN._400,
        disabledBackgroundColor = NestTheme.colors.NN._100
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        textStyle = filledTextStyle,
        buttonCornerRadius = size.toCornerRadius(),
        buttonColors = buttonColors,
        buttonHeight = size.toHeightDp(),
        buttonBorderStroke = null,
        progressBarColor = Color.White,
        onClick = onClick,
    )
}

@Composable
private fun GhostButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    size: Size,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val ghostTextStyle = NestTheme.typography.display2.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        textStyle = ghostTextStyle,
        buttonCornerRadius = size.toCornerRadius(),
        buttonColors = ButtonDefaults.outlinedButtonColors(
            contentColor = NestTheme.colors.GN._500,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.toHeightDp(),
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) NestTheme.colors.GN._500 else NestTheme.colors.NN._100
        ),
        progressBarColor = NestTheme.colors.GN._500,
        onClick = onClick,
    )
}

@Composable
private fun GhostAlternateButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    size: Size,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val ghostAlternateTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        textStyle = ghostAlternateTextStyle,
        buttonCornerRadius = size.toCornerRadius(),
        buttonColors = ButtonDefaults.outlinedButtonColors(
            contentColor = NestTheme.colors.NN._600,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.toHeightDp(),
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) NestTheme.colors.NN._300 else NestTheme.colors.NN._100
        ),
        progressBarColor = NestTheme.colors.NN._300,
        onClick = onClick,
    )
}


@Composable
private fun GhostInvertedButton(
    modifier: Modifier,
    text: String,
    size: Size,
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val ghostInvertedTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        textStyle = ghostInvertedTextStyle,
        buttonCornerRadius = size.toCornerRadius(),
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = NestTheme.colors.NN._1000,
            disabledBackgroundColor = Color.Transparent,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.toHeightDp(),
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) NestTheme.colors.NN._1000 else NestTheme.colors.NN._100
        ),
        progressBarColor = NestTheme.colors.NN._1000,
        onClick = onClick
    )
}

@Composable
private fun TextButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    isLoading: Boolean,
    size: Size,
    onClick: () -> Unit
) {
    val textButtonTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        textStyle = textButtonTextStyle,
        buttonCornerRadius = size.toCornerRadius(),
        buttonColors = ButtonDefaults.textButtonColors(
            contentColor = NestTheme.colors.NN._600,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.toHeightDp(),
        buttonBorderStroke = null,
        progressBarColor = NestTheme.colors.NN._400,
        onClick = onClick,
    )
}

@Composable
private fun NestDefaultButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    isLoading: Boolean,
    textStyle: TextStyle,
    buttonCornerRadius: Dp,
    buttonColors: ButtonColors,
    buttonHeight: Dp,
    buttonBorderStroke: BorderStroke?,
    progressBarColor: Color,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        val interactionSource = remember { MutableInteractionSource() }

        Button(
            modifier = modifier.height(buttonHeight),
            onClick = onClick,
            border = buttonBorderStroke,
            shape = RoundedCornerShape(buttonCornerRadius),
            colors = buttonColors,
            enabled = isEnabled,
            elevation = null,
            contentPadding = PaddingValues(all = 0.dp),
            interactionSource = interactionSource
        ) {
            NestTypography(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = text,
                textStyle = textStyle.copy(color = if (isLoading) Color.Transparent else textStyle.color),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = progressBarColor)
        }
    }
}

enum class Variant {
    FILLED,
    GHOST,
    GHOST_ALTERNATE,
    GHOST_INVERTED,
    TEXT_ONLY
}

enum class Size {
    LARGE,
    MEDIUM,
    SMALL,
    MICRO
}
private fun Size.toHeightDp(): Dp {
    return when(this) {
        Size.LARGE -> 48.dp
        Size.MEDIUM -> 40.dp
        Size.SMALL -> 32.dp
        Size.MICRO -> 24.dp
    }
}
private fun Size.toFontSize(): TextUnit {
    return when(this) {
        Size.LARGE -> 16.sp
        Size.MEDIUM -> 14.sp
        Size.SMALL -> 12.sp
        Size.MICRO -> 12.sp
    }
}
private fun Size.toCornerRadius(): Dp {
    return if (this == Size.MICRO) 6.dp else 8.dp
}
//region Preview
@Preview(
    showSystemUi = true,
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    showSystemUi = true,
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF222329,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun NestButtonPreview() {
    NestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NestTypography(text = "Filled", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
            NestButtonFilledPreview()

            NestTypography(text = "Ghost - Default", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
            NestButtonGhostPreview()

            NestTypography(text = "Ghost - Alternate", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
            NestButtonGhostAlternatePreview()

            NestTypography(text = "Ghost - Inverted", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
            NestButtonGhostInvertedPreview()

            NestTypography(text = "Text", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
            NestButtonTextPreview()

            NestTypography(text = "Button sizes", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
            NestButtonSizesPreview()
        }
    }
}


@Composable
private fun NestButtonFilledPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonGhostPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = Variant.GHOST,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = Variant.GHOST,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = Variant.GHOST,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonGhostAlternatePreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = Variant.GHOST_ALTERNATE,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = Variant.GHOST_ALTERNATE,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = Variant.GHOST_ALTERNATE,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonGhostInvertedPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = Variant.GHOST_INVERTED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = Variant.GHOST_INVERTED,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )


        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = Variant.GHOST_INVERTED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonTextPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonSizesPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Large",
            variant = Variant.FILLED,
            size = Size.LARGE,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Medium",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Small",
            variant = Variant.FILLED,
            size = Size.SMALL,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Micro",
            variant = Variant.FILLED,
            size = Size.MICRO,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
    }
}
//endregion
