package com.tokopedia.seller.menu.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory
import com.tokopedia.seller.menu.presentation.uimodel.SellerMenuItem

@Composable
fun SellerMenuContent(
    typeFactory: SellerMenuTypeFactory,
    sellerMenuItems: List<SellerMenuItem>
) {
    LazyColumn {
        items(
            sellerMenuItems,
            contentType = {
                it.type(typeFactory)
            },
            itemContent = {
            }
        )
    }
}

@Composable
fun SellerMenuHeader() {
    ConstraintLayout {
        NestTicker(ticker = listOf())
    }
}

@Composable
fun SellerMenuShopInfo(
    imageUrl: String
) {
    ConstraintLayout {
        val (avatarImage, shopName, shopBadge, dot, shopFollowers, shopScore) = createRefs()

        SellerMenuShopImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .constrainAs(avatarImage) {
                    start.linkTo(parent.start)
                    bottom.linkTo(shopScore.top)
                }
        )
    }
}

@Composable
fun SellerMenuShopImage(
    imageUrl: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .width(96.dp)
            .height(96.dp)
            .padding(2.dp)
            .border(3.dp, NestTheme.colors.NN._0, CircleShape)
    ) {
        NestImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .border(2.dp, NestTheme.colors.NN._50, CircleShape)
                .clip(CircleShape)
        )
    }
}

@Composable
fun SellerMenuShopInfoScore(
    shopScore: String,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        NestTypography(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.seller_menu_shop_score_label),
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.NN._950
            )
        )
        NestTypography(
            text = shopScore,
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.GN._500,
                fontWeight = FontWeight.Bold
            )
        )
        NestTypography(
            text = stringResource(id = R.string.seller_menu_shop_score_max_label),
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.NN._950
            )
        )
        Icon(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            painter = painterResource(id = com.tokopedia.unifycomponents.R.drawable.iconunify_chevron_right),
            contentDescription = "CHEVRON_RIGHT"
        )
    }
}

@Preview
@Composable
fun preview() {
    NestTheme {
        SellerMenuShopImage("")
    }
}
