package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * @author by astidhiyaa on 24/01/24
 */
@Composable
fun FeedProductLabel(
    //TODO: testing purpose only
    products: List<FeedCardProductModel> = listOf(productLabel),
    totalProducts: Int = 10,
) {
    //TODO: adjust logic
    NestTheme {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(28.dp)
                .shadow(elevation = 16.dp)
                .alpha(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NestIcon(
                iconId = IconUnify.SHOPPING_BAG,
                colorLightEnable = NestTheme.colors.NN._0,
                colorNightEnable = NestTheme.colors.NN._0,
                modifier = Modifier
                    .size(20.dp)
                    .padding(4.dp)
                    .paint(painter = painterResource(id = R.drawable.feed_tag_product_icon_yellow_background))
            )
            NestTypography(
                text = products.firstOrNull()?.name.orEmpty(),
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.NN._0
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .widthIn(0.dp, 240.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}

@Preview
@Composable
internal fun ProductLabelPreview() {
    FeedProductLabel()
}

/**
 * TODO: testing purpose only
 */

val productLabel = FeedCardProductModel(
    id = "123",
    parentID = "1",
    isParent = false,
    hasVariant = false,
    name = "iPhone 156GB",
    coverUrl = "https://images.tokopedia.net/img/cache/700/VqbcmM/2023/9/26/7011d677-ed82-437c-822b-e769777ba2aa.png",
    price = 100000.0,
    priceFmt = "Rp.100.000",
    isAvailable = true
)
