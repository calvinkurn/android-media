package com.tokopedia.common_compose.header

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestImage
import com.tokopedia.common_compose.ui.NestTheme

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

        if (properties.backButtonEnabled) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        ProfileImage(source = properties.imageSource)

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

        HeaderMarginArea()
    }
}

@Composable
private fun ProfileImage(source: ProfileSource) {
    val scale = ContentScale.Crop
    val contentDescription = "profile_image"
    val modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)

    when (source) {
        is ProfileSource.Url -> {
            NestImage(modifier = modifier, imageUrl = source.url)
        }
        is ProfileSource.Painter -> {
            Image(
                modifier = modifier,
                painter = source.painter,
                contentScale = scale,
                contentDescription = contentDescription
            )
        }
        is ProfileSource.ImageBitmap -> {
            Image(
                modifier = modifier,
                bitmap = source.bitmap,
                contentScale = scale,
                contentDescription = contentDescription
            )
        }
        is ProfileSource.ImageVector -> {
            Image(
                modifier = modifier,
                imageVector = source.vector,
                contentScale = scale,
                contentDescription = contentDescription
            )
        }
    }
}

@Preview
@Composable
fun HeaderProfileTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderProfileType(
            properties = NestHeaderType.Profile().copy(
                title = "Jakarta",
                subTitle = "Search for location"
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
fun HeaderProfileTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderProfileType(
            properties = NestHeaderType.Profile().copy(
                backButtonEnabled = false
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
fun HeaderProfileTypeOverFlowPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderProfileType(
            properties = NestHeaderType.Profile().copy(
                title = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia ",
                subTitle = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia "
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}
