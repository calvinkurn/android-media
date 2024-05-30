package com.tokopedia.catalog.ui.composeUi.screen

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.composeUi.component.CatalogSellerOfferingHeader
import com.tokopedia.catalog.ui.composeUi.component.CatalogSellerOfferingToolbar
import com.tokopedia.catalog.ui.composeUi.component.ItemProduct
import com.tokopedia.catalog.ui.model.CatalogFilterProductListState
import com.tokopedia.catalog.ui.model.CatalogProductListState
import com.tokopedia.catalog.ui.model.CatalogProductListUiModel
import com.tokopedia.globalerror.compose.NestGlobalError
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.sortfilter.compose.NestSortFilterAdvanced
import com.tokopedia.sortfilter.compose.Size
import kotlinx.coroutines.delay
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CatalogSellerOfferingScreen(
    productTitle: String,
    productVariant: String,
    background: Int,
    totalItemCart: Int,
    sortFilter: MutableState<CatalogFilterProductListState>,
    productListState: MutableState<CatalogProductListState>,
    countFilter: Int,
    throwable: Throwable?,
    lcaListener: (ChooseAddressWidget) -> Unit,
    onClickVariant: () -> Unit,
    onToolbarBackIconPressed: () -> Unit,
    onClickActionButtonCart: () -> Unit,
    onClickActionButtonMenu: () -> Unit,
    onClickMoreFilter: () -> Unit,
    onLoadMore: () -> Unit,
    onErrorRefresh: () -> Unit,
    onClickAtc: (CatalogProductListUiModel.CatalogProductUiModel, Int) -> Unit,
    onClickItemProduct: (CatalogProductListUiModel.CatalogProductUiModel, Int) -> Unit,
    hasNextPage: MutableState<Boolean>,
    onImpressionProduct: (CatalogProductListUiModel.CatalogProductUiModel, Int) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    clickFilter: Boolean = false,
    resetFilter: () -> Unit
) {
    val listState = rememberLazyListState()

    val reachedBottom: Boolean by remember {
        derivedStateOf {
            listState.reachedBottom()
        }
    }

    val rememberedLcaListener = remember { lcaListener }

    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    LaunchedEffect(reachedBottom) {
        delay(700)
        if (reachedBottom && hasNextPage.value) {
            onLoadMore.invoke()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {
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
                    listState, background, rememberedLcaListener, sortFilter,
                    onClickMoreFilter, onClickAtc, onClickItemProduct,
                    productListState, countFilter, hasNextPage, throwable, onErrorRefresh,
                    onImpressionProduct, clickFilter, resetFilter, isRefreshing, pullRefreshState
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun CatalogSellerOfferingBody(
    listState: LazyListState,
    background: Int,
    lcaListener: (ChooseAddressWidget) -> Unit,
    sortFilterState: MutableState<CatalogFilterProductListState>,
    onClickMoreFilter: () -> Unit,
    onClickAtc: (CatalogProductListUiModel.CatalogProductUiModel, Int) -> Unit,
    onClickItemProduct: (CatalogProductListUiModel.CatalogProductUiModel, Int) -> Unit,
    productListState: MutableState<CatalogProductListState>,
    countFilter: Int,
    hasNextPage: MutableState<Boolean>,
    throwable: Throwable?,
    onErrorRefresh: () -> Unit,
    onImpressionProduct: (CatalogProductListUiModel.CatalogProductUiModel, Int) -> Unit,
    filterClick: Boolean,
    resetFilter: () -> Unit,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState
) {
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), content = {
        item {
            Box(Modifier.background(Color.Transparent)) {
                CatalogSellerOfferingHeader(
                    background,
                    lcaListener
                )
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
        if (throwable != null) {
            item {
                val errorType = when (throwable) {
                    is SocketTimeoutException, is UnknownHostException, is ConnectException -> NestGlobalErrorType.NoConnection
                    else -> NestGlobalErrorType.ServerError
                }
                val currentContext = LocalContext.current
                Spacer(modifier = Modifier.height(48.dp))
                NestGlobalError(
                    type = errorType,
                    onClickAction = {
                        // TODO
                        onErrorRefresh.invoke()
                    },
                    onClickSecondaryAction =
                    if (errorType == NestGlobalErrorType.NoConnection) {
                        {
                            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            currentContext.startActivity(intent)
                        }
                    } else {
                        null
                    },
                    secondaryActionText = "Ke Pengaturan"
                )
            }
        } else {
            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    if (sortFilterState.value is CatalogFilterProductListState.Loading) {
                        FilterLoadingState()
                    } else {
                        Filter(
                            sortFilterState,
                            Size.DEFAULT,
                            onPrefixClicked = onClickMoreFilter,
                            countFilter = countFilter
                        )
                    }
                }
            }

            if (productListState.value is CatalogProductListState.Loading) {
                items(5) {
                    ProductListLoadingState()
                }
            } else {
                val productList = productListState.value.data.orEmpty()
                if (productList.isEmpty()) {
                    item {
                        EmptyState(isFilter = filterClick, resetFilter)
                    }
                } else {
                    items(productList.size) { index ->

                        onImpressionProduct.invoke(productList[index], index)

                        ItemProduct(onClickItem = {
                            onClickItemProduct.invoke(it, index)
                        }, onClickAtc = {
                                onClickAtc.invoke(it, index)
                            }, productListState, index)
                        if (index != productList.size - 1) {
                            Divider(
                                Modifier
                                    .fillMaxWidth(),
                                color = NestNN.light._50
                            )
                        }
                        if (index == productList.size - 1 && hasNextPage.value) {
                            Box(Modifier.fillMaxWidth()) {
                                NestLoader(
                                    variant = NestLoaderType.Decorative(size = NestLoaderSize.Large),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    })
}

@Composable
fun Filter(
    advItems: MutableState<CatalogFilterProductListState>,
    size: Size,
    onPrefixClicked: () -> Unit,
    countFilter: Int
) {
    NestSortFilterAdvanced(
        items = advItems.value.data.orEmpty(),
        size = size,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
        onPrefixClicked = onPrefixClicked,
        countSelected = countFilter,
        onItemClicked = {}
    )
}

@Composable
@Preview
fun FilterLoadingState() {
    Row(Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
        repeat(4) {
            NestLoader(
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                modifier = Modifier
                    .height(35.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun ProductListLoadingState() {
    Row(Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
            modifier = Modifier
                .size(80.dp, 80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(68.dp, 12.dp)
                )
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
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(78.dp, 12.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            NestLoader(
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                modifier = Modifier
                    .size(165.dp, 12.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            NestLoader(
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                modifier = Modifier
                    .size(165.dp, 12.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(68.dp, 12.dp)
                )
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
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(78.dp, 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(68.dp, 12.dp)
                )
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
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(78.dp, 12.dp)
                )
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
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(16.dp)),
                    modifier = Modifier
                        .size(78.dp, 12.dp)
                )
            }
        }
    }
    Divider(Modifier.height(1.dp))
}

@Composable
private fun EmptyState(isFilter: Boolean, resetFilter: () -> Unit) {
    val wordingTitle = if (isFilter) {
        stringResource(id = R.string.text_empty_product_list)
    } else {
        stringResource(id = R.string.catalog_no_products_title)
    }

    val wordingDescription = if (isFilter) {
        stringResource(id = R.string.text_empty_state_desc)
    } else {
        stringResource(id = R.string.catalog_no_products_body)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        NestImage(
            source = ImageSource.Remote(TokopediaImageUrl.ILLUSTRATION_EMPTY_CATALOG_PRODUCT_LIST),
            type = NestImageType.Rect(0.dp),
            modifier = Modifier
                .height(220.dp)
                .width(180.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        NestTypography(
            text = wordingTitle,
            textStyle = NestTheme.typography.heading4.copy(
                color = colorResource(
                    id = R.color.catalog_dms_column_info_value_color_light
                ),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.width(258.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        NestTypography(
            text = wordingDescription,
            textStyle = NestTheme.typography.display2.copy(
                color = colorResource(
                    id = R.color.catalog_dms_column_info_value_color_light
                ),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.width(328.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (isFilter) {
            NestButton(
                text = stringResource(id = R.string.text_reset_filter),
                onClick = { resetFilter.invoke() },
                variant = ButtonVariant.FILLED,
                size = ButtonSize.MEDIUM,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun LazyListState.reachedBottom(): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - 1
}
