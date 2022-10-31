package com.tokopedia.common_compose.ui

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
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
    NestTheme {
        Card(
            modifier = Modifier.tag("card"),
            contentColor = NestTheme.colors.cardColor
        ) {
            NestTypography(
                modifier = Modifier.padding(8.dp),
                text = "Flash Sale",
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.label
                )
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun CustomColorLightPreview() {
    NestTheme {
        Card(
            modifier = Modifier.tag("card"),
            contentColor = NestTheme.colors.cardColor
        ) {
            NestTypography(
                modifier = Modifier.padding(8.dp),
                text = "Flash Sale",
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.label
                )
            )
        }
    }
}

val NestColor.label: Color
    @Composable
    get() = NestGN.light._500

val NestColor.cardColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) NestBN.light._100 else  NestRN.light._500