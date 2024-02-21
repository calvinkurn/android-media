package com.tokopedia.catalog.ui.composeView

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.catalog.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestGN
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.ui.NestYN
import com.tokopedia.nest.principles.utils.ImageSource
import kotlinx.coroutines.delay

@Composable
fun CtaSellerOffering(
    shopName: String,
    badge: String,
    price: String,
    slashPrice: String,
    rating: String,
    sold: String,
    variantText: String,
    theme: Boolean,
    onClick: (() -> Unit)?
) {
    var switchState by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val maxSwitchState = if (variantText.isNotEmpty() && rating.isNotEmpty()) 2 else 1
    if (rating.isNotEmpty() || variantText.isNotEmpty()) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(2000)
                if (switchState <= maxSwitchState) {
                    switchState += 1
                } else {
                    switchState = 0
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .background(
                color = colorResource(
                    id = R.color.catalog_dms_dark_color
                ).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(44.dp)
                .background(NestGN.light._500, shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp))
        ) {
            NestIcon(
                iconId = IconUnify.CART,
                modifier = Modifier
                    .padding(8.dp),
                colorLightEnable = NestNN.light._0,
                colorNightEnable = NestNN.light._0
            )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                this@Column.AnimatedVisibility(
                    visible = switchState == 0,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    ShopInfo(theme, shopName = shopName, badge = badge)
                }
                this@Column.AnimatedVisibility(
                    visible = switchState == 1 && rating.isNotEmpty(),
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    ShopCredibility(context, theme, rating, sold)
                }
                this@Column.AnimatedVisibility(
                    visible = (switchState == 2 && variantText.isNotEmpty()) || (switchState == 1 && rating.isEmpty() && variantText.isNotEmpty()),
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    ProductVariant(theme, variantText)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                NestTypography(
                    text = price,
                    textStyle = NestTheme.typography.display2.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(
                            id = getColor(
                                theme,
                                R.color.catalog_dms_light_color_banner,
                                R.color.catalog_dms_light_color
                            )
                        )
                    ),
                    maxLines = 1
                )
                if (slashPrice.isNotEmpty()) {
                    NestTypography(
                        text = slashPrice,
                        textStyle = NestTheme.typography.small.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = colorResource(
                                id = getColor(
                                    theme,
                                    R.color.catalog_dms_dark_color,
                                    R.color.catalog_dms_light_color
                                )
                            ).copy(alpha = 0.5f),
                            textDecoration = TextDecoration.LineThrough
                        ),
                        maxLines = 1,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductVariant(theme: Boolean, productVariant: String) {
    NestTypography(
        text = productVariant,
        textStyle = NestTheme.typography.display3.copy(
            fontWeight = FontWeight.Normal,
            color = colorResource(
                id = getColor(
                    theme,
                    R.color.catalog_dms_light_color_banner,
                    R.color.catalog_dms_light_color
                )
            )
        )
    )
}

@Composable
private fun ShopInfo(theme: Boolean, shopName: String, badge: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        NestTypography(
            text = "Beli dari:",
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Normal,
                color = colorResource(
                    id = getColor(
                        theme,
                        R.color.catalog_dms_light_color_banner,
                        R.color.catalog_dms_light_color
                    )
                ).copy(alpha = 0.8f)
            )
        )

        NestImage(
            ImageSource.Remote(badge),
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        NestTypography(
            text = shopName,
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Normal,
                color = colorResource(
                    id = getColor(
                        theme,
                        R.color.catalog_dms_light_color_banner,
                        R.color.catalog_dms_light_color
                    )
                ).copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(start = 2.dp),
            maxLines = 1
        )
    }
}

@Composable
private fun ShopCredibility(context: Context, theme: Boolean, rating: String, sold: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        NestIcon(
            iconId = IconUnify.STAR_FILLED,
            colorLightEnable = NestYN.light._300,
            colorNightEnable = NestNN.dark._300,
            modifier = Modifier
                .height(12.dp)
                .width(12.dp)
        )
        NestTypography(
            text = rating,
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Normal,
                color = colorResource(
                    id = getColor(
                        theme,
                        R.color.catalog_dms_light_color_banner,
                        R.color.catalog_dms_light_color
                    )
                ).copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(end = 4.dp, start = 2.dp),
            maxLines = 1
        )
        Box(
            modifier = Modifier
                .background(
                    color = NestNN.light._0.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .height(2.dp)
                .width(2.dp)
        )
        NestTypography(
            text = sold,
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Normal,
                color = colorResource(
                    id = getColor(
                        theme,
                        R.color.catalog_dms_light_color_banner,
                        R.color.catalog_dms_light_color
                    )
                ).copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(start = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
@Preview
private fun CtaSellerOfferingPreview() {
    var switchState by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            switchState = !switchState
        }
    }
    Row(
        modifier = Modifier
            .background(
                color = colorResource(
                    id = R.color.catalog_dms_dark_color
                ).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .background(NestGN.light._500, shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp))
        ) {
            NestIcon(
                iconId = IconUnify.CART,
                modifier = Modifier
                    .padding(12.dp),
                colorLightEnable = NestNN.light._0,
                colorNightEnable = NestNN.light._0
            )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Crossfade(
                targetState = switchState,
                animationSpec = tween(durationMillis = 1000),
                label = ""
            ) { targetState ->
                if (targetState) {
                    ShopInfo(
                        true,
                        shopName = "Samsung",
                        badge = "https://images.tokopedia.net/img/official_store_badge.png"
                    )
                } else {
                    ShopCredibility(context, true, "5.0", "20rb Terjual")
                }
            }
            Row {
                NestTypography(
                    text = "Rp9.000.000",
                    textStyle = NestTheme.typography.display2.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = NestTheme.colors.NN._0
                    )
                )
                NestTypography(
                    text = "Rp9.000.000",
                    textStyle = NestTheme.typography.display2.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = NestTheme.colors.NN._0.copy(alpha = 0.5f),
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }
        }
    }
}

private fun getColor(isDark: Boolean, lightColor: Int, darkColor: Int): Int {
    return if (isDark) {
        darkColor
    } else {
        lightColor
    }
}
