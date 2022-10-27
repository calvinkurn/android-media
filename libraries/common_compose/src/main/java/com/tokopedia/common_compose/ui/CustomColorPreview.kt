package com.tokopedia.common_compose.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.extensions.tag
import com.tokopedia.common_compose.principles.NestTypography

/**
 * Created by yovi.putra on 27/10/22"
 * Project name: android-tokopedia-core
 **/


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomColorDarkPreview() {
    NestTheme(customColor = CustomPdpColor()) {
        Card(
            modifier = Modifier.tag("card"),
            contentColor = pdpColor.cardBackground
        ) {
            NestTypography(
                modifier = Modifier.padding(8.dp),
                text = "Flash Sale",
                textStyle = NestTheme.typography.display3.copy(
                    color = pdpColor.highLight
                )
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun CustomColorLightPreview() {
    NestTheme(customColor = CustomPdpColor()) {
        Card(
            modifier = Modifier.tag("card"),
            contentColor = pdpColor.cardBackground
        ) {
            NestTypography(
                modifier = Modifier.padding(8.dp),
                text = "Flash Sale",
                textStyle = NestTheme.typography.display3.copy(
                    color = pdpColor.highLight
                )
            )
        }
    }
}

interface PdpColor {
    val cardBackground: Color
    val highLight: Color
        get() = NestGN.light._500
}

class CustomPdpColor(
    override val light: PdpLightColor = PdpLightColor(),
    override val dark: PdpDarkColor = PdpDarkColor()
) : ColorMode<PdpColor>

class PdpDarkColor(
    override val cardBackground: Color = NestBN.light._100
) : PdpColor

class PdpLightColor(
    override val cardBackground: Color = NestRN.light._500
) : PdpColor

val pdpColor: PdpColor
    @Composable
    get() = NestTheme.colors.Custom as PdpColor