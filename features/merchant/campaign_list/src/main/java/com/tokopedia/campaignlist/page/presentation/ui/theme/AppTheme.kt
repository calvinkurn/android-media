package com.tokopedia.campaignlist.page.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp
import com.tokopedia.campaignlist.page.presentation.ui.color.LocalColors
import com.tokopedia.campaignlist.page.presentation.ui.color.TokopediaColor
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN0
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN0Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN700
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN700Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.getColor
import com.tokopedia.campaignlist.page.presentation.ui.elevation.Elevations
import com.tokopedia.campaignlist.page.presentation.ui.elevation.LocalElevations
import com.tokopedia.campaignlist.page.presentation.ui.font.AppTypography
import com.tokopedia.campaignlist.page.presentation.ui.font.LocalTypography
import com.tokopedia.campaignlist.page.presentation.ui.font.OpenSauceTypography


private val UnifyThemeLight = lightColors(
    primary = UnifyNN0,
    onPrimary = UnifyNN700,
    primaryVariant = UnifyNN0,
    secondary = UnifyNN0,
    surface = UnifyNN0,
)

private val UnifyThemeDark = darkColors(
    primary = UnifyNN0Dark,
    onPrimary = UnifyNN700Dark,
    secondary = UnifyNN0Dark,
    surface = UnifyNN0Dark
)

private val LightElevation = Elevations()
private val DarkElevation = Elevations(card = 1.dp)


@Composable
fun UnifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeColors = if (darkTheme) {
        UnifyThemeDark
    } else {
        UnifyThemeLight
    }
    val elevation = if (darkTheme) DarkElevation else LightElevation
    val colors = getColor(darkTheme)

    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalColors provides colors,
        LocalTypography provides AppTypography()
    ) {
        MaterialTheme(
            colors = themeColors,
            typography = OpenSauceTypography,
            content = content
        )
    }
}


object UnifyTheme {

    /**
     * Proxy to [MaterialTheme]
     */
    val themeColor: Colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colors

    /**
     * Proxy to [MaterialTheme]
     */
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Proxy to [MaterialTheme]
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    /**
     * Retrieves the current [Elevations] at the call site's position in the hierarchy.
     */
    val elevations: Elevations
        @Composable
        @ReadOnlyComposable
        get() = LocalElevations.current

    val colors: TokopediaColor
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}