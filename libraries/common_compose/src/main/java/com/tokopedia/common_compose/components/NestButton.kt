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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

/**
 * @param text: Text to be displayed on the button
 * @param variant: To determine the main appearance of button
 * @param size: To determine the button size height
 * @param isEnabled: To determine whether button should be enabled or not
 * @param isLoading: Determine if button is in loading state or not. If isLoading = true, circular progress bar will be displayed in the middle of button
 * @param isClickable: Determine if button should clickable or not. isClickable = false will make button non-clickable
 * @param loadingText: Text to be displayed on the button when button is in loading state
 * @param rightLoader: If rightLoader = true, progress bar will appear on the right side of button when we're on loading state.
 *                     If rightLoader = false then progress bar will be displayed on the left side
 * @param leadingIcon: Icon to be placed on the left side of the button. Only visible if button not in loading state
 * @param trailingIcon: Icon to be placed on the right side of the button. Only visible if button not in loading state
 * @param onClick: Contains action that should be executed when button tapped/clicked
 */
@Composable
fun NestButton(
    modifier: Modifier = Modifier,
    text: String,
    variant: ButtonVariant = ButtonVariant.FILLED,
    size: ButtonSize = ButtonSize.MEDIUM,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    isClickable: Boolean = true,
    loadingText: String = "",
    rightLoader: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    if (variant == ButtonVariant.FILLED) {
        FilledButton(modifier, isEnabled, text, size, isLoading, isClickable, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }

    if (variant == ButtonVariant.GHOST) {
        GhostButton(modifier, text, isEnabled, size, isLoading, isClickable, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }

    if (variant == ButtonVariant.GHOST_ALTERNATE) {
        GhostAlternateButton(modifier, text, isEnabled, size, isLoading, isClickable, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }

    if (variant == ButtonVariant.GHOST_INVERTED) {
        GhostInvertedButton(modifier, text, size, isEnabled, isLoading, isClickable, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }

    if (variant == ButtonVariant.TEXT_ONLY) {
        TextButton(modifier, text, isEnabled, isLoading, isClickable, size, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }

    if (variant == ButtonVariant.TRANSACTION_FILLED) {
        TransactionFilledButton(modifier, text, size, isEnabled, isLoading, isClickable, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }

    if (variant == ButtonVariant.TRANSACTION_GHOST) {
        TransactionGhostButton(modifier, text, isEnabled, isClickable, size, isLoading, loadingText, rightLoader, leadingIcon, trailingIcon, onClick)
    }
}

@Composable
private fun FilledButton(
    modifier: Modifier,
    isEnabled: Boolean,
    text: String,
    size: ButtonSize,
    isLoading: Boolean,
    isClickable: Boolean,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val filledTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
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
        isClickable = isClickable,
        textStyle = filledTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = buttonColors,
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = null,
        progressBarColor = Color.White,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
    )
}

@Composable
private fun GhostButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    size: ButtonSize,
    isLoading: Boolean,
    isClickable: Boolean,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val ghostTextStyle = NestTheme.typography.display2.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        isClickable = isClickable,
        textStyle = ghostTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = ButtonDefaults.outlinedButtonColors(
            contentColor = NestTheme.colors.GN._500,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) NestTheme.colors.GN._500 else NestTheme.colors.NN._100
        ),
        progressBarColor = NestTheme.colors.GN._500,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
    )
}

@Composable
private fun GhostAlternateButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    size: ButtonSize,
    isLoading: Boolean,
    isClickable: Boolean,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val ghostAlternateTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        isClickable = isClickable,
        textStyle = ghostAlternateTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = ButtonDefaults.outlinedButtonColors(
            contentColor = NestTheme.colors.NN._600,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) NestTheme.colors.NN._300 else NestTheme.colors.NN._100
        ),
        progressBarColor = NestTheme.colors.NN._300,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
    )
}


@Composable
private fun GhostInvertedButton(
    modifier: Modifier,
    text: String,
    size: ButtonSize,
    isEnabled: Boolean,
    isLoading: Boolean,
    isClickable: Boolean,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val ghostInvertedTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        isClickable = isClickable,
        textStyle = ghostInvertedTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = NestTheme.colors.NN._1000,
            disabledBackgroundColor = Color.Transparent,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) NestTheme.colors.NN._1000 else NestTheme.colors.NN._100
        ),
        progressBarColor = NestTheme.colors.NN._1000,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
    )
}

@Composable
private fun TransactionFilledButton(
    modifier: Modifier,
    text: String,
    size: ButtonSize,
    isEnabled: Boolean,
    isLoading: Boolean,
    isClickable: Boolean,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val transactionFilledTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
    )

    val buttonBackgroundDark = Color(0xFFFF8215)
    val buttonBackgroundLight = Color(0xFFFA591D)
    val buttonBackgroundColor = if (isSystemInDarkTheme()) buttonBackgroundDark else buttonBackgroundLight

    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = buttonBackgroundColor,
        contentColor = if (isSystemInDarkTheme()) NestTheme.colors.NN._1000 else NestTheme.colors.NN._0,
        disabledContentColor = NestTheme.colors.NN._400,
        disabledBackgroundColor = NestTheme.colors.NN._100
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        isClickable = isClickable,
        textStyle = transactionFilledTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = buttonColors,
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) buttonBackgroundColor else NestTheme.colors.NN._100
        ),
        progressBarColor = Color.White,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
    )
}

@Composable
private fun TransactionGhostButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    isClickable: Boolean,
    size: ButtonSize,
    isLoading: Boolean,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val transactionGhostTextStyle = NestTheme.typography.display2.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
    )

    val textColorDark = Color(0xFFFF8215)
    val textColorLight = Color(0xFFFA591D)
    val textColor = if (isSystemInDarkTheme()) textColorDark else textColorLight

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        isClickable = isClickable,
        textStyle = transactionGhostTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = BorderStroke(
            width = 1.dp,
            color = if (isEnabled) textColor else NestTheme.colors.NN._100
        ),
        progressBarColor = textColor,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
    )
}

@Composable
private fun TextButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    isLoading: Boolean,
    isClickable: Boolean,
    size: ButtonSize,
    loadingText: String,
    rightLoader: Boolean,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val textButtonTextStyle = NestTheme.typography.display1.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = size.fontSize
    )

    NestDefaultButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        isLoading = isLoading,
        isClickable = isClickable,
        textStyle = textButtonTextStyle,
        buttonCornerRadius = size.buttonCornerRadius,
        buttonColors = ButtonDefaults.textButtonColors(
            contentColor = NestTheme.colors.NN._600,
            disabledContentColor = NestTheme.colors.NN._400
        ),
        buttonHeight = size.buttonHeight,
        buttonBorderStroke = null,
        progressBarColor = NestTheme.colors.NN._400,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick,
    )
}

@Composable
private fun NestDefaultButton(
    modifier: Modifier,
    text: String,
    isEnabled: Boolean,
    isLoading: Boolean,
    isClickable: Boolean,
    textStyle: TextStyle,
    buttonCornerRadius: Dp,
    buttonColors: ButtonColors,
    buttonHeight: Dp,
    buttonBorderStroke: BorderStroke?,
    loaderHeight: Dp,
    progressBarColor: Color,
    rightLoader: Boolean,
    loadingText: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        val interactionSource = remember { MutableInteractionSource() }

        val doNothing = {}
        val onClickAction = if (isClickable) onClick else doNothing

        Button(
            modifier = modifier.height(buttonHeight),
            onClick = onClickAction,
            border = buttonBorderStroke,
            shape = RoundedCornerShape(buttonCornerRadius),
            colors = buttonColors,
            enabled = isEnabled,
            elevation = null,
            contentPadding = PaddingValues(horizontal = 16.dp),
            interactionSource = interactionSource
        ) {

            when {
                isLoading && loadingText.isNotEmpty() -> LoadingWithTextButton(loadingText, rightLoader, textStyle, loaderHeight, progressBarColor)
                isLoading && loadingText.isEmpty() -> ProgressBar(
                    loaderHeight = loaderHeight,
                    progressBarColor = progressBarColor
                )
                !isLoading && leadingIcon != null -> ButtonWithLeftIcon(
                    leadingIcon = { leadingIcon.invoke() },
                    text = text,
                    textStyle = textStyle
                )
                !isLoading && trailingIcon != null -> ButtonWithRightIcon(
                    trailingIcon = { trailingIcon.invoke() },
                    text = text,
                    textStyle = textStyle
                )
                else -> ButtonText(text = text, textStyle = textStyle)
            }

        }


    }
}

@Composable
private fun ProgressBar(loaderHeight: Dp, progressBarColor: Color) {
    //TODO: Replace loader with NestLoader
    CircularProgressIndicator(modifier = Modifier.size(loaderHeight), color = progressBarColor)
}

@Composable
private fun ButtonText(modifier: Modifier = Modifier, text: String, textStyle: TextStyle) {
    NestTypography(
        modifier = modifier,
        text = text,
        textStyle = textStyle.copy(color = textStyle.color),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun RowScope.LoadingWithTextButton(
    loadingText: String,
    rightLoader: Boolean,
    textStyle: TextStyle,
    loaderHeight: Dp,
    progressBarColor: Color
) = apply {
    if (!rightLoader) {
        ProgressBar(loaderHeight = loaderHeight, progressBarColor = progressBarColor)
    }

    ButtonText(
        modifier = Modifier.padding(
            start = if (!rightLoader && loadingText.isNotEmpty()) 6.dp else 0.dp,
            end = if (rightLoader && loadingText.isNotEmpty()) 6.dp else 0.dp
        ),
        text = loadingText,
        textStyle = textStyle
    )


    if (rightLoader) {
        ProgressBar(loaderHeight = loaderHeight, progressBarColor = progressBarColor)
    }
}


@Composable
private fun RowScope.ButtonWithLeftIcon(
    leadingIcon: @Composable () -> Unit,
    text: String,
    textStyle: TextStyle
) = apply {
    leadingIcon.invoke()
    Spacer(modifier = Modifier.width(8.dp))
    ButtonText(text = text, textStyle = textStyle)
}


@Composable
private fun RowScope.ButtonWithRightIcon(
    trailingIcon: @Composable () -> Unit,
    text: String,
    textStyle: TextStyle
) = apply {
    ButtonText(text = text, textStyle = textStyle)
    Spacer(modifier = Modifier.width(8.dp))
    trailingIcon.invoke()
}


enum class ButtonVariant {
    FILLED,
    GHOST,
    GHOST_ALTERNATE,
    GHOST_INVERTED,
    TEXT_ONLY,
    TRANSACTION_FILLED,
    TRANSACTION_GHOST
}

enum class ButtonSize(
    val buttonHeight: Dp,
    val buttonCornerRadius: Dp,
    val fontSize: TextUnit,
    val loaderHeight: Dp
) {
    LARGE(48.dp, 8.dp, 16.sp, 24.dp),
    MEDIUM(40.dp, 8.dp, 14.sp, 24.dp),
    SMALL(32.dp, 8.dp, 12.sp, 16.dp),
    MICRO(24.dp, 6.dp, 12.sp, 16.dp)
}

//region Preview
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "spec:width=411dp,height=1200dp"
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF222329,
    device = "spec:width=411dp,height=1200dp"
)
@Composable
private fun NestButtonPreview() {
    NestTheme {
        Column {
            NestHeader(title = "NestButton Preview")
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

                NestTypography(text = "Transaction - Filled (Deprecated)", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonTransactionFilledPreview()

                NestTypography(text = "Transaction - Ghost (Deprecated)", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonTransactionGhostPreview()

                NestTypography(text = "Text", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonTextPreview()

                NestTypography(text = "Button sizes", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonSizesPreview()

                NestTypography(text = "Button loading state - Without loading text", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonLoadingStatePreview()

                NestTypography(text = "Button loading state - With loading text", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonWithLoadingTextPreview()

                NestTypography(text = "Button icon - With icon", textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._800))
                NestButtonWithIcon()
            }
        }

    }
}


@Composable
private fun NestButtonFilledPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
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
            variant = ButtonVariant.GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.GHOST,
            size = ButtonSize.MEDIUM,
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
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.MEDIUM,
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
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )


        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.MEDIUM,
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
            variant = ButtonVariant.TEXT_ONLY,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.TEXT_ONLY,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.TEXT_ONLY,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonTransactionFilledPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = ButtonVariant.TRANSACTION_FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.TRANSACTION_FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.TRANSACTION_FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonTransactionGhostPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Enabled",
            variant = ButtonVariant.TRANSACTION_GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Disabled",
            variant = ButtonVariant.TRANSACTION_GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false,
            onClick = {},
        )
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Loading",
            variant = ButtonVariant.TRANSACTION_GHOST,
            size = ButtonSize.MEDIUM,
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
            variant = ButtonVariant.FILLED,
            size = ButtonSize.LARGE,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Medium",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Small",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.SMALL,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Micro",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MICRO,
            isEnabled = true,
            isLoading = false,
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonLoadingStatePreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Left loader",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            rightLoader = false,
            loadingText = "",
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Right loader",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            rightLoader = true,
            loadingText = "",
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonWithLoadingTextPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Left loader",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            rightLoader = false,
            loadingText = "Tunggu sebentar..",
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Right loader",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            rightLoader = true,
            loadingText = "Tunggu sebentar..",
            onClick = {},
        )
    }
}

@Composable
private fun NestButtonWithIcon() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            modifier = Modifier.weight(1f),
            text = "Left Icon",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            rightLoader = false,
            loadingText = "",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_down_8dp),
                    contentDescription = ""
                )
            },
            trailingIcon = null,
            onClick = {},
        )

        NestButton(
            modifier = Modifier.weight(1f),
            text = "Right icon",
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            rightLoader = false,
            loadingText = "",
            leadingIcon = null,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_down_8dp),
                    contentDescription = ""
                )
            },
            onClick = {},
        )
    }
}
//endregion
