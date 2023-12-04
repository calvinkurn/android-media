package com.tokopedia.unifyorderhistory.view.widget.buy_again

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.tokopedia.nest.principles.utils.ImageSource as ImageSource


@Preview()
@Composable
fun BuyAgainPreview() {
    UohBuyAgainWidget(recom = RecommendationWidget(
            title = "Waktunya beli kebutuhanmu lagi!",
            recommendationItemList = listOf(RecommendationItem(
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
            ),
            RecommendationItem(
                    name = "Fillet Ikan Pangasius",
                    imageUrl = "https://images.tokopedia.net/img/cache/250-square/VqbcmM/2022/8/22/8e7f3536-af84-4300-bfff-4832fb6f0f99.png",
                    price = "Rp25.500",
                    priceInt = 25500,
                    discountPercentageInt = 15,
                    discountPercentage = "15",
                    slashedPrice = "Rp30.000"
            ))
    ))
}

@Composable
fun UohBuyAgainWidget(recom: RecommendationWidget) {
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

        NestDivider(size = NestDividerSize.Small,
                modifier = Modifier.constrainAs(topDivider) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })

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
                            bottom.linkTo(list.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                contentDescription = null
        )

        NestTypography(
                text = recom.title,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
                textStyle = NestTheme.typography.display2.copy(
                    fontWeight = FontWeight.Bold
                )
        )

        NestIcon(
                iconId = IconUnify.CHEVRON_RIGHT,
                modifier = Modifier.constrainAs(chevron) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                })

        if (recom.recommendationItemList.size == 1) {
                UohBuyAgainCard(modifier = Modifier.constrainAs(uohCard) {
                    top.linkTo(title.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 30.dp)
                    end.linkTo(parent.end, margin = 30.dp)
                    bottom.linkTo(bottomDivider.top, margin = 10.dp)
                },
                productName = recom.recommendationItemList[0].name,
                productPrice = recom.recommendationItemList[0].price,
                slashedPrice = recom.recommendationItemList[0].slashedPrice,
                discountPercentage = recom.recommendationItemList[0].discountPercentage,
                imageUrl = recom.recommendationItemList[0].imageUrl,
                onProductCardClick = {})
        } else {
            UohBuyAgainList(
                    listBuyAgain = recom.recommendationItemList,
                    modifier = Modifier.constrainAs(list) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        bottom.linkTo(bottomDivider.top, margin = 10.dp)
                    }
            )
        }

        NestDivider(size = NestDividerSize.Large,
                modifier = Modifier.constrainAs(bottomDivider) {
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })
    }
}

@Composable
fun UohBuyAgainList(listBuyAgain: List<RecommendationItem> = emptyList(),
                    modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier,
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp)) {
        itemsIndexed(listBuyAgain) {index, item ->
            if (index < 3) {
                UohBuyAgainCard(
                        productName = listBuyAgain[index].name,
                        productPrice = listBuyAgain[index].price,
                        slashedPrice = listBuyAgain[index].slashedPrice,
                        discountPercentage = listBuyAgain[index].discountPercentage,
                        imageUrl = listBuyAgain[index].imageUrl,
                        onProductCardClick = {})
            } else if (index == 3) {
                ViewAllCard(modifier = modifier,
                        onButtonBuyAgainClick = {})
            }
        }
    }
}

@Composable
fun UohBuyAgainCard(
        productName: String,
        productPrice: String,
        slashedPrice: String,
        discountPercentage: String,
        imageUrl: String,
        modifier: Modifier = Modifier,
        onProductCardClick: () -> Unit) {
    NestCard(
            modifier = modifier
                    .heightIn(min = 56.dp)
                    .widthIn(min = 200.dp)
                    .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            type = NestCardType.Shadow,
            onClick = onProductCardClick
    ) {
        Row {
            ImageProduct(imageSource = ImageSource.Remote(imageUrl),
                         modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp))
            Column {
                NestTypography(
                        text = productName,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                        textStyle = NestTheme.typography.paragraph3
                )
                Row(
                        modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                ) {
                    NestTypography(
                            text = productPrice,
                            textStyle = NestTheme.typography.display2.copy(
                                    fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                    .align(Alignment.CenterVertically)
                    )
                    NestTypography(
                            text = slashedPrice,
                            textStyle = NestTheme.typography.small.copy(
                                    color = NestTheme.colors.NN._400,
                                    textDecoration = TextDecoration.LineThrough
                            ),
                            modifier = Modifier
                                    .padding(start = 4.dp)
                                    .align(Alignment.CenterVertically)
                    )
                    NestTypography(
                            text = "$discountPercentage%",
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
            NestButton(text = "Beli Lagi",
                    onClick = {},
                    variant = ButtonVariant.GHOST,
                    size = ButtonSize.SMALL,
                    modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
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
private fun ViewAllCard(modifier: Modifier = Modifier,
                       onButtonBuyAgainClick: () -> Unit) {
    NestCard(
            modifier = modifier
                    .heightIn(min = 56.dp)
                    .widthIn(min = 132.dp)
                    .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
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
        }
        /*ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (
                    bgImage,
                    seeAllLabel,
                    chevron
            ) = createRefs()

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
                                bottom.linkTo(list.bottom)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                    contentDescription = null
            )

            NestTypography(text = "Lihat Semua",
                    modifier = Modifier.constrainAs(seeAllLabel) {
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 4.dp)
                    },
                    textStyle = NestTheme.typography.small.copy(
                            color = NestTheme.colors.RN._500,
                            fontWeight = FontWeight.Bold
                    ))

            NestIcon(
                    iconId = IconUnify.CHEVRON_RIGHT,
                    modifier = Modifier
                            .size(16.dp)
                            .constrainAs(chevron) {
                                top.linkTo(seeAllLabel.top)
                                bottom.linkTo(seeAllLabel.bottom)
                                end.linkTo(parent.end, margin = 40.dp)
                            },
                    colorLightEnable = NestTheme.colors.NN._900,
                    colorNightEnable = NestTheme.colors.NN._900)
        }*/
    }
}