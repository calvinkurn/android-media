package com.tokopedia.content.product.preview.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * @author by astidhiyaa on 23/11/23
 */
@Composable
fun MediaBottomNav(
    product: BottomNavUiModel,
    onAtcClicked: () -> Unit = {}
) {
    NestTheme(darkTheme = true) {
        if (product == BottomNavUiModel.Empty) {
            RenderLoading()
        } else {
            RenderContent(product = product, onAtcClicked)
        }
    }
}

@Composable
private fun RenderLoading() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(NestTheme.colors.NN._0)
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
    ) {
        val (title, ogPrice, atcBtn) = createRefs()
        val loaderVariant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(rounded = 8.dp))

        NestLoader(
            variant = loaderVariant,
            modifier = Modifier
                .height(20.dp)
                .width(200.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        NestLoader(
            variant = loaderVariant,
            modifier = Modifier
                .height(20.dp)
                .width(90.dp)
                .constrainAs(ogPrice) {
                    start.linkTo(parent.start)
                    top.linkTo(title.bottom, 6.dp)
                    bottom.linkTo(parent.bottom)
                }
        )

        NestLoader(
            variant = loaderVariant,
            modifier = Modifier
                .height(38.dp)
                .width(100.dp)
                .constrainAs(atcBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun RenderContent(
    product: BottomNavUiModel,
    onAtcClicked: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(NestTheme.colors.NN._0)
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
    ) {
        val (title, ogPrice, slashedPrice, discountTag, atcBtn) = createRefs()

        NestTypography(
            text = product.title,
            maxLines = 1,
            textStyle = NestTheme.typography.small.copy(
                color = colorResource(id = R.color.product_preview_dms_bottom_title)
            ),
            modifier = Modifier.constrainAs(title) {
                width = Dimension.fillToConstraints
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(atcBtn.start, 8.dp)
            }
        )
        // Final Price
        if (product.price !is BottomNavUiModel.Price.NettPrice) {
            NestTypography(
                text = product.price.finalPrice,
                maxLines = 1,
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._1000
                ),
                modifier = Modifier
                    .heightIn(0.dp, 208.dp)
                    .constrainAs(ogPrice) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        start.linkTo(title.start)
                        top.linkTo(title.bottom, 4.dp)
                    }
            )
        }

        NestButton(
            text = product.buttonState.text,
            onClick = onAtcClicked,
            variant = ButtonVariant.GHOST_INVERTED,
            size = ButtonSize.SMALL,
            isClickable = product.buttonState != BottomNavUiModel.ButtonState.Inactive,
            modifier = Modifier
                .constrainAs(atcBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        if (product.price is BottomNavUiModel.Price.DiscountedPrice || product.price is BottomNavUiModel.Price.NettPrice) {
            NestTypography(
                text = product.price.ogPriceFmt,
                maxLines = 1,
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.NN._400,
                    textDecoration = TextDecoration.LineThrough
                ),
                modifier = Modifier
                    .constrainAs(slashedPrice) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        start.linkTo(ogPrice.end, 4.dp)
                        bottom.linkTo(ogPrice.bottom)
                        top.linkTo(ogPrice.top)
                    }
            )
        }
        if (product.price is BottomNavUiModel.Price.DiscountedPrice) {
            NestTypography(
                text = product.price.discountPercentage,
                maxLines = 1,
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.RN._500,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .constrainAs(discountTag) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        start.linkTo(slashedPrice.end, 2.dp)
                        bottom.linkTo(slashedPrice.bottom)
                        top.linkTo(slashedPrice.top)
                    }
            )
        }

        if (product.price is BottomNavUiModel.Price.NettPrice) {
            Row(modifier = Modifier
                .wrapContentSize()
                .border(1.dp, NestTheme.colors.RN._50, RoundedCornerShape(5.dp))
                .background(
                    NestTheme.colors.RN._200.copy(alpha = 0.2f),
                    RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .constrainAs(ogPrice) {
                    start.linkTo(parent.start)
                    top.linkTo(title.bottom, 6.dp)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nett_price),
                    contentDescription = "",
                )
                NestTypography(
                    text = product.price.nettPriceFmt,
                    maxLines = 1,
                    textStyle = NestTheme.typography.heading5.copy(
                        color = NestTheme.colors.RN._200
                    ),
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MediaBottomNavPreview() {
    MediaBottomNav(
        product = BottomNavUiModel.Empty
    ) { }
}
