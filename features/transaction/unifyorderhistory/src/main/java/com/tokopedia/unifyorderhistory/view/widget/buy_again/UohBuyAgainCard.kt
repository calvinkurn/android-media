package com.tokopedia.unifyorderhistory.view.widget.buy_again

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.nest.principles.utils.ImageSource as ImageSource

@Composable
fun UohBuyAgainCard(
        productName: String,
        productPrice: String,
        slashedPrice: String,
        discountPercentage: String,
        imageUrl: String,
        modifier: Modifier = Modifier,
        onProductCardClick: () -> Unit,
        onButtonBuyAgainClick: () -> Unit) {
    NestCard(
            modifier = modifier
                    .heightIn(min = 56.dp)
                    .widthIn(min = 200.dp)
                    .padding(6.dp),
            type = NestCardType.Shadow,
            onClick = onProductCardClick
    ) {
        Column {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (
                    image,
                    name,
                    price,
                    slashPrice,
                    discPercentage,
                    button
                ) = createRefs()

                ImageProduct(
                        imageSource = ImageSource.Remote(imageUrl),
                        modifier = Modifier.constrainAs(image) {
                            top.linkTo(parent.top, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
                )
                NestTypography(
                        text = productName,
                        modifier = Modifier.constrainAs(name) {
                            top.linkTo(image.top)
                            start.linkTo(image.end, margin = 8.dp)
                        },
                        textStyle = NestTheme.typography.paragraph3
                )
                NestTypography(
                        text = productPrice,
                        modifier = Modifier.constrainAs(price) {
                            top.linkTo(name.bottom, margin = 4.dp)
                            start.linkTo(name.start)
                        },
                        textStyle = NestTheme.typography.display2.copy(
                                fontWeight = FontWeight.Bold
                        )
                )
                NestTypography(
                        text = slashedPrice,
                        modifier = Modifier.constrainAs(slashPrice) {
                            top.linkTo(price.top)
                            bottom.linkTo(price.bottom)
                            start.linkTo(price.end, margin = 4.dp)
                        },
                        textStyle = NestTheme.typography.small.copy(
                                color = NestTheme.colors.NN._400,
                                textDecoration = TextDecoration.LineThrough
                        )
                )
                NestTypography(
                        text = "$discountPercentage%",
                        modifier = Modifier.constrainAs(discPercentage) {
                            top.linkTo(slashPrice.top)
                            bottom.linkTo(slashPrice.bottom)
                            start.linkTo(slashPrice.end, margin = 4.dp)
                        },
                        textStyle = NestTheme.typography.small.copy(
                                color = NestTheme.colors.RN._500,
                                fontWeight = FontWeight.Bold
                        )
                )
                NestButton(text = "Beli Lagi",
                        onClick = {},
                        variant = ButtonVariant.GHOST,
                        size = ButtonSize.SMALL,
                        modifier = Modifier.constrainAs(button) {
                            end.linkTo(parent.end, margin = 8.dp)
                            top.linkTo(parent.top, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                )
            }
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