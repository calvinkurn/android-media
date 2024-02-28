package com.tokopedia.catalog.ui.activity.sellerOfferingList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.sortfilter.compose.NestSortFilterAdvanced
import com.tokopedia.sortfilter.compose.Size
import com.tokopedia.sortfilter.compose.SortFilter
import kotlinx.coroutines.delay

@Composable
fun CatalogSellerOfferingScreen(
    productTitle: String,
    productVariant: String,
    background: Int,
    totalItemCart: Int,
    sortFilter: List<SortFilter> = mutableListOf(),
    productList: MutableList<CatalogProductListResponse.CatalogGetProductList.CatalogProduct>,
    countFilter: Int,
    listener: ChooseAddressWidget.ChooseAddressWidgetListener? = null,
    lcaListener: (ChooseAddressWidget) -> Unit = {},
    onClickVariant: () -> Unit,
    onToolbarBackIconPressed: () -> Unit,
    onClickActionButtonCart: () -> Unit,
    onClickActionButtonMenu: () -> Unit,
    onClickMoreFilter: () -> Unit,
    onLoadMore: () -> Unit,
    onClickAtc: (CatalogProductListResponse.CatalogGetProductList.CatalogProduct, Int) -> Unit,
    onClickItemProduct: (CatalogProductListResponse.CatalogGetProductList.CatalogProduct, Int) -> Unit
) {
    val listState = rememberLazyListState()

    val reachedBottom: Boolean by remember {
        derivedStateOf {
            listState.reachedBottom()
        }
    }

    LaunchedEffect(reachedBottom) {
        delay(700)
        if (reachedBottom) {
            onLoadMore.invoke()
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
                        Modifier.background(NestNN.light._0.copy(alpha = 0.2f)),
                        thickness = 1.dp
                    )
                }
                CatalogSellerOfferingBody(
                    listState, listener, background, lcaListener, sortFilter.toMutableList(),
                    onClickMoreFilter, onClickAtc, onClickItemProduct, productList, countFilter
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatalogSellerOfferingBody(
    listState: LazyListState,
    listener: ChooseAddressWidget.ChooseAddressWidgetListener?,
    background: Int,
    lcaListener: (ChooseAddressWidget) -> Unit,
    sortFilter: MutableList<SortFilter>,
    onClickMoreFilter: () -> Unit,
    onClickAtc: (CatalogProductListResponse.CatalogGetProductList.CatalogProduct, Int) -> Unit,
    onClickItemProduct: (CatalogProductListResponse.CatalogGetProductList.CatalogProduct, Int) -> Unit,
    productList: MutableList<CatalogProductListResponse.CatalogGetProductList.CatalogProduct>,
    countFilter: Int
) {
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
                Filter(sortFilter, Size.DEFAULT, onPrefixClicked = onClickMoreFilter, countFilter = countFilter)
            }
        }

        items(productList.size) { index ->
            ItemProduct(onClickItem = {
                onClickItemProduct.invoke(it, index)
            }, onClickAtc = {
                    onClickAtc.invoke(it, index)
                }, productList[index])
            if (index == productList.size - 1) {
                Box(Modifier.fillMaxWidth()) {
                    NestLoader(
                        variant = NestLoaderType.Decorative(size = NestLoaderSize.Large),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    })
}

@Composable
fun Filter(
    advItems: List<SortFilter>,
    size: Size,
    onPrefixClicked: () -> Unit,
    countFilter: Int
) {
    NestSortFilterAdvanced(
        items = advItems,
        size = size,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
        onPrefixClicked = onPrefixClicked,
        countSelected = countFilter,
        onItemClicked = {}
    )
}

private fun LazyListState.reachedBottom(): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - 1
}
