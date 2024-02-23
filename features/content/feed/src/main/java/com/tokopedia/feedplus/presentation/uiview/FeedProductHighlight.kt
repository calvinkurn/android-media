package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.delay
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * @author by astidhiyaa on 22/01/24
 */
@Composable
fun FeedProductHighlight(
    product: FeedCardProductModel,
    isVisible: Boolean,
    onClose: () -> Unit,
    onAtcClick: (FeedCardProductModel) -> Unit,
    onProductClick: (FeedCardProductModel) -> Unit,
) {
    val ctx = LocalContext.current
    val density = LocalDensity.current

    NestTheme(darkTheme = false) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically {
                with(density) { 60.dp.roundToPx() }
            },
            exit = slideOutVertically {
                with(density) { 60.dp.roundToPx()}
            }
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 4.dp)
            ) {
                if (product.isDiscount) {
                    NestRibbon(
                        text = product.discountFmt, top = 2.dp, modifier = Modifier.zIndex(16f)
                    )
                }
                ConstraintLayout(
                    modifier = Modifier
                        .size(276.dp, 88.dp)
                        .background(color = Color(0xB22E3137), shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                        .clickable {
                            onProductClick(product)
                        }
                ) {
                    val (image, title, ogPrice, discountedPrice, btnAtc, btnClose) = createRefs()
                    //Product Image
                    NestImage(
                        source = ImageSource.Remote(source = product.coverUrl),
                        type = NestImageType.Rect(12.dp),
                        modifier = Modifier
                            .size(72.dp)
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                            })
                    //Product Title
                    NestTypography(text = product.name,
                        maxLines = 2,
                        textStyle = NestTheme.typography.paragraph3.copy(
                            color = NestTheme.colors.NN._0
                        ),
                        modifier = Modifier.constrainAs(title) {
                            width = Dimension.fillToConstraints
                            top.linkTo(image.top)
                            start.linkTo(image.end, 12.dp)
                            end.linkTo(btnClose.start, 4.dp)
                        })
                    //Product Final Price
                    val finalPrice =
                        if (product.isDiscount) product.priceDiscountFmt else product.priceFmt
                    NestTypography(
                        text = finalPrice,
                        textStyle = NestTheme.typography.heading3.copy(
                            color = NestTheme.colors.NN._0
                        ),
                        modifier = Modifier.constrainAs(ogPrice) {
                            width = Dimension.fillToConstraints
                            top.linkTo(title.bottom, 4.dp)
                            start.linkTo(title.start)
                            end.linkTo(btnAtc.start, 4.dp)
                        })

                    //Button ATC
                    AndroidView(factory = {
                        UnifyButton(it).apply {
                            text = ctx.getText(R.string.feed_product_highlight_atc)
                            buttonVariant = UnifyButton.Variant.FILLED
                            buttonSize = UnifyButton.Size.MICRO
                            setDrawable(
                                MethodChecker.getDrawable(
                                    ctx,
                                    unifycomponentsR.drawable.iconunify_cart
                                ), UnifyButton.DrawablePosition.RIGHT
                            )
                            setOnClickListener { onAtcClick(product) }
                        }
                    }, modifier = Modifier
                        .constrainAs(btnAtc) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }) {}

                    //Close Button: close show label again
                    NestIcon(iconId = IconUnify.CLOSE,
                        colorLightEnable = NestTheme.colors.NN._0,
                        colorLightDisable = NestTheme.colors.NN._0,
                        colorNightEnable = NestTheme.colors.NN._0,
                        colorNightDisable = NestTheme.colors.NN._0,
                        modifier = Modifier
                            .size(16.dp)
                            .constrainAs(btnClose) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            }
                            .clickable { onClose() }
                    )

                    if (product.isDiscount) {
                        NestTypography(
                            text = product.priceFmt,
                            textStyle = NestTheme.typography.small.copy(
                                color = NestTheme.colors.NN._0,
                                textDecoration = TextDecoration.LineThrough
                            ),
                            modifier = Modifier.constrainAs(discountedPrice) {
                                width = Dimension.fillToConstraints
                                top.linkTo(ogPrice.bottom, 4.dp)
                                start.linkTo(ogPrice.start)
                                end.linkTo(btnAtc.start, 4.dp)
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun ProductTagItems(
    products: List<FeedCardProductModel>,
    totalProducts: Int,
    key: String,
    onAtcClick: (FeedCardProductModel) -> Unit,
    onProductClick: (FeedCardProductModel) -> Unit,
    onProductLabelClick: () -> Unit,
    onProductHighlightClose: () -> Unit,
    impressHighlight: (FeedCardProductModel) -> Unit,
) {
    var needToBeShown by remember { mutableStateOf(false) }
    val highlightedProduct = products.firstOrNull { it.isHighlight }

    LaunchedEffect(key1 = key, key2 = highlightedProduct, block = {
        delay(5000L)
        needToBeShown = true
        if (highlightedProduct != null) {
            impressHighlight(highlightedProduct)
        }
    })

    Column {
        FeedProductLabel(
            products = products,
            totalProducts = totalProducts,
            isVisible = !needToBeShown || highlightedProduct == null,
            onClick = onProductLabelClick
        )

        if (highlightedProduct != null) {
            FeedProductHighlight(
                product = highlightedProduct,
                isVisible = needToBeShown,
                onClose = {
                    onProductHighlightClose.invoke()
                    needToBeShown = false
                },
                onAtcClick = onAtcClick, onProductClick = onProductClick
            )
        }
    }
}
