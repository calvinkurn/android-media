package com.tokopedia.common_compose.ui

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


private val NestThemeLight = lightColors(
    primary = NestNN.light._0,
    onPrimary = NestNN.light._700,
    primaryVariant = NestNN.light._0,
    secondary = NestNN.light._0,
    surface = NestNN.light._0,
)

private val NestThemeDark = darkColors(
    primary = NestNN.dark._0,
    onPrimary = NestNN.dark._700,
    secondary = NestNN.dark._0,
    surface = NestNN.dark._0,
)

private val LightElevation = Elevations()
private val DarkElevation = Elevations(card = 1.dp)

@Composable
fun NestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    customColor: ColorMode<*>? = null,
    content: @Composable () -> Unit
) {
    val themeColors = if (darkTheme) NestThemeDark else NestThemeLight
    val elevation = if (darkTheme) DarkElevation else LightElevation
    val colors: NestColor = if (darkTheme) {
        NestDarkColor(Custom = customColor?.dark)
    } else {
        NestLightColor(Custom = customColor?.light)
    }

    AdaptiveStatusBarColor(darkTheme = darkTheme, themeColors = themeColors)

    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalNestColor provides colors,
        LocalTypography provides NestTextStyle()
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

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
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