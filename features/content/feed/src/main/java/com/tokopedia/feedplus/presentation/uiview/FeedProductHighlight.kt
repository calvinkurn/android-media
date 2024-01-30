package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import kotlinx.coroutines.delay
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * @author by astidhiyaa on 22/01/24
 */
//add listener
@Composable
fun FeedProductHighlight(
    product: FeedCardProductModel,
    isVisible: Boolean,
    onClose: () -> Unit,
    onAtcClick: (FeedCardProductModel) -> Unit,
    onProductClick: (FeedCardProductModel) -> Unit,
) {
    NestTheme {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
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
                    type = NestImageType.Rect(20.dp),
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
                //Product Original Price
                val originalPrice =
                    if (product.isDiscount) product.priceOriginalFmt else product.priceFmt
                NestTypography(
                    text = originalPrice,
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
                NestButton(
                    text = "+",
                    variant = ButtonVariant.FILLED,
                    size = ButtonSize.SMALL,
                    trailingIcon = unifycomponentsR.drawable.iconunify_cart,
                    onClick = { },
                    modifier = Modifier.constrainAs(btnAtc) {
                        width = Dimension.fillToConstraints
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                        .clickable { onAtcClick(product) })
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
                        text = product.priceDiscountFmt,
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

@Composable
fun ProductTagItems(
    products: List<FeedCardProductModel>,
    totalProducts: Int,
    key: String,
    onAtcClick: (FeedCardProductModel) -> Unit,
    onProductClick: (FeedCardProductModel) -> Unit,
    onProductLabelClick: () -> Unit,
) {
    var needToBeShown by remember { mutableStateOf(false) }

    Column {
        LaunchedEffect(key1 = key, block = {
            delay(5000L)
            needToBeShown = true
        })

        FeedProductLabel(
            products = products,
            totalProducts = totalProducts,
            isVisible = !needToBeShown,
            onClick = onProductLabelClick
        )
        val highlightedProduct = products.firstOrNull() ?: return
        FeedProductHighlight(
            product = highlightedProduct,
            isVisible = needToBeShown,
            onClose = { needToBeShown = false },
            onAtcClick = { onAtcClick }, onProductClick = { onProductClick })
    }
}
