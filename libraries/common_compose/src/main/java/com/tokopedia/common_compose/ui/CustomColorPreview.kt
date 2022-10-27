package com.tokopedia.common_compose.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.principles.NestTypography

/**
 * Created by yovi.putra on 27/10/22"
 * Project name: android-tokopedia-core
 **/


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomColorDarkPreview() {
    NestTheme(customColor = CustomPdpColor()) {
        NestTypography(
            text = "Flash Sale",
            textStyle = NestTheme.typography.display3.copy(
                color = pdpColor.cardBackground
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun CustomColorLightPreview() {
    NestTheme(customColor = CustomPdpColor()) {
        NestTypography(
            text = "Flash Sale",
            textStyle = NestTheme.typography.display3.copy(
                color = pdpColor.cardBackground
            )
        )
    }
}

interface PdpColor {
    val cardBackground: Color
}

class CustomPdpColor(
    override val light: PdpLightColor = PdpLightColor(),
    override val dark: PdpDarkColor = PdpDarkColor()
) : ColorMode<PdpColor>

class PdpDarkColor(
    override val cardBackground: Color = NestBN.light._100
): PdpColor

class PdpLightColor(
    override val cardBackground: Color = NestRN.light._500
): PdpColor

val pdpColor: PdpColor
    @Composable
    get() = NestTheme.colors.Custom as PdpColor