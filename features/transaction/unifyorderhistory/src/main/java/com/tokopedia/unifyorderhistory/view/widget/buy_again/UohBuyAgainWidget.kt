package com.tokopedia.unifyorderhistory.view.widget.buy_again

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.viewallcard.compose.NestViewAllCard
import com.tokopedia.nest.principles.utils.ImageSource as ImageSource

/*@Preview
@Composable
fun BuyAgainPreview() {
    UohBuyAgainWidget(
        recom = RecommendationWidget(
            title = "Waktunya beli kebutuhanmu lagi!",
            recommendationItemList = listOf(
                RecommendationItem(
                    name = "Fillet Ikan Pangasius",
                    imageUrl = "https://images.tokopedia.net/img/cache/250-square/VqbcmM/2022/8/22/8e7f3536-af84-4300-bfff-4832fb6f0f99.png",
                    price = "Rp25.500",
                    priceInt = 25500,
                    discountPercentageInt = 15,
                    discountPercentage = "15",
                    slashedPrice = "Rp30.000"
                )*//*,
            RecommendationItem(
                    name = "Fillet Ikan Pangasius",
                    imageUrl = "https://images.tokopedia.net/img/cache/250-square/VqbcmM/2022/8/22/8e7f3536-af84-4300-bfff-4832fb6f0f99.png",
                    price = "Rp25.500",
                    priceInt = 25500,
                    discountPercentageInt = 15,
                    discountPercentage = "15",
                    slashedPrice = "Rp30.000"
            ),
            RecommendationItem(
                    name = "Fillet Ikan Pangasius",
                    imageUrl = "https://images.tokopedia.net/img/cache/250-square/VqbcmM/2022/8/22/8e7f3536-af84-4300-bfff-4832fb6f0f99.png",
                    price = "Rp25.500",
                    priceInt = 25500,
                    discountPercentageInt = 15,
                    discountPercentage = "15",
                    slashedPrice = "Rp30.000"
            ),
            RecommendationItem(
                    name = "Fillet Ikan Pangasius",
                    imageUrl = "https://images.tokopedia.net/img/cache/250-square/VqbcmM/2022/8/22/8e7f3536-af84-4300-bfff-4832fb6f0f99.png",
                    price = "Rp25.500",
                    priceInt = 25500,
                    discountPercentageInt = 15,
                    discountPercentage = "15",
                    slashedPrice = "Rp30.000"
            )*//*
            )
        )
    )
}*/

@Composable
fun UohBuyAgainWidget(
    recom: RecommendationWidget,
    onChevronClicked: () -> Unit = {},
    onProductCardClick: (recommItem: RecommendationItem, index: Int) -> Unit,
    onButtonBuyAgainClick: (recommItem: RecommendationItem, index: Int) -> Unit,
    onSeeAllCardClick: () -> Unit,
    onItemScrolled: (recommItem: RecommendationItem, index: Int) -> Unit,
    onWidgetImpressed: () -> Unit
) {
    onWidgetImpressed()
    val backgroundImg = "https://images.tokopedia.net/img/android/uoh/buy_again_bg.png"
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (
            topDivider,
            background,
            title,
            chevron,
            uohCard,
            list,
            bottomDivider
        ) = createRefs()

        NestDivider(
            size = NestDividerSize.Small,
            modifier = Modifier.constrainAs(topDivider) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        NestImage(
            type = NestImageType.Rect(0.dp),
            contentScale = ContentScale.FillWidth,
            source = ImageSource.Remote(backgroundImg),
            modifier = Modifier
                .tag("background")
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    if (recom.recommendationItemList.size > 1) {
                        bottom.linkTo(list.bottom)
                    } else {
                        bottom.linkTo(uohCard.bottom)
                    }
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            contentDescription = null
        )

        HtmlLinkHelper(LocalContext.current, recom.title).spannedString?.let {
            NestTypography(
                text = it,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
                textStyle = NestTheme.typography.display2.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        NestIcon(
            iconId = IconUnify.CHEVRON_RIGHT,
            modifier = Modifier
                .constrainAs(chevron) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .clickable { onChevronClicked.invoke() }
        )

        if (recom.recommendationItemList.size == 1) {
            UohBuyAgainCard(
                index = 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(uohCard) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 30.dp)
                        end.linkTo(parent.end, margin = 30.dp)
                        bottom.linkTo(bottomDivider.top, margin = 10.dp)
                    },
                recommItem = recom.recommendationItemList[0],
                onProductCardClick = onProductCardClick,
                onBuyAgainButtonClicked = onButtonBuyAgainClick,
                onItemScrolled = onItemScrolled
            )
        } else {
            UohBuyAgainList(
                listBuyAgain = recom.recommendationItemList,
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(title.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(bottomDivider.top, margin = 10.dp)
                },
                onProductCardClick = onProductCardClick,
                onButtonBuyAgainClick = onButtonBuyAgainClick,
                onSeeAllCardClick = { onSeeAllCardClick.invoke() },
                onItemScrolled = onItemScrolled
            )
        }

        NestDivider(
            size = NestDividerSize.Large,
            modifier = Modifier.constrainAs(bottomDivider) {
                bottom.linkTo(parent.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun UohBuyAgainList(
    listBuyAgain: List<RecommendationItem> = emptyList(),
    modifier: Modifier = Modifier,
    onProductCardClick: (recommItem: RecommendationItem, index: Int) -> Unit,
    onButtonBuyAgainClick: (recommItem: RecommendationItem, index: Int) -> Unit,
    onSeeAllCardClick: () -> Unit,
    onItemScrolled: (recommItem: RecommendationItem, index: Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
    ) {
        itemsIndexed(listBuyAgain) { index, item ->
            if (index < 3) {
                UohBuyAgainCard(
                    index = index,
                    recommItem = listBuyAgain[index],
                    onProductCardClick = onProductCardClick,
                    onBuyAgainButtonClicked = onButtonBuyAgainClick,
                    onItemScrolled = onItemScrolled
                )
            } else if (index == 3) {
                NestViewAllCard(
                    modifier = modifier
                        .height(62.dp)
                        .width(130.dp)
                        .padding(start = 6.dp, top = 6.dp),
                    enableCta = true,
                    ctaText = "Lihat Semua",
                    subtitle = " ",
                    onClick = onSeeAllCardClick
                )
                /*ViewAllCard(modifier = modifier,
                        onButtonBuyAgainClick = {})*/
            }
        }
    }
}

@Composable
fun UohBuyAgainCard(
    index: Int,
    recommItem: RecommendationItem,
    modifier: Modifier = Modifier,
    onProductCardClick: (RecommendationItem, Int) -> Unit,
    onBuyAgainButtonClicked: (RecommendationItem, Int) -> Unit,
    onItemScrolled: (RecommendationItem, Int) -> Unit
) {
    onItemScrolled(recommItem, index)

    NestCard(
        modifier = modifier
            .heightIn(min = 56.dp)
            .widthIn(max = 315.dp)
            .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
        type = NestCardType.Shadow,
        onClick = { onProductCardClick.invoke(recommItem, index) }
    ) {
        Row {
            ImageProduct(
                imageSource = ImageSource.Remote(recommItem.imageUrl),
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            )
            Column {
                NestTypography(
                    text = recommItem.name,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp)
                        .widthIn(max = 142.dp),
                    textStyle = NestTheme.typography.paragraph3,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                ) {
                    NestTypography(
                        text = recommItem.price,
                        textStyle = NestTheme.typography.display2.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    NestTypography(
                        text = recommItem.slashedPrice,
                        textStyle = NestTheme.typography.small.copy(
                            color = NestTheme.colors.NN._400,
                            textDecoration = TextDecoration.LineThrough
                        ),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically)
                    )
                    if (recommItem.discountPercentage.isNotEmpty()) {
                        NestTypography(
                            text = recommItem.discountPercentage,
                            textStyle = NestTheme.typography.small.copy(
                                color = NestTheme.colors.RN._500,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            NestButton(
                text = "Beli Lagi",
                onClick = { onBuyAgainButtonClicked.invoke(recommItem, index) },
                variant = ButtonVariant.GHOST,
                size = ButtonSize.MICRO,
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun ImageProduct(imageSource: ImageSource, modifier: Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(bottomStart = 4.dp))
            .background(NestTheme.colors.TN._50)
    ) {
        NestImage(
            source = imageSource,
            modifier = Modifier
                .tag("ivRecommendedProduct")
                .size(40.dp)
                .align(Alignment.Center),
            contentDescription = null
        )
    }
}

@Composable
fun ItemImpression(key: Long, lazyListState: LazyListState, onItemViewed: () -> Unit) {
    /*val isItemWithKeyInView by remember {
        derivedStateOf {
            lazyListState.layoutInfo
                    .visibleItemsInfo
                    .any { it.key == key }
        }
    }

    if (isItemWithKeyInView) {
        LaunchedEffect(Unit) {
            onItemViewed()
        }
    }*/
    LaunchedEffect(Unit) {
        onItemViewed()
    }
}

@Composable
private fun ViewAllCard(
    modifier: Modifier = Modifier,
    onButtonBuyAgainClick: () -> Unit
) {
    NestCard(
        modifier = modifier
            .heightIn(min = 68.dp)
            .widthIn(min = 110.dp)
            .padding(start = 6.dp, top = 6.dp, bottom = 6.dp),
        type = NestCardType.Shadow,
        onClick = onButtonBuyAgainClick
    ) {
        val backgroundImg = "https://images.tokopedia.net/img/android/uoh/uoh_buy_again_see_all_bg.png"
        Box {
            NestImage(
                type = NestImageType.Rect(0.dp),
                contentScale = ContentScale.FillWidth,
                source = ImageSource.Remote(backgroundImg),
                modifier = Modifier.align(Alignment.TopEnd),
                contentDescription = null
            )

            Row(modifier = Modifier.align(Alignment.BottomStart)) {
                NestTypography(
                    text = "Lihat Semua",
                    modifier = Modifier
                        .padding(start = 10.dp),
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.GN._500,
                        fontWeight = FontWeight.Bold
                    )
                )

                NestIcon(
                    iconId = IconUnify.CHEVRON_RIGHT,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    colorLightEnable = NestTheme.colors.GN._500,
                    colorNightEnable = NestTheme.colors.GN._500
                )
            }
        }
    }
}
