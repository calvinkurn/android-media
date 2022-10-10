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
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN200
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN200Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN400
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN400Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN50
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN50Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN800
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN800Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN950
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyBN950Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyColor
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyGN400
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyGN400Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyGN50
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyGN500
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyGN500Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyGN50Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyN700
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyN700Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN0
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN0Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN200
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN200Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN300
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN300Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN600
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN600Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN900
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN900Dark
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN950
import com.tokopedia.campaignlist.page.presentation.ui.color.UnifyNN950Dark
import com.tokopedia.campaignlist.page.presentation.ui.elevation.Elevations
import com.tokopedia.campaignlist.page.presentation.ui.elevation.LocalElevations
import com.tokopedia.campaignlist.page.presentation.ui.font.AppTypography
import com.tokopedia.campaignlist.page.presentation.ui.font.LocalTypography
import com.tokopedia.campaignlist.page.presentation.ui.font.OpenSauceTypography
import com.tokopedia.campaignlist.page.presentation.ui.shape.RoundedShapes


private val UnifyThemeLight = lightColors(
    primary = UnifyNN0,
    onPrimary = UnifyN700,
    primaryVariant = UnifyNN0,
    secondary = UnifyNN0,
    surface = UnifyNN0,
)

private val UnifyThemeDark = darkColors(
    primary = UnifyNN0Dark,
    onPrimary = UnifyN700Dark,
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
    val colors = if (darkTheme) {
        UnifyThemeDark
    } else {
        UnifyThemeLight
    }
    val elevation = if (darkTheme) DarkElevation else LightElevation
    val unifyColor = if (darkTheme) {
        UnifyColor(
            NN0 = UnifyNN0Dark,
            BN50 = UnifyBN50Dark,
            BN200 = UnifyBN200Dark,
            BN400= UnifyBN400Dark,
            BN800 = UnifyBN800Dark,
            BN950= UnifyBN950Dark,
            NN200 = UnifyNN200Dark,
            NN300 = UnifyNN300Dark,
            NN600 = UnifyNN600Dark,
            NN900 = UnifyNN900Dark,
            NN950 = UnifyNN950Dark,
            GN50 = UnifyGN50Dark,
            GN400 = UnifyGN400Dark,
            GN500 = UnifyGN500Dark
        )
    } else {
        UnifyColor(
            NN0 = UnifyNN0,
            BN50 = UnifyBN50,
            BN200 = UnifyBN200,
            BN400= UnifyBN400,
            BN800 = UnifyBN800,
            BN950= UnifyBN950,
            NN300 = UnifyNN300,
            NN200 = UnifyNN200,
            NN600 = UnifyNN600,
            NN900 = UnifyNN900,
            NN950 = UnifyNN950,
            GN50 = UnifyGN50,
            GN400 = UnifyGN400,
            GN500 = UnifyGN500
        )
    }

    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalColors provides unifyColor,
        LocalTypography provides AppTypography()
    ) {
        MaterialTheme(
            colors = colors,
            typography = OpenSauceTypography,
            shapes = RoundedShapes,
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