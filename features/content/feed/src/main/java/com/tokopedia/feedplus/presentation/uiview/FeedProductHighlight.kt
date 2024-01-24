package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
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
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * @author by astidhiyaa on 22/01/24
 */
@Composable
fun FeedProductHighlight(product: ContentTaggedProductUiModel = productHighlight) {
    NestTheme {
        AnimatedVisibility(
            visible = true, //TODO: Adjust visibility
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xB22E3137), shape = RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                val (image, title, ogPrice, discountedPrice, btnAtc, btnClose) = createRefs()
                //Product Image
                NestImage(
                    source = ImageSource.Remote(source = product.imageUrl),
                    type = NestImageType.Rect(20.dp),
                    modifier = Modifier
                        .size(72.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        })
                //Product Title
                NestTypography(text = product.title,
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
                NestTypography(
                    text = product.price.price.toString(),
                    textStyle = NestTheme.typography.heading3.copy(
                        color = NestTheme.colors.NN._0
                    ),
                    modifier = Modifier.constrainAs(ogPrice) {
                        width = Dimension.fillToConstraints
                        top.linkTo(title.bottom, 4.dp)
                        start.linkTo(title.start)
                        end.linkTo(btnAtc.start, 4.dp)
                    }) //TODO: adjust price
                //Product Discounted Price: show when product have a discount
                NestTypography(
                    text = product.price.price.toString(),
                    textStyle = NestTheme.typography.small.copy(
                        color = NestTheme.colors.NN._0
                    ),
                    modifier = Modifier.constrainAs(discountedPrice) {
                        width = Dimension.fillToConstraints
                        top.linkTo(ogPrice.bottom, 4.dp)
                        start.linkTo(ogPrice.start)
                        end.linkTo(btnAtc.start, 4.dp)
                    }) //TODO: adjust price
                //Button ATC
                NestButton(
                    text = "+",
                    variant = ButtonVariant.FILLED,
                    size = ButtonSize.SMALL,
                    trailingIcon = unifycomponentsR.drawable.iconunify_cart ,
                    onClick = {  },
                    modifier = Modifier.constrainAs(btnAtc) {
                        width = Dimension.fillToConstraints
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    })
                //Close Button
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
                        })
            }
        }
    }
}

@Preview
@Composable
internal fun ProductHighlightPreview() {
    FeedProductHighlight()
}

/**
 * TODO: Testing purpose.
 */
val productHighlight = ContentTaggedProductUiModel(
    id = "123",
    parentID = "1",
    showGlobalVariant = false,
    shop = ContentTaggedProductUiModel.Shop(id = "2", name = "CokiCoki"),
    title = "iPhone 156GB",
    imageUrl = "https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/26/7011d677-ed82-437c-822b-e769777ba2aa.png",
    price = ContentTaggedProductUiModel.NormalPrice(
        formattedPrice = "Rp.100.000",
        price = 100000.0
    ),
    appLink = "",
    campaign = ContentTaggedProductUiModel.Campaign(
        type = ContentTaggedProductUiModel.CampaignType.NoCampaign,
        status = ContentTaggedProductUiModel.CampaignStatus.Unknown,
        isExclusiveForMember = false
    ),
    affiliate = ContentTaggedProductUiModel.Affiliate("9", "aku"),
    stock = ContentTaggedProductUiModel.Stock.Available
)
