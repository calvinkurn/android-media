package com.tokopedia.common_compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp


private val NestThemeLight = lightColors(
    primary = NestGN500,
    onPrimary = NestNN0,
    primaryVariant = NestGN400,
    secondary = NestBN200,
    surface = NestNN0,
)

private val NestThemeDark = darkColors(
    primary = NestGN500Dark,
    onPrimary = NestNN0Dark,
    secondary = NestBN200Dark,
    surface = NestNN0Dark
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
    val themeColors = if (darkTheme) {
        NestThemeDark
    } else {
        NestThemeLight
    }
    val elevation = if (darkTheme) DarkElevation else LightElevation
    val colors = populateColor(darkTheme)

    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalColors provides colors,
        LocalTypography provides NestTextStyle()
    ) {
        MaterialTheme(
            colors = themeColors,
            typography = OpenSauceTypography,
            content = content,
            shapes = NestShape,
        )
    }
}


object NestTheme {
    val colors: TokopediaColor
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

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
