package com.tokopedia.common_compose.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.ui.NestTheme

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

        if (properties.backButtonEnabled) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderSubTitle(
                subTitle = properties.subTitle,
                contentSecondaryColor = contentSecondaryColor
            )

            Row(
                modifier = Modifier.clickable {
                    properties.onTitleClicked.invoke()
                },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderTitle(
                    modifier = Modifier.weight(1f, fill = false),
                    title = properties.title
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = "chevron_down"
                )

                Spacer(modifier = Modifier)
            }
        }

        HeaderMarginArea()
    }
}

@Preview
@Composable
fun HeaderHeaderLocationTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderLocationType(
            properties = NestHeaderType.Location().copy(
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
fun HeaderHeaderLocationTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderLocationType(
            properties = NestHeaderType.Location().copy(
                backButtonEnabled = false
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
fun HeaderHeaderLocationTypeOverFlowPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderLocationType(
            properties = NestHeaderType.Location().copy(
                title = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia ",
                subTitle = "Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia Tokopedia "
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}
