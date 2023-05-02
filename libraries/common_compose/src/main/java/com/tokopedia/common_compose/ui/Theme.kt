package com.tokopedia.common_compose.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val NestThemeLight = lightColors(
    primary = NestGN.light._500,
    onPrimary = NestNN.light._0,
    primaryVariant = NestGN.light._400,
    secondary = NestBN.light._200,
    surface = NestNN.light._0,
    background = NestNN.light._0
)

private val NestThemeDark = darkColors(
    primary = NestGN.dark._500,
    onPrimary = NestNN.dark._0,
    secondary = NestBN.dark._200,
    surface = NestNN.dark._0,
    background = NestNN.dark._0
)

private val LightElevation = Elevations()
private val DarkElevation = Elevations(card = 1.dp)

private val NestShape = Shapes(
    small = RoundedCornerShape(8.dp), // based on NestButton
    medium = RoundedCornerShape(8.dp), // TBD
    large = RoundedCornerShape(4.dp) // TBD
)

@Composable
fun NestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeColors = if (darkTheme) NestThemeDark else NestThemeLight
    val elevation = if (darkTheme) DarkElevation else LightElevation
    val colors: NestColor = if (darkTheme) NestDarkColor() else NestLightColor()

    AdaptiveStatusBarColor(darkTheme = darkTheme, themeColors = themeColors)

    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalNestColor provides colors,
        LocalTypography provides NestTextStyle()
    ) {
        MaterialTheme(
            colors = themeColors,
            typography = OpenSauceTypography,
            content = content,
            shapes = NestShape
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
            window.statusBarColor = if (!darkTheme && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                NestNN.light._200.toArgb()
            } else {
                themeColors.onPrimary.toArgb()
            }

            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }
}

object NestTheme {
    val colors: NestColor
        @Composable
        @ReadOnlyComposable
        get() = LocalNestColor.current

    val typography: NestTextStyle
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
