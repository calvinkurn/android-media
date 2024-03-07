package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
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
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.delay
import timber.log.Timber
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * @author by astidhiyaa on 22/01/24
 */
@Composable
internal fun FeedProductHighlight(
    product: FeedCardProductModel,
    isVisible: Boolean,
    onClose: () -> Unit,
    onAtcClick: (FeedCardProductModel) -> Unit,
    onProductClick: (FeedCardProductModel) -> Unit
) {
    val ctx = LocalContext.current

    NestTheme(darkTheme = false) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it - 60 },
                animationSpec = tween(
                    durationMillis = 600,
                    easing = EaseOut,
                ),
            ) + fadeIn(tween()),
            exit = fadeOut(tween(durationMillis = 400, easing = EaseOut)) + shrinkVertically(
                animationSpec = spring(
                    stiffness = Spring.DampingRatioNoBouncy,
                    visibilityThreshold = IntSize.VisibilityThreshold
                ),
                targetHeight = { -it })
        ) {
            Box(
                modifier = Modifier
                    .size(276.dp, 88.dp)
                    .padding(horizontal = if (product.isDiscount) 4.dp else 0.dp)
            ) {
                if (product.isDiscount) {
                    FeedCardRibbon(
                        text = product.discountFmt,
                        top = 8.dp,
                        modifier = Modifier.zIndex(16f)
                    )
                }
                ConstraintLayout(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = Color(0xB22E3137),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                        .clickable {
                            onProductClick(product)
                        }
                ) {
                    val (image, title, ogPrice, discountedPrice, btnAtc, btnClose) = createRefs()
                    // Product Image
                    NestImage(
                        source = ImageSource.Remote(source = product.coverUrl),
                        modifier = Modifier
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                            }
                            .size(72.dp)
                    )
                    // Product Title
                    NestTypography(
                        text = product.name,
                        maxLines = 2,
                        textStyle = NestTheme.typography.paragraph3.copy(
                            color = NestTheme.colors.NN._0
                        ),
                        modifier = Modifier.constrainAs(title) {
                            top.linkTo(image.top)
                            start.linkTo(image.end, 12.dp)
                            end.linkTo(btnClose.start, 4.dp)
                            width = Dimension.fillToConstraints
                        }
                    )
                    // Product Final Price
                    val finalPrice =
                        if (product.isDiscount) product.priceDiscountFmt else product.priceFmt
                    NestTypography(
                        text = finalPrice,
                        textStyle = NestTheme.typography.display2.copy(
                            color = NestTheme.colors.NN._0,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.constrainAs(ogPrice) {
                            top.linkTo(title.bottom, 4.dp)
                            start.linkTo(title.start)
                            end.linkTo(btnAtc.start, 4.dp)
                            width = Dimension.fillToConstraints
                        }
                    )

                    // Button ATC
                    AndroidView(
                        factory = {
                            UnifyButton(it).apply {
                                text = ctx.getText(R.string.feed_product_highlight_atc)
                                buttonVariant = UnifyButton.Variant.FILLED
                                buttonSize = UnifyButton.Size.MICRO
                                setDrawable(
                                    MethodChecker.getDrawable(
                                        ctx,
                                        unifycomponentsR.drawable.iconunify_cart
                                    ),
                                    UnifyButton.DrawablePosition.RIGHT
                                )
                                setOnClickListener { onAtcClick(product) }
                            }
                        },
                        modifier = Modifier
                            .constrainAs(btnAtc) {
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .width(48.dp)
                    ) {}

                    // Close Button: close show label again
                    NestIcon(
                        iconId = IconUnify.CLOSE,
                        colorLightEnable = NestTheme.colors.NN._0,
                        colorLightDisable = NestTheme.colors.NN._0,
                        colorNightEnable = NestTheme.colors.NN._0,
                        colorNightDisable = NestTheme.colors.NN._0,
                        modifier = Modifier
                            .constrainAs(btnClose) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            }
                            .size(16.dp)
                            .clickable { onClose() }
                    )

                    if (product.isDiscount) {
                        NestTypography(
                            text = product.priceFmt,
                            textStyle = NestTheme.typography.small.copy(
                                color = colorResource(id = R.color.feed_dms_highlight_slash),
                                textDecoration = TextDecoration.LineThrough
                            ),
                            modifier = Modifier.constrainAs(discountedPrice) {
                                top.linkTo(ogPrice.bottom, 4.dp)
                                start.linkTo(ogPrice.start)
                                end.linkTo(btnAtc.start, 4.dp)
                                width = Dimension.fillToConstraints
                            }
                        )
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
    isFocused: MutableState<Boolean>,
) {
    var isHighlightVisible by remember { mutableStateOf(false) }
    val highlightedProduct = products.firstOrNull { it.isHighlight }

    if (!isFocused.value) {
        isHighlightVisible = false
    } else {
        LaunchedEffect(key1 = key) {
            try {
                delay(5000L)
                isHighlightVisible = true
                highlightedProduct?.let { impressHighlight.invoke(it) }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    if (highlightedProduct != null) {
        FeedProductHighlight(
            product = highlightedProduct,
            isVisible = isHighlightVisible,
            onClose = {
                onProductHighlightClose.invoke()
                isHighlightVisible = false
            },
            onAtcClick = onAtcClick,
            onProductClick = onProductClick
        )
    }

    FeedProductLabel(
        products = products,
        totalProducts = totalProducts,
        isVisible = !isHighlightVisible || highlightedProduct == null,
        onClick = onProductLabelClick
    )
}
