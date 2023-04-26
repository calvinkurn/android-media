package com.tokopedia.common_compose.header

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun HeaderSingleLineType(
    properties: NestHeaderType.SingleLine,
    iconColor: Color
) {
    HeaderContainer {

        HeaderMarginStart()

        if (properties.backButtonEnabled) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        HeaderTitle(title = properties.title)
    }
}

@Preview
@Composable
fun HeaderSingleLineTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950,
    ) {
        Column {
            HeaderSingleLineType(
                properties = NestHeaderType.SingleLine(),
                iconColor = NestTheme.colors.NN._900
            )
        }
    }
}

@Preview
@Composable
fun HeaderSingleLineTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950,
    ) {
        Column {
            HeaderSingleLineType(
                properties = NestHeaderType.SingleLine().copy(
                    backButtonEnabled = false
                ),
                iconColor = NestTheme.colors.NN._900
            )
        }
    }
}
