package com.tokopedia.campaignlist.page.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.tokopedia.campaignlist.page.presentation.ui.color.LocalColors
import com.tokopedia.campaignlist.page.presentation.ui.color.TokopediaColor
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN0
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN0Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN700
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN700Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.getColor
import com.tokopedia.campaignlist.page.presentation.ui.elevation.Elevations
import com.tokopedia.campaignlist.page.presentation.ui.elevation.LocalElevations
import com.tokopedia.campaignlist.page.presentation.ui.font.LocalTypography
import com.tokopedia.campaignlist.page.presentation.ui.font.NestTypography
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
fun NestTheme(
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


    AdaptiveStatusBarColor(darkTheme = darkTheme, themeColors = themeColors)

    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalColors provides colors,
        LocalTypography provides NestTypography()
    ) {
        MaterialTheme(
            colors = themeColors,
            typography = OpenSauceTypography,
            content = content
        )
    }
}

@Composable
private fun AdaptiveStatusBarColor(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColors: Colors
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = themeColors.primary.toArgb()

            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }
}


object NestTheme {
    val colors: TokopediaColor
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: NestTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current


    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val elevations: Elevations
        @Composable
        @ReadOnlyComposable
        get() = LocalElevations.current
}