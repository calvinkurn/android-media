package com.tokopedia.common_compose.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun HeaderDoubleLineType(
    properties: NestHeaderType.DoubleLine,
    contentSecondaryColor: Color,
    iconColor: Color
) {
    HeaderContainer {
        HeaderMarginArea()

        if (properties.backButtonEnabled) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderTitle(title = properties.title)

            HeaderSubTitle(
                subTitle = properties.subTitle,
                contentSecondaryColor = contentSecondaryColor
            )
        }

        HeaderMarginArea()
    }
}

@Preview
@Composable
fun HeaderDoubleLineTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        Column {
            HeaderDoubleLineType(
                properties = NestHeaderType.DoubleLine(),
                iconColor = NestTheme.colors.NN._900,
                contentSecondaryColor = NestTheme.colors.NN._600
            )
        }
    }
}

@Preview
@Composable
fun HeaderDoubleLineTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        Column {
            HeaderDoubleLineType(
                properties = NestHeaderType.DoubleLine().copy(
                    backButtonEnabled = false
                ),
                iconColor = NestTheme.colors.NN._900,
                contentSecondaryColor = NestTheme.colors.NN._600
            )
        }
    }
}

@Preview
@Composable
fun HeaderDoubleLineTypeOverFlowPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        Column {
            HeaderDoubleLineType(
                properties = NestHeaderType.DoubleLine().copy(
                    title = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia ",
                    subTitle = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia "
                ),
                iconColor = NestTheme.colors.NN._900,
                contentSecondaryColor = NestTheme.colors.NN._600
            )
        }
    }
}
