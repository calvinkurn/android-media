package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
    products: List<FeedCardProductModel>,
    totalProducts: Int,
) {
    val ctx = LocalContext.current
    val wording: String = when {
        products.size == FeedProductTagView.PRODUCT_COUNT_ZERO && totalProducts == FeedProductTagView.PRODUCT_COUNT_ZERO -> {
            return
        }

        products.size == FeedProductTagView.PRODUCT_COUNT_ONE -> {
            products.firstOrNull()?.name.orEmpty()
        }

        totalProducts > FeedProductTagView.PRODUCT_COUNT_NINETY_NINE -> {
            ctx.getString(R.string.feeds_tag_product_99_more_text)
        }

        else -> {
            val total =
                if (totalProducts > FeedProductTagView.PRODUCT_COUNT_ZERO) totalProducts else products.size
            ctx.getString(R.string.feeds_tag_product_text, total)
        }
    }
    NestTheme {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(28.dp)
                .shadow(elevation = 16.dp)
                .alpha(0.8f)
                .background(
                    colorResource(id = R.color.feed_dms_tag_product_background),
                    RoundedCornerShape(4.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            NestIcon(
                iconId = IconUnify.SHOPPING_BAG,
                colorLightEnable = NestTheme.colors.NN._0,
                colorNightEnable = NestTheme.colors.NN._0,
                modifier = Modifier
                    .size(20.dp)
                    .padding(4.dp)
                    .background(colorResource(id = R.color.feed_dms_tag_product_icon_background)),
            )
            NestTypography(
                text = wording,
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
    //FeedProductLabel()
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
