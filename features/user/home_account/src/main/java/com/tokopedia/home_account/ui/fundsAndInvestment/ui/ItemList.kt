package com.tokopedia.home_account.ui.fundsAndInvestment.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.TokopediaCardAnalytics
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun ItemList(
    modifier: Modifier,
    userId: String,
    item: WalletUiModel,
    onItemClicked: (WalletUiModel) -> Unit
) {
    LaunchedEffect(item.id) {
        if (!item.isFailed && item.id == AccountConstants.WALLET.CO_BRAND_CC) {
            TokopediaCardAnalytics.sendViewLihatSemuaPagePyEvent(
                eventLabel = item.statusName,
                userId = userId
            )
        }
    }

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onItemClicked(item) }
        ) {
            NestImage(
                source = ImageSource.Remote(source = item.urlImage),
                modifier = Modifier
                    .padding(
                        start = dp_24,
                        top = dp_16,
                        end = dp_16,
                        bottom = dp_16
                    )
                    .size(dp_48)
            )

            ContentText(
                title = item.title,
                subtitle = item.subtitle,
                isActive = item.isActive,
                isVertical = item.isVertical,
                isFailed = item.isFailed,
                modifier = Modifier.weight(1F)
            )

            ContentAction(
                isActive = item.isActive,
                isVertical = item.isVertical,
                isFailed = item.isFailed
            )

            Spacer(modifier = Modifier.width(dp_24))
        }
        NestDivider(
            modifier = Modifier
                .padding(start = dp_72)
                .fillMaxWidth(),
            size = NestDividerSize.Small
        )
    }
}

@Composable
private fun ContentText(
    title: String,
    subtitle: String,
    isActive: Boolean,
    isVertical: Boolean,
    isFailed: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        NestTypography(
            text = title,
            textStyle = NestTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        when {
            isFailed -> {
                Spacer(modifier = Modifier.height(dp_4))
                Subtitle(subtitle = LocalContext.current.getString(R.string.funds_and_investment_failed))
            }
            subtitle.isNotEmpty() && isActive || !isVertical -> {
                Spacer(modifier = Modifier.height(dp_4))
                Subtitle(subtitle = subtitle)
            }
        }
    }
}

@Composable
private fun ContentAction(
    isActive: Boolean,
    isVertical: Boolean,
    isFailed: Boolean
) {
    when {
        isFailed -> {
            IconAction(
                iconId = IconUnify.RELOAD,
                colorLightEnable = NestTheme.colors.GN._500,
                colorNightEnable = NestTheme.colors.GN._500
            )
        }
        !isActive && isVertical -> {
            NestTypography(
                text = LocalContext.current.getString(R.string.funds_and_investment_actiivate),
                textStyle = NestTheme.typography.body3.copy(
                    color = NestTheme.colors.GN._500,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        else -> {
            IconAction(iconId = IconUnify.CHEVRON_RIGHT)
        }
    }
}

@Composable
private fun IconAction(
    iconId: Int,
    colorLightEnable: Color? = null,
    colorNightEnable: Color? = null,
) {
    NestIcon(
        iconId = iconId,
        modifier = Modifier
            .size(dp_24),
        colorLightEnable = colorLightEnable,
        colorNightEnable = colorNightEnable
    )
}

@Composable
private fun Subtitle(
    subtitle: String
) {
    NestTypography(
        text = subtitle,
        textStyle = NestTheme.typography.body3.copy(
            color = NestTheme.colors.NN._950
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun ListItemFailedPreview() {
    NestTheme {
        ItemList(
            modifier = Modifier,
            item = WalletUiModel(
                id = "1",
                title = "Test Title 1",
                subtitle = "Test Subtitle 1",
                isFailed = true,
                isActive = true,
                isVertical = true,
                hideTitle = false
            ),
            userId = "12345"
        ) {

        }
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun ListItemLinkedSuccessPreview() {
    NestTheme {
        ItemList(
            modifier = Modifier,
            item = WalletUiModel(
                id = "1",
                title = "Test Title 1",
                subtitle = "Test Subtitle 1",
                isFailed = false,
                isActive = true,
                isVertical = true,
                hideTitle = false
            ),
            userId = "12345"
        ) {

        }
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun ListItemUnlinkedSuccessPreview() {
    NestTheme {
        ItemList(
            modifier = Modifier,
            item = WalletUiModel(
                id = "1",
                title = "Test Title 1",
                subtitle = "Test Subtitle 1",
                isFailed = false,
                isActive = false,
                isVertical = true,
                hideTitle = false
            ),
            userId = "12345"
        ) {

        }
    }
}
