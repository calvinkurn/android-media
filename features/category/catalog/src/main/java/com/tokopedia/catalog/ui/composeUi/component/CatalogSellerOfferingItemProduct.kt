@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.tokopedia.catalog.ui.composeUi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.model.CatalogProductListState
import com.tokopedia.catalog.ui.model.CatalogProductListUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.compose.NestProgressBar
import com.tokopedia.catalogcommon.R as catalogcommonR

@Composable
fun ItemProduct(
    onClickItem: (CatalogProductListUiModel.CatalogProductUiModel) -> Unit,
    onClickAtc: (CatalogProductListUiModel.CatalogProductUiModel) -> Unit,
    catalogProductState: MutableState<CatalogProductListState>,
    index: Int
) {
    val catalogProduct = catalogProductState.value.data.orEmpty()[index]
    val productBenefit = catalogProduct.labelGroups.find {
        it.position == "ri_product_benefit"
    }

    val productOffer = catalogProduct.labelGroups.find {
        it.position == "ri_product_offer"
    }

    val totalSold = catalogProduct.labelGroups.find {
        it.position == "ri_product_credibility"
    }

    val freeOngkir = catalogProduct.labelGroups.find {
        it.position == "overlay_2"
    }

    Column(
        Modifier
            .clickable {
                onClickItem.invoke(catalogProduct)
            }
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            Modifier
                .background(colorResource(id = R.color.catalog_dms_light_color)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            if (catalogProduct.stock.isHidden) {
                ProductVisualWithoutStock(
                    catalogProduct.mediaUrl.image,
                    freeOngkir?.url.orEmpty(),
                    Modifier.align(Alignment.Top)
                )
            } else {
                ProductVisualWithStock(
                    productImage = catalogProduct.mediaUrl.image,
                    freeOngkir = freeOngkir?.url.orEmpty(),
                    stock = catalogProduct.stock.soldPercentage,
                    wordingStock = catalogProduct.stock.wording,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .width(90.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                ShopInfoRow(
                    shopName = catalogProduct.shop.name,
                    shopLocation = catalogProduct.shop.city,
                    badge = catalogProduct.shop.badge
                )
                Spacer(modifier = Modifier.height(6.dp))
                ProductPriceRow(catalogProduct.price.text, catalogProduct.price.original)
                Spacer(modifier = Modifier.height(4.dp))
                if (productBenefit?.title.orEmpty().isNotEmpty() || productOffer?.title.orEmpty().isNotEmpty()) {
                    ProductOfferAndBenefit(
                        productBenefit?.title.orEmpty(),
                        productOffer?.title.orEmpty()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                ShopCredibilityRow(
                    rating = catalogProduct.credibility.rating,
                    ratingCount = catalogProduct.credibility.ratingCount,
                    totalSold = totalSold?.title.orEmpty()
                )
                Spacer(modifier = Modifier.height(6.dp))
                ShippingInfo(
                    catalogProduct.delivery.eta,
                    catalogProduct.delivery.type,
                    catalogProduct.additionalService.name
                )
            }
            Box(
                Modifier
                    .height(44.dp)
                    .width(44.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 0.dp,
                            topStart = 10.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 10.dp
                        )
                    )
                    .border(
                        0.5.dp,
                        colorResource(id = R.color.catalog_dms_misty_blue),
                        RoundedCornerShape(
                            topEnd = 0.dp,
                            topStart = 10.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 10.dp
                        )
                    )
                    .clickable {
                        onClickAtc.invoke(catalogProduct)
                    }
                    .testTag("btnAddToCart").semantics {
                        this.testTagsAsResourceId = true
                    }
            ) {
                NestIcon(
                    IconUnify.CART,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(24.dp)
                        .width(24.dp),
                    colorLightEnable = colorResource(id = R.color.catalog_dms_light_color_text_common),
                    colorNightEnable = colorResource(id = R.color.catalog_dms_light_color_text_common)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProductVisualWithoutStock(
    productImage: String,
    freeOngkir: String,
    modifier: Modifier
) {
    Box {
        NestImage(
            source = ImageSource.Remote(productImage),
            type = NestImageType.Rect(0.dp),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(90.dp)
                .width(90.dp)
                .clip(
                    RoundedCornerShape(
                        12.dp
                    )
                )
                .testTag("imgProduct").semantics {
                    this.testTagsAsResourceId = true
                }
        )
        if (freeOngkir.isNotEmpty()) {
            NestImage(
                source = ImageSource.Remote(freeOngkir),
                type = NestImageType.Rect(0.dp),
                alignment = Alignment.BottomStart,
                modifier = Modifier
                    .height(25.dp)
                    .width(48.dp)
                    .align(Alignment.BottomStart)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 12.dp
                        )
                    ).testTag("lblFreeOngkir").semantics {
                        this.testTagsAsResourceId = true
                    }
            )
        }
    }
}

@Composable
private fun ProductVisualWithStock(
    productImage: String,
    freeOngkir: String,
    stock: Int,
    wordingStock: String,
    modifier: Modifier
) {
    val context = LocalContext.current
    NestCard(
        modifier = modifier,
        type = NestCardType.NoBorder
    ) {
        Column(
            Modifier
                .background(colorResource(id = R.color.catalog_dms_dark_color).copy(0.2f))
        ) {
            Box {
                NestImage(
                    source = ImageSource.Remote(productImage),
                    type = NestImageType.Rect(0.dp),
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        )
                        .testTag("imgProduct").semantics {
                            this.testTagsAsResourceId = true
                        }
                )
                if (freeOngkir.isNotEmpty()) {
                    NestImage(
                        source = ImageSource.Remote(freeOngkir),
                        type = NestImageType.Rect(0.dp),
                        alignment = Alignment.BottomStart,
                        modifier = Modifier
                            .height(25.dp)
                            .width(48.dp)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 12.dp
                                )
                            )
                            .align(Alignment.BottomStart)
                            .testTag("lblFreeOngkir").semantics {
                                this.testTagsAsResourceId = true
                            }
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.testTag("divStockBar").semantics {
                    this.testTagsAsResourceId = true
                }
            ) {
                NestTypography(
                    text = wordingStock,
                    textStyle = NestTheme.typography.small.copy(
                        color = colorResource(
                            id = R.color.catalog_dms_light_color_text_description
                        ),
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column(Modifier.align(Alignment.CenterVertically)) {
                    NestProgressBar(
                        value = stock,
                        isSmooth = true,
                        colorType = ProgressBarUnify.COLOR_RED,
                        icon = ContextCompat.getDrawable(
                            context,
                            catalogcommonR.drawable.catalog_ic_stockbar_progress_top
                        ),
                        height = ProgressBarUnify.SIZE_SMALL,
                        trackColor = ContextCompat.getColor(context, R.color.catalog_dms_misty_blue)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun ShopInfoRow(shopName: String, shopLocation: String, badge: String) {
    val maxCharShopName = 20
    val maxCharShopLocation = 15
    val shopNameTruncate = if (shopName.length > maxCharShopName) {
        shopName.substring(Int.ZERO, maxCharShopName) + "....."
    } else {
        shopName
    }

    val shopLocationTruncate = if (shopLocation.length > maxCharShopLocation) {
        shopLocation.substring(Int.ZERO, maxCharShopLocation) + "....."
    } else {
        shopLocation
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        NestImage(
            source = ImageSource.Remote(badge),
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        NestTypography(
            text = shopNameTruncate,
            textStyle = NestTheme.typography.display3.copy(
                color = colorResource(
                    id = R.color.catalog_dms_light_color_text_description
                ),
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            modifier = Modifier.testTag("txtShopName").semantics {
                this.testTagsAsResourceId = true
            }
        )
        Spacer(modifier = Modifier.width(4.dp))

        if (shopLocation.isNotEmpty()) {
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description
                        ),
                        CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            NestTypography(
                text = shopLocationTruncate,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_description
                    ),
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                modifier = Modifier.width(100.dp)
            )
        }
    }
}

@Composable
private fun ProductPriceRow(price: String, slashPrice: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        NestTypography(
            text = price,
            textStyle = NestTheme.typography.display2.copy(
                color = colorResource(
                    id = R.color.catalog_dms_light_color_text_common
                ),
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.testTag("txtOriginPrice").semantics {
                this.testTagsAsResourceId = true
            }
        )
        if (slashPrice.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            NestTypography(
                text = slashPrice,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_description
                    ),
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.LineThrough
                ),
                modifier = Modifier.testTag("txtDiscountPrice").semantics {
                    this.testTagsAsResourceId = true
                }
            )
        }
    }
}

@Composable
private fun ProductOfferAndBenefit(productBenefit: String, productOffer: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (productBenefit.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        colorResource(id = R.color.catalog_dms_pastel_pink),
                        RoundedCornerShape(5.dp)
                    )
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .background(colorResource(id = R.color.catalog_dms_whispering_white))
                    .testTag("divCouponShop").semantics {
                        this.testTagsAsResourceId = true
                    }

            ) {
                NestTypography(
                    text = productBenefit,
                    textStyle = NestTheme.typography.small.copy(
                        color = colorResource(
                            id = R.color.catalog_dms_coral_red
                        ),
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                )
            }
        }
        if (productBenefit.isNotEmpty() && productOffer.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (productOffer.isNotEmpty()) {
            NestTypography(
                text = productOffer,
                textStyle = NestTheme.typography.small.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_golden_rod
                    ),
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.testTag("divBMGM").semantics {
                    this.testTagsAsResourceId = true
                }
            )
        }
    }
}

@Composable
private fun ShopCredibilityRow(rating: String, ratingCount: String, totalSold: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (rating.isNotEmpty()) {
            NestIcon(
                IconUnify.STAR_FILLED,
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp),
                colorLightEnable = colorResource(id = R.color.catalog_dms_sunshine_yellow),
                colorNightEnable = colorResource(id = R.color.catalog_dms_sunshine_yellow)
            )
            Spacer(modifier = Modifier.width(2.dp))
            NestTypography(
                text = rating,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common
                    ),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.testTag("txtRatingProduct").semantics {
                    this.testTagsAsResourceId = true
                }
            )
            if (ratingCount.isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                NestTypography(
                    text = "($ratingCount)",
                    textStyle = NestTheme.typography.display3.copy(
                        color = colorResource(
                            id = R.color.catalog_dms_light_color_text_description
                        ),
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        if (rating.isNotEmpty() && totalSold.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description
                        ),
                        CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        if (totalSold.isNotEmpty()) {
            val totalSoldSplitWord = totalSold.split(" ")
            NestTypography(
                text = totalSoldSplitWord.getOrNull(Int.ZERO).orEmpty(),
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common
                    ),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.testTag("txtSoldQty").semantics {
                    this.testTagsAsResourceId = true
                }
            )
            Spacer(modifier = Modifier.width(2.dp))
            NestTypography(
                text = totalSoldSplitWord.getOrNull(Int.ONE).orEmpty(),
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_description
                    ),
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.testTag("labelSoldQty").semantics {
                    this.testTagsAsResourceId = true
                }
            )
        }
    }
}

@Composable
private fun ShippingInfo(eta: String, courierType: String, additionalService: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (eta.isNotEmpty()) {
            NestTypography(
                text = eta,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common
                    ),
                    fontWeight = FontWeight.Normal
                )
            )
        }
        if (eta.isNotEmpty() && (additionalService.isNotEmpty() || courierType.isNotEmpty())) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description
                        ),
                        CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (additionalService.isNotEmpty()) {
            NestTypography(
                text = additionalService,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common
                    ),
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1
            )
        }
        if (eta.isNotEmpty() && additionalService.isNotEmpty() && courierType.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description
                        ),
                        CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (courierType.isNotEmpty()) {
            NestTypography(
                text = courierType,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common
                    ),
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1
            )
        }
    }
}
