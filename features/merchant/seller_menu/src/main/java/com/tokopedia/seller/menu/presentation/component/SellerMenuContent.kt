package com.tokopedia.seller.menu.presentation.component

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestNotification
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuDividerUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSettingTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState

// TODO: Add alpha to text
@Composable
fun SellerMenuContent(
    uiState: SellerMenuUIState
) {
    when(uiState) {
        is SellerMenuUIState.OnSuccessGetMenuList ->
            SellerMenuSuccessState(uiState.visitableList)
        else -> SellerMenuSuccessState(listOf())
    }
}

@Composable
fun SellerMenuSuccessState(
    items: List<SellerMenuComposeItem>
) {
    LazyColumn {
        items(
            items,
            contentType = {
                it.itemType
            },
            itemContent = {
                when(it) {
                    is SellerMenuSettingTitleUiModel -> {
                        SellerMenuTitleSection(
                            title = stringResource(id = it.titleRes),
                            ctaText = it.ctaRes?.let { res ->
                                stringResource(id = res)
                            },
                            onCtaClicked = {

                            }
                        )
                    }
                    is SellerMenuItemUiModel -> {
                        SellerMenuItem(
                            iconType = it.iconUnifyType,
                            titleRes = it.titleRes,
                            tagRes = null,
                            counter = null
                        )
                    }
                    is SellerMenuOrderUiModel -> {
                        SellerMenuOrderSection(
                            newOrderCount = it.newOrderCount,
                            readyToShipCount = it.readyToShip
                        )
                    }
                    is SellerMenuProductUiModel -> {
                        SellerMenuProductSection(
                            productCount = stringResource(
                                id = R.string.seller_menu_product_count,
                                it.count
                            )
                        )
                    }
                    is SellerMenuDividerUiModel -> {
                        when (it.type) {
                            DividerType.THICK -> {
                                NestDivider(
                                    size = NestDividerSize.Large,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 13.dp,
                                            bottom = 13.dp
                                        )
                                )
                            }
                            DividerType.THIN_FULL -> {
                                NestDivider(
                                    size = NestDividerSize.Small,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            bottom = 10.dp
                                        )
                                )
                            }
                            DividerType.THIN_INDENTED -> {
                                NestDivider(
                                    size = NestDividerSize.Small,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            top = 12.dp,
                                            bottom = 10.dp
                                        )
                                )
                            }
                            else -> {
                                NestDivider(
                                    size = NestDividerSize.Small,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 10.dp
                                        )
                                )
                            }
                        }
                    }
                    is SellerMenuSectionTitleUiModel -> {
                        SellerMenuTitleOtherSection(
                            title = stringResource(id = it.titleRes)
                        )
                    }
                }
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
fun SellerMenuInfoLoading() {

}

@Composable
fun SellerMenuShopInfo(
    imageUrl: String,
    shopNameString: String,
    @DrawableRes badgeRes: Int,
    shopScoreString: String,
    followersCount: String
) {
    ConstraintLayout {
        val (startSpace, avatarImage, shopName, shopBadge, avatarSpace, dot, shopFollowers, shopScore, shopScoreSpace) = createRefs()

        Spacer(
            modifier = Modifier
                .width(16.dp)
                .constrainAs(startSpace) {
                    start.linkTo(parent.start)
                }
        )

        SellerMenuShopImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .constrainAs(avatarImage) {
                    start.linkTo(startSpace.end)
                    bottom.linkTo(shopScore.top)
                }
        )

        NestTypography(
            text = shopNameString,
            textStyle = NestTheme.typography.heading3.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                ),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .constrainAs(shopName) {
                    start.linkTo(avatarSpace.end)
                    top.linkTo(avatarImage.top)
                    bottom.linkTo(shopBadge.top)
                }
        )

        Spacer(
            modifier = Modifier
                .width(16.dp)
                .constrainAs(avatarSpace) {
                    start.linkTo(avatarImage.end)
                }
        )

        Image(
            painterResource(id = badgeRes),
            contentDescription = null,
            modifier = Modifier
                .height(16.dp)
                .widthIn(
                    min = 0.dp,
                    max = 16.dp
                )
                .constrainAs(shopBadge) {
                    start.linkTo(shopName.start)
                    top.linkTo(shopName.bottom)
                    bottom.linkTo(avatarImage.bottom)
                }
        )

        NestTypography(
            text = stringResource(id = R.string.dot_string),
            textStyle = NestTheme.typography.body2.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.68f
                ),
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .constrainAs(dot) {
                    bottom.linkTo(shopBadge.bottom)
                    start.linkTo(shopBadge.end)
                    top.linkTo(shopBadge.top)
                }
                .wrapContentHeight(
                    align = Alignment.CenterVertically
                )
        )

        NestTypography(
            text = followersCount,
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.68f
                ),
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .constrainAs(shopFollowers) {
                    start.linkTo(dot.end)
                    top.linkTo(shopBadge.top)
                    bottom.linkTo(shopBadge.bottom)
                }
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .constrainAs(shopScoreSpace) {
                    top.linkTo(avatarImage.bottom)
                }
        )

        SellerMenuShopInfoScore(
            shopScore = shopScoreString,
            modifier = Modifier
                .constrainAs(shopScore) {
                    top.linkTo(shopScoreSpace.bottom)
                    start.linkTo(avatarImage.start)
                }
                .fillMaxWidth()
                .wrapContentHeight(
                    align = Alignment.CenterVertically
                )
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
            source = ImageSource.Remote(imageUrl),
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
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.68f
                )
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
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.68f
                )
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

@Composable
fun SellerMenuTitleSection(
    title: String,
    ctaText: String?,
    @DimenRes dimenRes: Int? = null,
    onCtaClicked: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = dimenRes?.let {
                    dimensionResource(id = it)
                } ?: 16.dp,
                bottom = 5.dp,
                end = 16.dp
            )
            .fillMaxWidth()
    ) {
        val (titleRef, ctaRef) = createRefs()

        NestTypography(
            text = title,
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.68f
                ),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .heightIn(min = 0.dp)
                .widthIn(min = 0.dp)
                .constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        if (ctaText != null) {
            NestTypography(
                text = ctaText,
                textStyle = NestTheme.typography.body3.copy(
                    color = NestTheme.colors.GN._500,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .heightIn(min = 0.dp)
                    .widthIn(min = 0.dp)
                    .constrainAs(ctaRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }

    }
}

@Composable
fun SellerMenuTitleOtherSection(
    title: String
) {
    NestTypography(
        text = title,
        textStyle = NestTheme.typography.body1.copy(
            color = NestTheme.colors.NN._950.copy(
                alpha = 0.96f
            ),
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
                top = 8.dp
            )
    )
}

@Composable
fun SellerMenuItem(
    iconType: Int?,
    @StringRes titleRes: Int,
    @StringRes tagRes: Int?,
    counter: Int?
) {
    ConstraintLayout(
        modifier = Modifier
            .height(44.dp)
    ) {
        val (menuIcon, menuIconSpacer, menuTitle, menuTag, counterIcon) = createRefs()

        if (iconType != null) {
            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .constrainAs(menuIconSpacer) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )

            NestIcon(
                iconId = iconType,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .constrainAs(menuIcon) {
                        start.linkTo(menuIconSpacer.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                    }
            )
        }

        NestTypography(
            text = stringResource(id = titleRes),
            textStyle = NestTheme.typography.body2.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                ),
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(menuTitle) {
                    start.linkTo(menuIcon.end)
                    end.linkTo(counterIcon.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    linkTo(
                        start = menuIcon.end,
                        end = counterIcon.start,
                        bias = 0.0f
                    )
                }
        )

        if (tagRes != null) {
            NestNotification(
                text = stringResource(id = tagRes),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .constrainAs(menuTag) {
                        start.linkTo(menuTitle.end)
                        end.linkTo(counterIcon.start)
                        top.linkTo(menuTitle.top)
                        bottom.linkTo(menuTitle.bottom)
                    }
            )
        }

        if (counter != null) {
            NestNotification(
                text = counter.toString(),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .constrainAs(counterIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }

    }
}

@Composable
fun SellerMenuProductSection(
    productCount: String
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
                top = 10.dp
            )
            .fillMaxWidth()
    ) {
        val (labelProduct, textProductCount, imageChevronRight) = createRefs()

        NestTypography(
            text = stringResource(id = R.string.seller_menu_product_list),
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                ),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .constrainAs(labelProduct) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        NestTypography(
            text = productCount,
            textStyle = NestTheme.typography.body3.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.68f
                ),
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .constrainAs(textProductCount) {
                    start.linkTo(parent.start)
                    top.linkTo(labelProduct.bottom)
                }
        )

        Icon(
            painter = painterResource(id = com.tokopedia.unifycomponents.R.drawable.iconunify_chevron_right),
            contentDescription = "CHEVRON_RIGHT",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .constrainAs(imageChevronRight) {
                    top.linkTo(labelProduct.top)
                    bottom.linkTo(textProductCount.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun SellerMenuOrderSection(
    newOrderCount: Int,
    readyToShipCount: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp,
                top = 10.dp
            )
            .fillMaxWidth()
    ) {
        val (cardNewOrder, cardReadyToShip) = createRefs()

        SellerMenuOrderSectionItem(
            iconRes = R.drawable.ic_seller_menu_new_order, notificationCount = newOrderCount, sectionTextString = stringResource(id = R.string.seller_menu_new_order),
            modifier = Modifier
                .constrainAs(cardNewOrder) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        SellerMenuOrderSectionItem(
            iconRes = R.drawable.ic_seller_menu_delivery,
            notificationCount = readyToShipCount,
            sectionTextString = stringResource(id = R.string.seller_menu_delivery),
            modifier = Modifier
                .constrainAs(cardReadyToShip) {
                    start.linkTo(cardNewOrder.end)
                    top.linkTo(cardNewOrder.top)
                    bottom.linkTo(cardNewOrder.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun SellerMenuOrderSectionItem(
    @DrawableRes iconRes: Int,
    notificationCount: Int,
    sectionTextString: String,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (cardContainer, sectionText) = createRefs()

        BadgedBox(
            badge = {
                if (notificationCount.isMoreThanZero()) {
                    NestNotification(
                        text = notificationCount.toString()
                    )
                }
            },
            content = {
                Card(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                ) {
                    Image(
                        painterResource(id = iconRes),
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .constrainAs(cardContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(top = 8.dp)
        )

        NestTypography(
            text = sectionTextString,
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                ),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .constrainAs(sectionText) {
                    top.linkTo(cardContainer.top)
                    bottom.linkTo(cardContainer.bottom)
                    start.linkTo(cardContainer.end)
                }
                .padding(start = 16.dp)
                .width(64.dp)
        )
    }
}

@Composable
fun SellerMenuStatusRegular(
    rmStatsText: String,
    rmTotalStatsText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            val (
                shopStatusTitle,
                iconEligiblePm,
                regularMerchantStatus,
                dividerStatsRm,
                textStatsRm,
                textTotalStatsRm
            ) = createRefs()

            val leftGuideline = createGuidelineFromStart(16.dp)

            NestTypography(
                text = stringResource(id = com.tokopedia.seller.menu.common.R.string.regular_merchant),
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.96f
                    ),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .constrainAs(shopStatusTitle) {
                        start.linkTo(leftGuideline)
                        top.linkTo(parent.top)
                        bottom.linkTo(dividerStatsRm.top)
                    }
            )

            Image(
                painter = painterResource(id = com.tokopedia.unifycomponents.R.drawable.iconunify_badge_pm_filled),
                contentDescription = "BADGE_PM_FILLED",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .padding(end = 4.dp)
                    .constrainAs(iconEligiblePm) {
                        top.linkTo(shopStatusTitle.top)
                        bottom.linkTo(shopStatusTitle.bottom)
                        end.linkTo(regularMerchantStatus.start)
                    }
            )

            NestTypography(
                text = stringResource(id = R.string.setting_upgrade),
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.GN._500,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .constrainAs(regularMerchantStatus) {
                        top.linkTo(shopStatusTitle.top)
                        bottom.linkTo(shopStatusTitle.bottom)
                        end.linkTo(parent.end)
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_divider_stats_rm),
                contentDescription = null,
                modifier = Modifier
                    .width(0.dp)
                    .height(1.dp)
                    .constrainAs(dividerStatsRm) {
                        start.linkTo(shopStatusTitle.start)
                        end.linkTo(regularMerchantStatus.end)
                        top.linkTo(shopStatusTitle.bottom)
                        bottom.linkTo(textStatsRm.top)
                    }
            )

            NestTypography(
                text = rmStatsText,
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.68f
                    )
                ),
                modifier = Modifier
                    .constrainAs(textStatsRm) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(leftGuideline)
                        top.linkTo(dividerStatsRm.bottom)
                    }
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        end = 16.dp
                    )
            )

            NestTypography(
                text = rmTotalStatsText,
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.96f
                    ),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        end = 16.dp
                    )
                    .constrainAs(textTotalStatsRm) {
                        top.linkTo(dividerStatsRm.bottom)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun SellerMenuStatusPm() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        ConstraintLayout {
            val (
                powerMerchantText,
                powerMerchantStatusText,
                badgeBorder,
                pmIcon,
                upgradePMText
            ) = createRefs()

            val leftGuideline = createGuidelineFromStart(14.dp)
            val middleGuideline = createGuidelineFromStart(40.dp)

            NestTypography(
                text = stringResource(id = com.tokopedia.seller.menu.common.R.string.power_merchant_status),
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .widthIn(min = 0.dp)
                    .constrainAs(powerMerchantText) {
                        start.linkTo(middleGuideline)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            NestTypography(
                text = stringResource(id = com.tokopedia.seller.menu.common.R.string.setting_not_active),
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.RN._500,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .widthIn(min = 0.dp)
                    .constrainAs(powerMerchantStatusText) {
                        start.linkTo(powerMerchantText.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clip(CircleShape)
                    .border(1.dp, NestTheme.colors.NN._0, CircleShape)
                    .background(Color.Transparent)
                    .constrainAs(badgeBorder) {
                        start.linkTo(parent.start)
                        end.linkTo(middleGuideline)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Composable
fun SellerMenuStatusOS() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        ConstraintLayout {
            val (statusText, badgeBorder, statusImage) = createRefs()

            val middleGuideline = createGuidelineFromStart(40.dp)

            NestTypography(
                text = stringResource(id = com.tokopedia.seller.menu.common.R.string.official_store),
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .constrainAs(statusText) {
                        start.linkTo(middleGuideline)
                    }
            )

            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(0.dp)
                    .clip(CircleShape)
                    .border(5.dp, NestTheme.colors.NN._0, CircleShape)
                    .background(Color.Transparent)
                    .constrainAs(badgeBorder) {
                        start.linkTo(parent.start)
                        end.linkTo(middleGuideline)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Image(
                painter = painterResource(id = com.tokopedia.gm.common.R.drawable.ic_official_store_product),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(statusImage) {
                        top.linkTo(badgeBorder.top)
                        bottom.linkTo(badgeBorder.bottom)
                        start.linkTo(badgeBorder.start)
                        end.linkTo(badgeBorder.end)
                    }
            )
        }
    }
}

@Preview
@Composable
fun preview() {
    NestTheme {
        SellerMenuStatusPm()
//        SellerMenuStatusRegular("Transaksi sejak berlangsung", "10/100")
//        SellerMenuOrderSection(
//            1,2
//        )
    }
//    NestTheme {
//        SellerMenuShopInfo(
//            imageUrl = "",
//            shopNameString = "Adeedast Naiki",
//            badgeRes = R.drawable.ic_power_merchant,
//            shopScoreString = "100",
//            followersCount = "10 Followers"
//        )
//
//    }
}
