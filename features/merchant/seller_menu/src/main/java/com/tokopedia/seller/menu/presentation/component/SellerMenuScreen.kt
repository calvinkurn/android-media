package com.tokopedia.seller.menu.presentation.component

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.gm.common.constant.PMProURL
import com.tokopedia.header.compose.HeaderActionButton
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.NestLabel
import com.tokopedia.nest.components.NestLabelType
import com.tokopedia.nest.components.NestLocalLoad
import com.tokopedia.nest.components.NestNotification
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.components.ticker.NestTickerData
import com.tokopedia.nest.components.ticker.TickerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.IconSource
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.addImpression
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuActionClick
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuDividerUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuFeatureUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSettingTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuUIState
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuComposeViewModel
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import com.tokopedia.gm.common.R as gmcommonR
import com.tokopedia.seller.menu.R as sellermenuR
import com.tokopedia.seller.menu.common.R as sellermenucommonR
import com.tokopedia.unifycomponents.R as unifycomponentsR

private const val STATUS_INCUBATE_OS = 6
private const val TICKER_TYPE_WARNING = "warning"
private const val TICKER_TYPE_DANGER = "danger"
private const val TRANSACTION_FORMAT = "%s/" + Constant.ShopStatus.MAX_TRANSACTION
private const val REACH_MAX_FREE_TRANSACTION = ">" + Constant.ShopStatus.MAX_TRANSACTION

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerMenuScreen(
    viewModel: SellerMenuComposeViewModel,
    onSuccessLoadInitialState: () -> Unit,
    onRefresh: () -> Unit,
    onActionClick: (SellerMenuActionClick) -> Unit,
    onReload: (Boolean) -> Unit,
    onTickerClick: (String) -> Unit,
    onShowToaster: (String) -> Unit,
    onShopInfoImpressed: (ShopType?) -> Unit,
    onShopScoreImpressed: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isToasterAlreadyShown = viewModel.isToasterAlreadyShown.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = onRefresh
    )

    Scaffold(
        topBar = {
            NestHeader(
                type = NestHeaderType.SingleLine(
                    title = stringResource(id = R.string.title_seller_menu),
                    optionsButton = listOf(
                        HeaderActionButton(
                            onClicked = {
                                onActionClick(SellerMenuActionClick.INBOX)
                            },
                            contentDescription = "Inbox",
                            notification = null,
                            icon = IconSource.Painter(sellermenuR.drawable.ic_seller_menu_inbox)
                        ),
                        HeaderActionButton(
                            onClicked = {
                                onActionClick(SellerMenuActionClick.NOTIF_CENTER)
                            },
                            contentDescription = "Notif",
                            notification = null,
                            icon = IconSource.Painter(sellermenuR.drawable.ic_seller_menu_notif)
                        )
                    ),
                    onBackClicked = {
                        onActionClick(SellerMenuActionClick.BACK_BUTTON)
                    }
                )
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .pullRefresh(pullRefreshState)
            ) {
                SellerMenuContent(
                    uiState.value,
                    onSuccessLoadInitialState,
                    onActionClick,
                    onReload,
                    onTickerClick,
                    onShowToaster = { message ->
                        if (!isToasterAlreadyShown.value) {
                            onShowToaster(message)
                        }
                    },
                    onShopInfoImpressed = { shopType ->
                        onShopInfoImpressed(shopType)
                        onShopScoreImpressed()
                    }
                )

                PullRefreshIndicator(
                    refreshing = isRefreshing.value,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    )
}

@Composable
fun SellerMenuContent(
    uiState: SellerMenuUIState,
    onSuccessLoadInitialState: () -> Unit,
    onActionClick: (SellerMenuActionClick) -> Unit,
    onReload: (Boolean) -> Unit,
    onTickerClick: (String) -> Unit,
    onShowToaster: (String) -> Unit,
    onShopInfoImpressed: (ShopType?) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is SellerMenuUIState.OnSuccessGetMenuList -> {
            if (uiState.isInitialValue) {
                onSuccessLoadInitialState()
            }
            SellerMenuSuccessState(
                uiState.visitableList,
                onActionClick,
                onReload,
                onTickerClick,
                onShopInfoImpressed,
                modifier
            )
        }

        is SellerMenuUIState.OnFailedGetMenuList -> {
            uiState.throwable.message?.takeIf { it.isNotBlank() }?.let { errorMessage ->
                onShowToaster(errorMessage)
            }
            SellerMenuSuccessState(
                uiState.visitableList,
                onActionClick,
                onReload,
                onTickerClick,
                onShopInfoImpressed,
                modifier
            )
        }

        else -> SellerMenuSuccessState(
            listOf(),
            onActionClick,
            onReload,
            onTickerClick,
            onShopInfoImpressed,
            modifier
        )
    }
}

@Composable
fun SellerMenuSuccessState(
    items: List<SellerMenuComposeItem>,
    onActionClick: (SellerMenuActionClick) -> Unit,
    onReload: (Boolean) -> Unit,
    onTickerClick: (String) -> Unit,
    onShopInfoImpressed: (ShopType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        items(
            items,
            key = {
                it.key
            },
            contentType = {
                it.itemType
            },
            itemContent = {
                var loading by rememberSaveable {
                    mutableStateOf(false)
                }
                when (it) {
                    is SellerMenuSettingTitleUiModel -> {
                        SellerMenuTitleSection(
                            title = stringResource(id = it.titleRes),
                            ctaText = it.ctaRes?.let { res ->
                                stringResource(id = res)
                            },
                            onCtaClicked = {
                                onActionClick(it.actionClick)
                            }
                        )
                    }

                    is SellerMenuItemUiModel -> {
                        SellerMenuItem(
                            iconType = it.iconUnifyType,
                            titleRes = it.titleRes,
                            tagRes = null,
                            counter = it.notificationCount.takeIf { count ->
                                count > Int.ZERO
                            },
                            onActionClick = {
                                onActionClick(it.actionClick)
                            }
                        )
                    }

                    is SellerMenuOrderUiModel -> {
                        SellerMenuOrderSection(
                            newOrderCount = it.newOrderCount,
                            readyToShipCount = it.readyToShip,
                            onActionClick = onActionClick
                        )
                    }

                    is SellerMenuProductUiModel -> {
                        SellerMenuProductSection(
                            productCount = stringResource(
                                id = R.string.seller_menu_product_count,
                                it.count
                            ),
                            onClick = {
                                onActionClick(SellerMenuActionClick.PRODUCT_LIST)
                            }
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

                    is SellerMenuInfoLoadingUiModel -> {
                        SellerMenuInfoLoading()
                    }

                    is SellerMenuInfoUiModel -> {
                        SellerMenuShopInfo(
                            imageUrl = it.shopAvatarUrl,
                            shopNameString = it.shopName,
                            badgeUrl = it.shopBadgeUrl.orEmpty(),
                            shopScoreString = it.shopScore.toString(),
                            followersCount = it.shopFollowers.toString(),
                            partialResponseStatus = it.partialResponseStatus,
                            totalBalance = it.balanceValue,
                            userShopInfoWrapper = it.userShopInfoWrapper,
                            onActionClick = onActionClick,
                            isLoading = loading,
                            onReload = { isLoading ->
                                loading = isLoading
                                onReload(isLoading)
                            },
                            onClickText = { spannedRange ->
                                onTickerClick(spannedRange.item)
                            },
                            modifier = Modifier
                                .addImpression(
                                    uniqueIdentifier = it.key,
                                    impressionState = it.impressHolder,
                                    state = lazyListState,
                                    onItemViewed = { _ ->
                                        onShopInfoImpressed(it.userShopInfoWrapper.shopType)
                                    }
                                )
                        )
                    }

                    is SellerMenuFeatureUiModel -> {
                        SellerMenuFeatureSection(onActionClick = onActionClick)
                    }
                }
            }
        )
    }
}

@Composable
fun SellerMenuInfoLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (
                avatarImage,
                shopNameLoaderRef,
                badgeFollowersLoaderRef,
                shopStatusLoaderRef,
                balanceTitleLoaderRef,
                balanceValueLoaderRef,
                shopStatusDivider
            ) = createRefs()

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.5f)
                    .padding(
                        start = 16.dp
                    )
                    .constrainAs(shopNameLoaderRef) {
                        top.linkTo(avatarImage.top)
                        bottom.linkTo(badgeFollowersLoaderRef.top)
                        start.linkTo(avatarImage.end)
                    }
            ) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(
                        NestShimmerType.Line
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(96.dp)
                    .padding(
                        start = 16.dp
                    )
                    .constrainAs(badgeFollowersLoaderRef) {
                        top.linkTo(shopNameLoaderRef.bottom)
                        bottom.linkTo(avatarImage.bottom)
                        start.linkTo(shopNameLoaderRef.start)
                    }
            ) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(
                        NestShimmerType.Line
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            SellerMenuShopImage(
                imageUrl = "",
                modifier = Modifier
                    .padding(
                        start = 16.dp
                    )
                    .constrainAs(avatarImage) {
                        top.linkTo(parent.top)
                    }
            )

            Box(
                modifier = Modifier
                    .height(82.dp)
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 17.dp
                    )
                    .constrainAs(shopStatusLoaderRef) {
                        top.linkTo(avatarImage.bottom)
                        start.linkTo(parent.start)
                    }
            ) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(
                        NestShimmerType.Line
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }

            NestDivider(
                size = NestDividerSize.Small,
                modifier = Modifier
                    .height(1.dp)
                    .constrainAs(shopStatusDivider) {
                        top.linkTo(shopStatusLoaderRef.bottom)
                    }
            )

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(96.dp)
                    .padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 16.dp
                    )
                    .constrainAs(balanceTitleLoaderRef) {
                        start.linkTo(parent.start)
                        top.linkTo(shopStatusDivider.bottom)
                    }
            ) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(
                        NestShimmerType.Line
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(128.dp)
                    .padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        end = 16.dp
                    )
                    .constrainAs(balanceValueLoaderRef) {
                        end.linkTo(parent.end)
                        top.linkTo(shopStatusDivider.bottom)
                    }
            ) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(
                        NestShimmerType.Line
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun SellerMenuShopInfo(
    modifier: Modifier = Modifier,
    imageUrl: String,
    shopNameString: String,
    badgeUrl: String,
    shopScoreString: String,
    followersCount: String,
    partialResponseStatus: Pair<Boolean, Boolean>,
    totalBalance: String,
    userShopInfoWrapper: UserShopInfoWrapper,
    onActionClick: (SellerMenuActionClick) -> Unit,
    isLoading: Boolean,
    onReload: (Boolean) -> Unit,
    onClickText: ((spannedRange: AnnotatedString.Range<String>) -> Unit)? = null
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            ticker,
            startSpace,
            avatarImage,
            shopName,
            shopBadge,
            avatarSpace,
            endSpace,
            dot,
            shopFollowers,
            shopScore,
            shopScoreSpace,
            shopStatus,
            balance,
            errorLocalLoad
        ) = createRefs()

        val statusUiModel = userShopInfoWrapper.userShopInfoUiModel.statusInfoUiModel
        val shouldShowTicker =
            statusUiModel.statusTitle.isNotBlank() &&
                statusUiModel.statusMessage.isNotBlank() &&
                statusUiModel.shopStatus.orZero() == STATUS_INCUBATE_OS

        if (shouldShowTicker) {
            val tickerType = when (statusUiModel.tickerType) {
                TICKER_TYPE_DANGER -> TickerType.ERROR
                TICKER_TYPE_WARNING -> TickerType.WARNING
                else -> TickerType.ANNOUNCEMENT
            }
            NestTicker(
                ticker = listOf(
                    NestTickerData(
                        tickerTitle = statusUiModel.statusTitle,
                        tickerType = tickerType,
                        tickerDescription = HtmlLinkHelper(
                            LocalContext.current,
                            statusUiModel.statusMessage
                        ).spannedString?.toAnnotatedString() ?: ""
                    )
                ),
                modifier = Modifier
                    .constrainAs(ticker) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                onClickText = onClickText
            )
        }

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
                    if (shouldShowTicker) {
                        top.linkTo(ticker.top)
                    } else {
                        top.linkTo(parent.top)
                    }
                    bottom.linkTo(shopScore.top)
                }
                .clickable {
                    onActionClick(SellerMenuActionClick.SHOP)
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
                .clickable {
                    onActionClick(SellerMenuActionClick.SHOP)
                }
        )

        Spacer(
            modifier = Modifier
                .width(16.dp)
                .constrainAs(avatarSpace) {
                    start.linkTo(avatarImage.end)
                }
        )

        Spacer(
            modifier = Modifier
                .width(16.dp)
                .constrainAs(endSpace) {
                    end.linkTo(parent.end)
                }
        )

        NestImage(
            source = ImageSource.Remote(badgeUrl),
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
            text = "$followersCount ${stringResource(id = R.string.setting_followers)}",
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
                .clickable {
                    onActionClick(SellerMenuActionClick.SHOP_FAVORITES)
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
                    end.linkTo(endSpace.start)
                }
                .padding(
                    end = 8.dp,
                    start = 16.dp
                )
                .wrapContentHeight(
                    align = Alignment.CenterVertically
                )
                .clickable {
                    onActionClick(SellerMenuActionClick.SHOP_SCORE)
                }
        )

        var hasLocalLoadShown = false

        if (partialResponseStatus.first) {
            SellerMenuShopStatusInfo(
                userShopInfoWrapper = userShopInfoWrapper,
                modifier = Modifier
                    .constrainAs(shopStatus) {
                        top.linkTo(shopScore.bottom)
                        start.linkTo(avatarImage.start)
                        end.linkTo(endSpace.start)
                    }
                    .padding(
                        top = 8.dp,
                        end = 16.dp,
                        start = 16.dp
                    ),
                onActionClick = onActionClick
            )
        } else {
            hasLocalLoadShown = true
            NestLocalLoad(
                title = stringResource(id = sellermenuR.string.setting_error_message),
                description = stringResource(id = sellermenuR.string.setting_error_description),
                isLoading = isLoading,
                modifier = Modifier
                    .constrainAs(errorLocalLoad) {
                        top.linkTo(shopScore.bottom)
                        start.linkTo(avatarImage.start)
                        end.linkTo(endSpace.start)
                    }
            ) {
                onReload(it)
            }
        }

        if (partialResponseStatus.second) {
            SellerMenuBalanceSection(
                balance = totalBalance,
                modifier = Modifier
                    .padding(
                        end = 16.dp,
                        start = 16.dp
                    )
                    .constrainAs(balance) {
                        top.linkTo(shopStatus.bottom)
                        start.linkTo(avatarImage.start)
                        end.linkTo(endSpace.start)
                    },
                onClick = {
                    onActionClick(SellerMenuActionClick.BALANCE)
                }
            )
        } else if (!hasLocalLoadShown) {
            NestLocalLoad(
                title = stringResource(id = sellermenuR.string.setting_error_message),
                description = stringResource(id = sellermenuR.string.setting_error_description),
                isLoading = isLoading,
                modifier = Modifier
                    .constrainAs(errorLocalLoad) {
                        top.linkTo(shopScore.bottom)
                        start.linkTo(avatarImage.start)
                        end.linkTo(endSpace.start)
                    }
            ) {
                onReload(it)
            }
        }
    }
}

@Composable
fun SellerMenuShopStatusInfo(
    userShopInfoWrapper: UserShopInfoWrapper,
    modifier: Modifier,
    onActionClick: (SellerMenuActionClick) -> Unit
) {
    val totalTransaction = userShopInfoWrapper.userShopInfoUiModel.totalTransaction.orZero()
    val shouldShowTransaction = totalTransaction <= Constant.ShopStatus.MAX_TRANSACTION_VISIBLE
    when (val shopType = userShopInfoWrapper.shopType) {
        is RegularMerchant -> {
            SellerMenuStatusRegular(
                shouldShowTransaction = shouldShowTransaction,
                totalTransaction = totalTransaction,
                pmEligibleIcon = getPmEligibleIcon(userShopInfoWrapper),
                modifier = modifier
                    .clickable {
                        onActionClick(SellerMenuActionClick.POWER_MERCHANT)
                    }
            )
        }

        is PowerMerchantStatus -> {
            val periodType = userShopInfoWrapper.userShopInfoUiModel.periodTypePmPro
            val isNewSeller = userShopInfoWrapper.userShopInfoUiModel.isNewSeller
            val canUpgrade = when {
                periodType == Constant.D_DAY_PERIOD_TYPE_PM_PRO && !isNewSeller -> true
                periodType == Constant.COMMUNICATION_PERIOD_PM_PRO -> false
                shopType is PowerMerchantStatus.NotActive -> false
                else -> true
            }

            SellerMenuStatusPm(
                shouldShowTransaction = shouldShowTransaction,
                totalTransaction = totalTransaction,
                canUpgrade = canUpgrade,
                modifier = modifier,
                isKyc = userShopInfoWrapper.userShopInfoUiModel.isKyc.orFalse(),
                onActionClick = onActionClick
            )
        }

        is PowerMerchantProStatus -> {
            val backgroundImageUrl: String = when (shopType) {
                PowerMerchantProStatus.Advanced -> PMProURL.BG_ADVANCE
                PowerMerchantProStatus.Expert -> PMProURL.BG_EXPERT
                PowerMerchantProStatus.Ultimate -> PMProURL.BG_ULTIMATE
            }

            val pmProBadgeUrl = userShopInfoWrapper.userShopInfoUiModel.badge

            SellerMenuStatusPmPro(
                shouldShowTransaction = shouldShowTransaction,
                totalTransaction = totalTransaction,
                backgroundImageUrl = backgroundImageUrl,
                pmProBadgeUrl = pmProBadgeUrl,
                modifier = modifier
                    .clickable {
                        onActionClick(SellerMenuActionClick.POWER_MERCHANT)
                    }
            )
        }

        is ShopType.OfficialStore -> {
            SellerMenuStatusOS(
                modifier = modifier
                    .clickable {
                        onActionClick(SellerMenuActionClick.OFFICIAL_STORE)
                    }
            )
        }

        else -> {
            /* no-op */
        }
    }
}

private fun getRmStatsTextRes(userShopInfoUiModel: UserShopInfoWrapper.UserShopInfoUiModel): Int {
    return if (userShopInfoUiModel.dateCreated.isBlank() || userShopInfoUiModel.isBeforeOnDate) {
        sellermenuR.string.transaction_on_date
    } else {
        sellermenuR.string.total_transaction_label
    }
}

private fun getPmEligibleIcon(userShopInfoWrapper: UserShopInfoWrapper): Int? {
    return if (userShopInfoWrapper.shopType == RegularMerchant.Verified) {
        userShopInfoWrapper.userShopInfoUiModel.getPowerMerchantProEligibleIcon()
            ?: userShopInfoWrapper.userShopInfoUiModel.getPowerMerchantEligibleIcon()
    } else {
        null
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
                .fillMaxWidth()
                .fillMaxHeight()
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
            painter = painterResource(id = unifycomponentsR.drawable.iconunify_chevron_right),
            contentDescription = "CHEVRON_RIGHT"
        )
    }
}

@Composable
fun SellerMenuBalanceSection(
    balance: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        val (balanceTitle, balanceValue) = createRefs()

        NestTypography(
            text = stringResource(id = sellermenucommonR.string.setting_balance),
            textStyle = NestTheme.typography.body2.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                )
            ),
            modifier = Modifier
                .constrainAs(balanceTitle) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    onClick()
                }
        )

        NestTypography(
            text = balance,
            textStyle = NestTheme.typography.body2.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                ),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .constrainAs(balanceValue) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    onClick()
                }
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
                    .clickable {
                        onCtaClicked()
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
    counter: Int?,
    onActionClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .clickable {
                onActionClick()
            }
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
    productCount: String,
    onClick: () -> Unit
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
            .clickable {
                onClick()
            }
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
            painter = painterResource(id = unifycomponentsR.drawable.iconunify_chevron_right),
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
    readyToShipCount: Int,
    onActionClick: (SellerMenuActionClick) -> Unit
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
            iconRes = R.drawable.ic_seller_menu_new_order,
            notificationCount = newOrderCount,
            sectionTextString = stringResource(id = R.string.seller_menu_new_order),
            modifier = Modifier
                .constrainAs(cardNewOrder) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .clickable {
                    onActionClick(SellerMenuActionClick.NEW_ORDER)
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
                .clickable {
                    onActionClick(SellerMenuActionClick.READY_TO_SHIP_ORDER)
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
    shouldShowTransaction: Boolean = false,
    totalTransaction: Long = 0L,
    pmEligibleIcon: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    bottom = 12.dp
                )
        ) {
            val (
                shopStatusTitle,
                iconEligiblePm,
                regularMerchantStatus,
                dividerSpacer,
                transactionComponent
            ) = createRefs()

            val leftGuideline = createGuidelineFromStart(16.dp)

            NestTypography(
                text = stringResource(id = sellermenucommonR.string.regular_merchant),
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
                        bottom.linkTo(dividerSpacer.top)
                    }
            )

            if (pmEligibleIcon != null) {
                NestIcon(
                    iconId = pmEligibleIcon,
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
            }

            TotalTransactionComponent(
                shouldShowTransaction,
                totalTransaction,
                modifier = Modifier.constrainAs(transactionComponent) {
                    top.linkTo(shopStatusTitle.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}

@Composable
fun TotalTransactionComponent(
    shouldShowTransaction: Boolean,
    totalTransaction: Long,
    modifier: Modifier
) {
    if (shouldShowTransaction) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (divider, tvTransactionLabel, tvTransactions) = createRefs()
            val totalTransactionFmt = if (totalTransaction <= Constant.ShopStatus.MAX_TRANSACTION) {
                String.format(TRANSACTION_FORMAT, totalTransaction.toString())
            } else {
                REACH_MAX_FREE_TRANSACTION
            }

            Image(
                painter = painterResource(id = R.drawable.ic_divider_stats_rm),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .constrainAs(divider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 12.dp)
                    }
            )

            NestTypography(
                text = stringResource(id = R.string.total_transaction_label),
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.68f
                    )
                ),
                modifier = Modifier
                    .constrainAs(tvTransactionLabel) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(divider.bottom)
                    }
                    .padding(
                        top = 12.dp,
                        end = 16.dp
                    )
            )

            NestTypography(
                text = totalTransactionFmt,
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.96f
                    ),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        end = 16.dp
                    )
                    .constrainAs(tvTransactions) {
                        top.linkTo(tvTransactionLabel.top)
                        bottom.linkTo(tvTransactionLabel.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun SellerMenuStatusPm(
    shouldShowTransaction: Boolean = false,
    totalTransaction: Long = 0L,
    canUpgrade: Boolean,
    modifier: Modifier,
    isKyc: Boolean,
    onActionClick: (SellerMenuActionClick) -> Unit
) {
    val actionClick =
        when {
            !isKyc -> SellerMenuActionClick.POWER_MERCHANT_KYC
            canUpgrade -> SellerMenuActionClick.POWER_MERCHANT_UPGRADE
            else -> SellerMenuActionClick.POWER_MERCHANT
        }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .clickable { onActionClick(actionClick) }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (
                powerMerchantText,
                powerMerchantStatusText,
                pmIcon,
                upgradePMText,
                kycNotVerified,
                transactionComponent
            ) = createRefs()

            val pmText = stringResource(id = sellermenucommonR.string.power_merchant_upgrade)

            NestTypography(
                text = pmText,
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .widthIn(min = 0.dp)
                    .constrainAs(powerMerchantText) {
                        start.linkTo(pmIcon.end, margin = 4.dp)
                        top.linkTo(pmIcon.top)
                        bottom.linkTo(pmIcon.bottom)
                    }
            )

            if (!isKyc) {
                NestIcon(
                    iconId = IconUnify.INFORMATION,
                    colorLightEnable = NestTheme.colors.RN._500,
                    colorNightEnable = NestTheme.colors.RN._500,
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .constrainAs(kycNotVerified) {
                            end.linkTo(powerMerchantStatusText.start, margin = 2.dp)
                            top.linkTo(pmIcon.top)
                            bottom.linkTo(pmIcon.bottom)
                        }
                )

                NestTypography(
                    text = stringResource(id = sellermenucommonR.string.setting_other_not_verified),
                    textStyle = NestTheme.typography.body2.copy(
                        color = NestTheme.colors.NN._950,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .widthIn(min = 0.dp)
                        .constrainAs(powerMerchantStatusText) {
                            top.linkTo(pmIcon.top)
                            bottom.linkTo(pmIcon.bottom)
                            end.linkTo(parent.end)
                        }
                        .padding(end = 16.dp)
                )
            }

            NestIcon(
                iconId = IconUnify.BADGE_PM_FILLED,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, NestTheme.colors.NN._0, CircleShape)
                    .background(Color.Transparent)
                    .constrainAs(pmIcon) {
                        start.linkTo(parent.start, margin = 12.dp)
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            bias = 0f,
                            bottomMargin = 12.dp,
                            topMargin = 12.dp
                        )
                    }
            )

            TotalTransactionComponent(
                shouldShowTransaction,
                totalTransaction,
                modifier = Modifier.constrainAs(transactionComponent) {
                    top.linkTo(pmIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                }
            )
        }
    }
}

@Composable
fun SellerMenuStatusPmPro(
    shouldShowTransaction: Boolean,
    totalTransaction: Long,
    backgroundImageUrl: String,
    pmProBadgeUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val (
                pmProBackgroundRef,
                pmProTextRef,
                pmProIconRef,
                pmInfoSpacerRef,
                transactionComponent
            ) = createRefs()

            NestImage(
                source = ImageSource.Remote(backgroundImageUrl),
                modifier = Modifier
                    .constrainAs(pmProBackgroundRef) {
                        start.linkTo(parent.start)
                    }
                    .fillMaxHeight()
            )

            Spacer(
                modifier = Modifier
                    .width(40.dp)
                    .constrainAs(pmInfoSpacerRef) {
                        start.linkTo(parent.start)
                    }
            )

            NestImage(
                source = ImageSource.Remote(pmProBadgeUrl),
                type = NestImageType.Circle,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(1.dp, NestTheme.colors.NN._50, CircleShape)
                    .constrainAs(pmProIconRef) {
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            bias = 0f,
                            topMargin = 12.dp,
                            bottomMargin = 12.dp
                        )
                        end.linkTo(pmInfoSpacerRef.end)
                    }
            )

            NestTypography(
                text = stringResource(id = sellermenuR.string.power_merchant_pro_status),
                textStyle = NestTheme.typography.body2.copy(
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.96f
                    ),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(start = 3.dp)
                    .constrainAs(pmProTextRef) {
                        top.linkTo(pmProIconRef.top)
                        bottom.linkTo(pmProIconRef.bottom)
                        start.linkTo(pmInfoSpacerRef.end)
                    }
            )

            TotalTransactionComponent(
                shouldShowTransaction,
                totalTransaction,
                modifier = Modifier.constrainAs(transactionComponent) {
                    top.linkTo(pmProTextRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                }
            )
        }
    }
}

@Composable
fun SellerMenuStatusOS(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = 8.dp)
        ) {
            val (statusText, statusImage) = createRefs()

            NestTypography(
                text = stringResource(id = sellermenucommonR.string.official_store),
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .constrainAs(statusText) {
                        start.linkTo(statusImage.end, margin = 8.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Image(
                painter = painterResource(id = gmcommonR.drawable.ic_official_store_product),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(statusImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .width(24.dp)
                    .height(24.dp)
            )
        }
    }
}

@Composable
fun SellerMenuFeatureSection(
    modifier: Modifier = Modifier,
    onActionClick: (SellerMenuActionClick) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        val (titleRef, labelRef, statisticCardRef, promoCardRef, feedCardRef, financialCardRef) = createRefs()

        NestTypography(
            text = stringResource(id = R.string.seller_menu_business_section),
            textStyle = NestTheme.typography.body1.copy(
                color = NestTheme.colors.NN._950.copy(
                    alpha = 0.96f
                ),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .constrainAs(titleRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                )
        )

        NestLabel(
            labelText = stringResource(id = R.string.seller_menu_seller_app_only),
            labelType = NestLabelType.HIGHLIGHT_LIGHT_GREEN,
            modifier = Modifier
                .constrainAs(labelRef) {
                    start.linkTo(titleRef.end)
                    top.linkTo(titleRef.top)
                    bottom.linkTo(titleRef.bottom)
                }
                .padding(top = 8.dp)
        )

        SellerMenuFeatureCard(
            iconUrl = SellerMigrationConstants.URL_STATISTICS_ICON,
            titleString = stringResource(id = R.string.seller_menu_shop_statistics),
            descString = stringResource(id = R.string.seller_menu_shop_statistics_description),
            onActionClick = {
                onActionClick(SellerMenuActionClick.MIGRATION_STATISTIC)
            },
            modifier = Modifier
                .constrainAs(statisticCardRef) {
                    top.linkTo(titleRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(promoCardRef.start)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 16.dp, top = 16.dp)
        )

        SellerMenuFeatureCard(
            iconUrl = SellerMigrationConstants.URL_PROMO_ICON,
            titleString = stringResource(id = R.string.seller_menu_ads_and_promo),
            descString = stringResource(id = R.string.seller_menu_ads_and_promo_description),
            onActionClick = {
                onActionClick(SellerMenuActionClick.MIGRATION_PROMO)
            },
            modifier = Modifier
                .constrainAs(promoCardRef) {
                    top.linkTo(titleRef.bottom)
                    start.linkTo(statisticCardRef.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 8.dp, top = 16.dp, end = 16.dp)
        )

        SellerMenuFeatureCard(
            iconUrl = SellerMigrationConstants.URL_FEED_ICON,
            titleString = stringResource(id = R.string.seller_menu_feed_and_play),
            descString = stringResource(id = R.string.seller_menu_feed_and_play_description),
            onActionClick = {
                onActionClick(SellerMenuActionClick.MIGRATION_FEED)
            },
            modifier = Modifier
                .constrainAs(feedCardRef) {
                    top.linkTo(statisticCardRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(financialCardRef.start)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 16.dp, top = 8.dp)
        )

        SellerMenuFeatureCard(
            iconUrl = SellerMigrationConstants.URL_FINANCE_ICON,
            titleString = stringResource(id = R.string.seller_menu_financial_service),
            descString = stringResource(id = R.string.seller_menu_financial_service_description),
            onActionClick = {
                onActionClick(SellerMenuActionClick.MIGRATION_FINANCE)
            },
            modifier = Modifier
                .constrainAs(financialCardRef) {
                    top.linkTo(promoCardRef.bottom)
                    start.linkTo(feedCardRef.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 8.dp, top = 8.dp, end = 16.dp)
        )
    }
}

@Composable
fun SellerMenuFeatureCard(
    iconUrl: String,
    titleString: String,
    descString: String,
    onActionClick: () -> Unit,
    modifier: Modifier
) {
    NestCard(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
        type = NestCardType.default(),
        onClick = onActionClick
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
        ) {
            val (featureImageRef, titleRef, descriptionRef) = createRefs()

            NestImage(
                source = ImageSource.Remote(iconUrl),
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(featureImageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            NestTypography(
                text = titleString.uppercase(),
                textStyle = NestTheme.typography.body3.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .constrainAs(titleRef) {
                        start.linkTo(featureImageRef.end)
                        top.linkTo(featureImageRef.top)
                        bottom.linkTo(featureImageRef.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 8.dp)
            )

            NestTypography(
                text = descString,
                textStyle = NestTheme.typography.body3.copy(
                    fontWeight = FontWeight.Normal,
                    color = NestTheme.colors.NN._950.copy(
                        alpha = 0.68f
                    )
                ),
                modifier = Modifier
                    .constrainAs(descriptionRef) {
                        start.linkTo(parent.start)
                        top.linkTo(titleRef.bottom)
                    }
                    .padding(top = 8.dp)
            )
        }
    }
}
