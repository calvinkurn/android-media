package com.tokopedia.common_compose.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
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
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = toBackgroundColor(variant, isEnabled),
            disabledBackgroundColor = NestTheme.colors.NN._100
        )

        NestDefaultButton(
            modifier = modifier,
            text = text,
            textStyle = createTextStyle(variant, isEnabled, size),
            buttonCornerRadius = size.toCornerRadius(),
            buttonColors = buttonColors,
            buttonHeight = size.toHeightDp(),
            buttonBorder = null,
            enabled = isEnabled,
            isLoading = isLoading,
            progressBarColor = NestTheme.colors.NN._0,
            onClick = onClick,
        )
    }
    
    if (variant == Variant.GHOST) {
        NestDefaultButton(
            modifier = modifier,
            text = text,
            textStyle = createTextStyle(variant, isEnabled, size),
            buttonCornerRadius = size.toCornerRadius(),
            buttonColors = ButtonDefaults.outlinedButtonColors(),
            buttonHeight = size.toHeightDp(),
            buttonBorder = BorderStroke(width = 1.dp, color = NestTheme.colors.GN._500),
            enabled = isEnabled,
            isLoading = isLoading,
            progressBarColor = NestTheme.colors.GN._500,
            onClick = onClick,
        )
    }

    if (variant == Variant.GHOST_ALTERNATE) {
        NestDefaultButton(
            modifier = modifier,
            text = text,
            textStyle = createTextStyle(variant, isEnabled, size),
            buttonCornerRadius = size.toCornerRadius(),
            buttonColors = ButtonDefaults.outlinedButtonColors(),
            buttonHeight = size.toHeightDp(),
            buttonBorder = BorderStroke(width = 1.dp, color = NestTheme.colors.NN._300),
            enabled = isEnabled,
            isLoading = isLoading,
            progressBarColor = NestTheme.colors.GN._500,
            onClick = onClick,
        )
    }

    if (variant == Variant.GHOST_INVERTED) {
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent
        )

        NestDefaultButton(
            modifier = modifier,
            text = text,
            textStyle = createTextStyle(variant, isEnabled, size),
            buttonCornerRadius = size.toCornerRadius(),
            buttonColors = buttonColors,
            buttonHeight = size.toHeightDp(),
            buttonBorder = BorderStroke(width = 1.dp, color = NestTheme.colors.NN._0),
            enabled = isEnabled,
            isLoading = isLoading,
            progressBarColor = Color.White,
            onClick = onClick,
        )
    }

    if (variant == Variant.TEXT_ONLY) {
        NestTextButton(modifier, text, variant, isEnabled, size, onClick)
    }
}

@Composable
private fun NestDefaultButton(
    modifier: Modifier,
    text: String,
    textStyle: TextStyle,
    buttonCornerRadius: Dp,
    buttonColors: ButtonColors,
    buttonHeight: Dp,
    buttonBorder: BorderStroke?,
    enabled: Boolean,
    isLoading: Boolean,
    progressBarColor: Color,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Button(
            modifier = modifier.height(buttonHeight),
            onClick = onClick,
            border = buttonBorder,
            shape = RoundedCornerShape(buttonCornerRadius),
            colors = buttonColors,
            enabled = enabled,
            elevation = null
        ) {
            if (!isLoading) {
                NestTypography(
                    text = text,
                    textStyle = textStyle,
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = progressBarColor)
            }
        }
    }
}
@Composable
private fun NestTextButton(
    modifier: Modifier,
    text: String,
    variant: Variant,
    isEnabled: Boolean,
    size: Size,
    onClick: () -> Unit
) {

    TextButton(
        modifier = modifier,
        onClick = onClick,
        content = {
            NestTypography(
                text = text,
                textStyle = createTextStyle(
                    variant = variant, isEnabled = isEnabled, size = size
                ),
                modifier = Modifier,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

enum class Variant {
    FILLED,
    GHOST,
    GHOST_ALTERNATE,
    GHOST_INVERTED,
    TEXT_ONLY,
    FLOATING_BUTTON
}

enum class Size {
    LARGE,
    MEDIUM,
    SMALL,
    MICRO
}

@Composable
private fun toBackgroundColor(variant: Variant, isEnabled: Boolean): Color {
    if (!isEnabled) return NestTheme.colors.NN._100

    return when(variant) {
        Variant.FILLED -> NestTheme.colors.GN._500
        Variant.GHOST -> NestTheme.colors.NN._0
        Variant.GHOST_ALTERNATE -> NestTheme.colors.NN._0
        Variant.GHOST_INVERTED -> Color.Transparent
        Variant.TEXT_ONLY -> NestTheme.colors.NN._0
        Variant.FLOATING_BUTTON -> NestTheme.colors.GN._500
    }
}
@Composable
private fun createTextStyle(variant: Variant, isEnabled: Boolean, size: Size) : TextStyle {
    val filledTextStyle = if (isEnabled) {
        NestTheme.typography.display1.copy(
            color = if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0,
            fontWeight = FontWeight.ExtraBold,
            fontSize = size.toFontSize()
        )
    } else {
        NestTheme.typography.display1.copy(
            color = NestTheme.colors.NN._400,
            fontWeight = FontWeight.ExtraBold,
            fontSize = size.toFontSize()
        )
    }

    val ghostTextStyle = NestTheme.typography.display2.copy(
        color = NestTheme.colors.GN._500,
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    val ghostAlternateTextStyle = NestTheme.typography.display1.copy(
        color = NestTheme.colors.NN._300,
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    val ghostInvertedTextStyle = NestTheme.typography.display1.copy(
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    val textTextStyle = NestTheme.typography.display1.copy(
        color = NestTheme.colors.NN._600,
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    val floatingButtonTextStyle = NestTheme.typography.display2.copy(
        color = if (isSystemInDarkTheme()) NestTheme.colors.NN._0 else NestTheme.colors.NN._1000,
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.toFontSize()
    )

    return when(variant) {
        Variant.FILLED -> filledTextStyle
        Variant.GHOST -> ghostTextStyle
        Variant.GHOST_ALTERNATE -> ghostAlternateTextStyle
        Variant.GHOST_INVERTED -> ghostInvertedTextStyle
        Variant.TEXT_ONLY -> textTextStyle
        Variant.FLOATING_BUTTON -> floatingButtonTextStyle
    }

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

/*
@Preview(name = "Button Filled - Enabled")
@Preview(name = "Button Filled - Enabled Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestButtonFilledEnabledPreview() {
    NestTheme {
        NestButton(
            Modifier,
            text = "Bagikan",
            onClick = {},
            isLoading = true
        )
    }
}

@Preview(name = "Button Filled - Disabled")
@Preview(name = "Button Filled - Disabled Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestButtonFilledDisabledPreview() {
    NestTheme {
        NestButton(
            Modifier,
            text = "Bagikan",
            onClick = {},
        )
    }
}
@Preview(name = "Button disabled")
@Preview(name = "Button disabled - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestButtonDisabledPreview() {
    NestTheme {
        NestButton(
            Modifier,
            text = "Bagikan",
            onClick = {},
            enabled = false
        )
    }
}*/

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
fun NestButtonPreview() {
    NestTheme {
        Column(modifier = Modifier.padding(16.dp)) {

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
        }
    }
}

@Composable
private fun NestButtonFilledPreview() {
    Row(modifier = Modifier.fillMaxWidth()) {
        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Enabled",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Active",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Disabled",
            variant = Variant.FILLED,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
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
    Row(modifier = Modifier.fillMaxWidth()) {
        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Enabled",
            variant = Variant.GHOST,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Active",
            variant = Variant.GHOST,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Disabled",
            variant = Variant.GHOST,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
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
    Row(modifier = Modifier.fillMaxWidth()) {
        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Enabled",
            variant = Variant.GHOST_ALTERNATE,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Active",
            variant = Variant.GHOST_ALTERNATE,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Disabled",
            variant = Variant.GHOST_ALTERNATE,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
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
    Row(modifier = Modifier.fillMaxWidth()) {
        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Enabled",
            variant = Variant.GHOST_INVERTED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Active",
            variant = Variant.GHOST_INVERTED,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Disabled",
            variant = Variant.GHOST_INVERTED,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
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
    Row(modifier = Modifier.fillMaxWidth()) {
        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Enabled",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Active",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Disabled",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(0.25f),
            text = "Loading",
            variant = Variant.TEXT_ONLY,
            size = Size.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}
