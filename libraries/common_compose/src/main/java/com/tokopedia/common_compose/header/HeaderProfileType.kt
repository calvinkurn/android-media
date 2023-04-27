package com.tokopedia.common_compose.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun HeaderProfileType(
    properties: NestHeaderType.Profile,
    contentSecondaryColor: Color,
    iconColor: Color
) {
    HeaderContainer {
        HeaderMarginArea()

        if (properties.showBackButton) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        ProfileImage(headerImageSource = properties.imageSource)

        Spacer(modifier = Modifier.width(8.dp))

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

@Composable
private fun ProfileImage(headerImageSource: HeaderImageSource) {
    val scale = ContentScale.Crop
    val contentDescription = "profile_image"
    val modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)

    HeaderImage(
        modifier = modifier,
        imageSource = headerImageSource,
        scale = scale,
        contentDescription = contentDescription
    )
}

@Preview
@Composable
private fun HeaderProfileTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderProfileType(
            properties = NestHeaderType.Profile().copy(
                title = "Tokopedia Official",
                subTitle = "Pilih Akun",
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
private fun HeaderProfileTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderProfileType(
            properties = NestHeaderType.Profile().copy(
                title = "Tokopedia Official",
                subTitle = "Pilih Akun",
                showBackButton = false
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
private fun HeaderProfileTypeOverFlowPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderProfileType(
            properties = NestHeaderType.Profile().copy(
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
