package com.tokopedia.catalog.ui.activity.sellerOfferingList

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.catalog.R
import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.catalog.util.ColorConst.COLOR_WHITE
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.sortfilter.compose.NestSortFilterAdvanced
import com.tokopedia.sortfilter.compose.Size
import com.tokopedia.sortfilter.compose.SortFilter



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatalogSellerOfferingScreen(
    productTitle: String,
    productVariant: String,
    background: Int,
    totalItemCart: Int,
    sortFilter: List<SortFilter> = mutableListOf(),
    productList: MutableList<CatalogProductListResponse.CatalogGetProductList.CatalogProduct>,
    listener: ChooseAddressWidget.ChooseAddressWidgetListener? = null,
    lcaListener: (ChooseAddressWidget) -> Unit = {},
    onClickVariant: () -> Unit,
    onToolbarBackIconPressed: () -> Unit,
    onClickActionButtonCart: () -> Unit,
    onClickActionButtonMenu: () -> Unit,
    onClickMoreFilter: () -> Unit,
    onClickFilter: (SortFilter) -> Unit,
    onLoadMore: (Int) -> Unit,
    onClickAtc: () -> Unit
) {

    Log.d("TEESS",productList.size.toString())
    var filterItems by remember {
        mutableStateOf(mutableListOf<SortFilter>())
    }
    filterItems = sortFilter.toMutableList()

    var page = 0

    val listState = rememberLazyListState()

    val reachedBottom: Boolean by remember {
        derivedStateOf {
            listState.reachedBottom()
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            page+=1
            onLoadMore.invoke(page)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(background))
            )
            Column {
                Column(Modifier.background(Color.Transparent)) {
                    CatalogSellerOfferingToolbar(
                        productTitle,
                        productVariant,
                        totalItemCart,
                        onToolbarBackIconPressed = onToolbarBackIconPressed,
                        onClickVariant = onClickVariant,
                        onClickActionButtonCart = onClickActionButtonCart,
                        onClickActionButtonMenu = onClickActionButtonMenu
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(
                        Modifier.background(NestNN.light._0.copy(alpha = 0.2f)), thickness = 1.dp
                    )
                }
                LazyColumn(state = listState, modifier = Modifier.fillMaxWidth(), content = {
                    item {
                        Box(Modifier.background(Color.Transparent)) {
                            CatalogSellerOfferingHeader(
                                listener = listener,
                                background,
                                lcaListener
                            )
                        }
                    }
                    stickyHeader {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {
                            Filter(filterItems, Size.DEFAULT, onClick = { sortFilterSelected ->
                                onClickFilter.invoke(sortFilterSelected)
                                filterItems = filterItems.map {
                                    if (it == sortFilterSelected) {
                                        it.copy(isSelected = it.isSelected.not())
                                    } else {
                                        it
                                    }
                                }.toMutableList()
                            }, onPrefixClicked = onClickMoreFilter)
                        }
                    }

                    items(productList.size) { index ->
                        ItemProduct(onClickAtc,productList[index])
                    }
                })
            }
        }

    }

}


@Composable
fun Filter(
    advItems: List<SortFilter>,
    size: Size,
    onClick: (SortFilter) -> Unit,
    onPrefixClicked: () -> Unit
) {
    NestSortFilterAdvanced(
        items = advItems,
        size = size,
        onItemClicked = { sf ->
            onClick.invoke(sf)

        },
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        onPrefixClicked = onPrefixClicked
    )
}


@Composable
fun ItemProduct(
    onClickAtc: () -> Unit,
    catalogProduct: CatalogProductListResponse.CatalogGetProductList.CatalogProduct
) {
    val productBenefit = catalogProduct.labelGroups.find {
        it.position == "ri_product_benefit"
    }

    val productOffer = catalogProduct.labelGroups.find {
        it.position == "ri_product_offer"
    }

    val totalSold = catalogProduct.labelGroups.find {
        it.position == "ri_product_credibility"
    }
    Column {
        Row(Modifier.background(colorResource(id = R.color.catalog_dms_light_color)), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            NestImage(
                source = ImageSource.Remote(catalogProduct.mediaUrl.image),
                modifier = Modifier
                    .height(90.dp)
                    .width(90.dp)
                    .align(Alignment.Top)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier=Modifier.weight(1f)) {
                ShopInfoRow(shopName = catalogProduct.shop.name, shopLocation = catalogProduct.shop.city,
                    badge = catalogProduct.shop.badge )
                Spacer(modifier = Modifier.height(6.dp))
                ProductPriceRow(catalogProduct.price.text, catalogProduct.price.original)
                Spacer(modifier = Modifier.height(4.dp))
                ProductOfferAndBenefit(productBenefit?.title.orEmpty(), productOffer?.title.orEmpty())
                Spacer(modifier = Modifier.height(6.dp))
                ShopCredibilityRow(
                    rating = catalogProduct.credibility.rating,
                    ratingCount = catalogProduct.credibility.ratingCount,
                    totalSold = totalSold?.title.orEmpty()
                )
                Spacer(modifier = Modifier.height(6.dp))
                ShippingInfo(catalogProduct.delivery.eta,
                    catalogProduct.delivery.type,
                    catalogProduct.additionalService.name)
            }
            Box(
                Modifier
                    .height(44.dp)
                    .width(44.dp)
                    .border(
                        0.5.dp, colorResource(id = R.color.catalog_dms_misty_blue),
                        RoundedCornerShape(
                            topEnd = 0.dp,
                            topStart = 10.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 10.dp
                        )
                    )
                    .padding(10.dp)
                    .clickable {
                        onClickAtc.invoke()
                    }
            ) {
                NestIcon(
                    IconUnify.CART,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(16.dp)
                        .width(16.dp),
                    colorLightEnable = colorResource(id = R.color.catalog_dms_light_color_text_common),
                    colorNightEnable = colorResource(id = R.color.catalog_dms_light_color_text_common)
                )
            }

        }
        Spacer(Modifier.height(16.dp))
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp))
        Spacer(Modifier.height(12.dp))
    }
}


@Composable
private fun ShopInfoRow(shopName:String, shopLocation:String, badge:String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        NestImage(
            source = ImageSource.Remote(badge),
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        NestTypography(
            text = shopName,
            textStyle = NestTheme.typography.display3.copy(
                color = colorResource(
                    id = R.color.catalog_dms_light_color_text_description,
                ),
                fontWeight = FontWeight.Normal
            ),
        )
        Spacer(modifier = Modifier.width(4.dp))

        if (shopLocation.isNotEmpty()){
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description,
                        ), CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            NestTypography(
                text = shopLocation,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_description,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
        }
    }
}

@Composable
private fun ProductPriceRow(price:String, slashPrice:String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        NestTypography(
            text = price,
            textStyle = NestTheme.typography.display2.copy(
                color = colorResource(
                    id = R.color.catalog_dms_light_color_text_common,
                ),
                fontWeight = FontWeight.ExtraBold
            ),
        )
        if (slashPrice.isNotEmpty()){
            Spacer(modifier = Modifier.width(4.dp))
            NestTypography(
                text = slashPrice,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_description,
                    ),
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.LineThrough
                ),
            )
        }

    }
}

@Composable
private fun ProductOfferAndBenefit(productBenefit:String, productOffer:String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (productBenefit.isNotEmpty()){
            Box(
                modifier = Modifier
                    .border(
                        2.dp, colorResource(id = R.color.catalog_dms_pastel_pink),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 4.dp)
                    .background(colorResource(id = R.color.catalog_dms_whispering_white))
            ) {
                NestTypography(
                    text = productBenefit,
                    textStyle = NestTheme.typography.small.copy(
                        color = colorResource(
                            id = R.color.catalog_dms_coral_red
                        ),
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
            }
        }
        if (productBenefit.isNotEmpty() && productOffer.isNotEmpty()){
            Spacer(modifier = Modifier.width(4.dp))

        }
        if (productOffer.isNotEmpty()){
            NestTypography(
                text = productOffer,
                textStyle = NestTheme.typography.small.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_golden_rod
                    ),
                    fontWeight = FontWeight.ExtraBold
                ),
            )
        }
    }
}

@Composable
private fun ShopCredibilityRow(rating:String, ratingCount:String, totalSold:String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (rating.isNotEmpty()){
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
                        id = R.color.catalog_dms_light_color_text_common,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
            if (ratingCount.isNotEmpty()){
                Spacer(modifier = Modifier.width(4.dp))
                NestTypography(
                    text = "($ratingCount)",
                    textStyle = NestTheme.typography.display3.copy(
                        color = colorResource(
                            id = R.color.catalog_dms_light_color_text_description,
                        ),
                        fontWeight = FontWeight.Normal
                    ),
                )
            }

        }
        if (rating.isNotEmpty() && totalSold.isNotEmpty()){
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description,
                        ), CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        if (totalSold.isNotEmpty()){
            val totalSoldSplitWord = totalSold.split(" ")
            NestTypography(
                text = totalSoldSplitWord.getOrNull(Int.ZERO).orEmpty(),
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
            Spacer(modifier = Modifier.width(2.dp))
            NestTypography(
                text = totalSoldSplitWord.getOrNull(Int.ONE).orEmpty(),
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_description,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
        }

    }
}

@Composable
private fun ShippingInfo(eta:String, courierType:String, additionalService:String){
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (eta.isNotEmpty()){
            NestTypography(
                text = eta,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
        }
        if (eta.isNotEmpty() && (additionalService.isNotEmpty() || courierType.isNotEmpty())){
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description,
                        ), CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (additionalService.isNotEmpty()){
            NestTypography(
                text = additionalService,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
        }
        if (eta.isNotEmpty() && additionalService.isNotEmpty() && courierType.isNotEmpty()){
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier
                    .background(
                        colorResource(
                            id = R.color.catalog_dms_light_color_text_description,
                        ), CircleShape
                    )
                    .width(2.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (courierType.isNotEmpty()){
            NestTypography(
                text = courierType,
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(
                        id = R.color.catalog_dms_light_color_text_common,
                    ),
                    fontWeight = FontWeight.Normal
                ),
            )
        }

    }

}

private fun LazyListState.reachedBottom(): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - 1
}




