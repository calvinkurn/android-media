package com.tokopedia.common_compose.header

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

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
        HeaderMarginArea()

        if (properties.showBackButton) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        HeaderTitle(
            modifier = Modifier.weight(1f),
            title = properties.title
        )

        HeaderOptionsButton(optionsButton = properties.optionsButton, iconColor = iconColor)

        HeaderMarginArea()
    }
}

@Preview
@Composable
private fun HeaderSingleLineTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderSingleLineType(
            properties = NestHeaderType.SingleLine().copy(
                title = "Tokopedia App",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            ),
            iconColor = NestTheme.colors.NN._900
        )
    }
}

@Preview
@Composable
private fun HeaderSingleLineTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderSingleLineType(
            properties = NestHeaderType.SingleLine().copy(
                showBackButton = false
            ),
            iconColor = NestTheme.colors.NN._900
        )
    }
}
