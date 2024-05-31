package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
internal fun FeedProductLabel(
    products: List<FeedCardProductModel>,
    totalProducts: Int,
    isVisible: Boolean,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val wording: String = when {
        products.size == PRODUCT_COUNT_ZERO && totalProducts == PRODUCT_COUNT_ZERO -> {
            return
        }

        products.size == PRODUCT_COUNT_ONE -> {
            products.firstOrNull()?.name.orEmpty()
        }

        totalProducts > PRODUCT_COUNT_NINETY_NINE -> {
            ctx.getString(R.string.feeds_tag_product_99_more_text)
        }

        else -> {
            val total = if (totalProducts > PRODUCT_COUNT_ZERO) totalProducts else products.size
            ctx.getString(R.string.feeds_tag_product_text, total)
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 600, easing = EaseOut)),
        exit = fadeOut(animationSpec = tween(easing = EaseOut, durationMillis = 400, delayMillis = 200)),
    ) {
        NestTheme(darkTheme = false, isOverrideStatusBarColor = false) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(28.dp)
                    .shadow(elevation = 16.dp)
                    .alpha(0.7f)
                    .background(
                        colorResource(id = R.color.feed_dms_tag_product_background),
                        RoundedCornerShape(4.dp)
                    )
                    .clickable { onClick() },
            ) {
                NestIcon(
                    iconId = IconUnify.SHOPPING_BAG,
                    colorLightEnable = NestTheme.colors.NN._0,
                    colorNightEnable = NestTheme.colors.NN._0,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                        .background(colorResource(id = R.color.feed_dms_tag_product_icon_background)),
                )
                NestTypography(
                    text = wording,
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._0
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .wrapContentSize()
                        .widthIn(0.dp, 240.dp)
                        .padding(end = 8.dp)
                )
            }
        }
    }
}

internal const val PRODUCT_COUNT_ZERO = 0
internal const val PRODUCT_COUNT_ONE = 1
internal const val PRODUCT_COUNT_NINETY_NINE = 99
