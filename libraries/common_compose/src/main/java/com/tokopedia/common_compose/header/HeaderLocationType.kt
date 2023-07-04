package com.tokopedia.common_compose.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
internal fun HeaderLocationType(
    properties: NestHeaderType.Location,
    contentSecondaryColor: Color,
    iconColor: Color
) {
    HeaderContainer {
        HeaderMarginArea()

        if (properties.showBackButton) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderLocationContent(
                properties = properties,
                contentSecondaryColor = contentSecondaryColor,
                iconColor = iconColor
            )
        }

        HeaderOptionsButton(optionsButton = properties.optionsButton, iconColor = iconColor)

        HeaderMarginArea()
    }
}

@Preview
@Composable
private fun HeaderLocationTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderLocationType(
            properties = NestHeaderType.Location().copy(
                title = "Jakarta",
                subTitle = "Search for location",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
private fun HeaderLocationTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderLocationType(
            properties = NestHeaderType.Location().copy(
                showBackButton = false
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
private fun HeaderLocationTypeOverFlowPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderLocationType(
            properties = NestHeaderType.Location().copy(
                title = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia ",
                subTitle = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia ",
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}
