package com.tokopedia.common_compose.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
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
import com.tokopedia.common_compose.components.loader.NestLoader
import com.tokopedia.common_compose.components.loader.NestLoaderType
import com.tokopedia.common_compose.header.NestHeaderType
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
 * @param leadingIcon: Icon resource id (e.g R.drawable.ic_chevron) to be placed on the left side of the button. Only visible if button not in loading state
 * @param trailingIcon: Icon resource id (e.g R.drawable.ic_chevron) to be placed on the right side of the button. Only visible if button not in loading state
 * @param onClick: Contains action that should be executed when button tapped/clicked
 */
@Composable
fun NestButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.FILLED,
    size: ButtonSize = ButtonSize.MEDIUM,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    isClickable: Boolean = true,
    loadingText: String = "",
    rightLoader: Boolean = true,
    leadingIcon: Int? = null,
    trailingIcon: Int? = null
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        useWhiteColorLoader = true,
        loadingText = loadingText,
        rightLoader = rightLoader,
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        useWhiteColorLoader = false,
        loadingText = loadingText,
        rightLoader = rightLoader,
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        useWhiteColorLoader = false,
        loadingText = loadingText,
        rightLoader = rightLoader,
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        useWhiteColorLoader = isSystemInDarkTheme(),
        loadingText = loadingText,
        rightLoader = rightLoader,
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        useWhiteColorLoader = true,
        loadingText = loadingText,
        rightLoader = rightLoader,
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        useWhiteColorLoader = true,
        loadingText = loadingText,
        rightLoader = rightLoader,
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
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
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
        buttonIconHeightAndWidth = size.buttonIconHeightAndWidth,
        useWhiteColorLoader = false,
        loadingText = loadingText,
        rightLoader = rightLoader,
        loaderHeight = size.loaderHeight,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
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
    buttonIconHeightAndWidth: Dp,
    loaderHeight: Dp,
    useWhiteColorLoader: Boolean,
    rightLoader: Boolean,
    loadingText: String,
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
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
                isLoading && loadingText.isNotEmpty() -> LoadingWithTextButton(loadingText, rightLoader, textStyle, loaderHeight, useWhiteColorLoader)
                isLoading && loadingText.isEmpty() -> NestButtonProgressBar(
                    loaderHeight = loaderHeight,
                    useWhiteColorLoader = useWhiteColorLoader
                )
                !isLoading && leadingIcon != null -> ButtonWithLeftIcon(
                    resourceId = leadingIcon,
                    text = text,
                    textStyle = textStyle,
                    buttonIconHeightAndWidth = buttonIconHeightAndWidth
                )
                !isLoading && trailingIcon != null -> ButtonWithRightIcon(
                    resourceId = trailingIcon,
                    text = text,
                    textStyle = textStyle,
                    buttonIconHeightAndWidth = buttonIconHeightAndWidth
                )
                else -> NestButtonText(text = text, textStyle = textStyle)
            }
        }
    }
}

@Composable
private fun NestButtonProgressBar(loaderHeight: Dp, useWhiteColorLoader: Boolean) {
    NestLoader(
        variant = NestLoaderType.Circular(isWhite = useWhiteColorLoader),
        modifier = Modifier.size(loaderHeight)
    )
}

@Composable
private fun NestButtonIcon(resourceId: Int, buttonIconHeightAndWidth: Dp) {
    // TODO: Replace with NestIcon
    Icon(
        modifier = Modifier.size(buttonIconHeightAndWidth),
        painter = painterResource(id = resourceId),
        contentDescription = "Icon"
    )
}

@Composable
private fun NestButtonText(modifier: Modifier = Modifier, text: String, textStyle: TextStyle) {
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
    useWhiteColorLoader: Boolean
) = apply {
    if (!rightLoader) {
        NestButtonProgressBar(
            loaderHeight = loaderHeight,
            useWhiteColorLoader = useWhiteColorLoader
        )
    }

    NestButtonText(
        modifier = Modifier.padding(
            start = if (!rightLoader && loadingText.isNotEmpty()) 6.dp else 0.dp,
            end = if (rightLoader && loadingText.isNotEmpty()) 6.dp else 0.dp
        ),
        text = loadingText,
        textStyle = textStyle
    )

    if (rightLoader) {
        NestButtonProgressBar(
            loaderHeight = loaderHeight,
            useWhiteColorLoader = useWhiteColorLoader
        )
    }
}

@Composable
private fun RowScope.ButtonWithLeftIcon(
    resourceId: Int,
    text: String,
    textStyle: TextStyle,
    buttonIconHeightAndWidth: Dp
) = apply {
    NestButtonIcon(resourceId = resourceId, buttonIconHeightAndWidth = buttonIconHeightAndWidth)
    Spacer(modifier = Modifier.width(8.dp))
    NestButtonText(text = text, textStyle = textStyle)
}

@Composable
private fun RowScope.ButtonWithRightIcon(
    resourceId: Int,
    text: String,
    textStyle: TextStyle,
    buttonIconHeightAndWidth: Dp
) = apply {
    NestButtonText(text = text, textStyle = textStyle)
    Spacer(modifier = Modifier.width(8.dp))
    NestButtonIcon(resourceId = resourceId, buttonIconHeightAndWidth = buttonIconHeightAndWidth)
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
    val buttonIconHeightAndWidth: Dp,
    val fontSize: TextUnit,
    val loaderHeight: Dp
) {
    LARGE(48.dp, 8.dp, 24.dp, 16.sp, 24.dp),
    MEDIUM(40.dp, 8.dp, 24.dp, 14.sp, 24.dp),
    SMALL(32.dp, 8.dp, 24.dp, 12.sp, 16.dp),
    MICRO(24.dp, 6.dp, 16.dp, 12.sp, 16.dp)
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
            NestHeader(type = NestHeaderType.SingleLine(title = "Nest Button Preview"))
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
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )

        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonGhostPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )
        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )
        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonGhostAlternatePreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )
        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )
        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonGhostInvertedPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )

        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonTextPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TEXT_ONLY,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TEXT_ONLY,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )

        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TEXT_ONLY,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonTransactionFilledPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TRANSACTION_FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TRANSACTION_FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )

        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TRANSACTION_FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonTransactionGhostPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Enabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TRANSACTION_GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )
        NestButton(
            text = "Disabled",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TRANSACTION_GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = false,
            isLoading = false
        )
        NestButton(
            text = "Loading",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.TRANSACTION_GHOST,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true
        )
    }
}

@Composable
private fun NestButtonSizesPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Large",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.LARGE,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Medium",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Small",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.SMALL,
            isEnabled = true,
            isLoading = false
        )

        NestButton(
            text = "Micro",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MICRO,
            isEnabled = true,
            isLoading = false
        )
    }
}

@Composable
private fun NestButtonLoadingStatePreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Left loader",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            loadingText = "",
            rightLoader = false
        )

        NestButton(
            text = "Right loader",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            loadingText = "",
            rightLoader = true
        )
    }
}

@Composable
private fun NestButtonWithLoadingTextPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Left loader",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            loadingText = "Tunggu sebentar..",
            rightLoader = false
        )

        NestButton(
            text = "Right loader",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = true,
            loadingText = "Tunggu sebentar..",
            rightLoader = true
        )
    }
}

@Composable
private fun NestButtonWithIcon() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NestButton(
            text = "Left Icon",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            loadingText = "",
            rightLoader = false,
            leadingIcon = R.drawable.iconunify_arrow_back,
            trailingIcon = null
        )

        NestButton(
            text = "Right icon",
            onClick = {},
            modifier = Modifier.weight(1f),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.MEDIUM,
            isEnabled = true,
            isLoading = false,
            loadingText = "",
            rightLoader = false,
            leadingIcon = null,
            trailingIcon = R.drawable.ic_system_action_close_normal_24
        )
    }
}
//endregion
