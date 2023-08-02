package com.tokopedia.centralizedpromo.compose

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.R.drawable
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.LayoutType.PROMO_CREATION
import com.tokopedia.centralizedpromo.view.bottomSheet.DetailPromoBottomSheet
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoComposeViewModel
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.sortfilter.compose.NestSortFilter
import com.tokopedia.sortfilter.compose.SortFilter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CentralizedPromoScreen(viewModel: CentralizedPromoComposeViewModel) {

    val uiState = viewModel.layoutList.collectAsState().value
    val isRefreshing = viewModel.isSwipeRefresh.collectAsState().value

    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        viewModel.sendEvent(CentralizedPromoEvent.SwipeRefresh)
    })
    val context = LocalContext.current

    Scaffold(
        topBar = {
            NestHeader(
                type = NestHeaderType.SingleLine().copy(
                    title = "Iklan dan Promosi"
                )
            )
        }) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(it)
                .pullRefresh(pullRefreshState)
        ) {

            BackgroundDrawable()

            LazyVerticalGrid(
                columns = Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {

                HeaderSection(
                    uiState.onGoingData as? Result<OnGoingPromoListUiModel>,
                    uiState.isLoadingHeader()
                ) {
                    RouteManager.route(
                        context, it
                    )
                }

                BodySection(
                    uiState.promoCreationData as? Result<PromoCreationListUiModel>,
                    uiState.selectedTabId(),
                    uiState.isLoadingBody(),
                    onFilterClicked = { tabData ->
                        viewModel.sendEvent(
                            CentralizedPromoEvent.FilterUpdate(
                                selectedTabFilterData = tabData.first to tabData.second
                            )
                        )
                    },
                    onPromoClicked = { promoCreationUiModel ->
                        val showRbac = !viewModel.getKeyRBAC(promoCreationUiModel.title)
                        onPromoClicked(
                            context,
                            uiState.selectedTabName(),
                            promoCreationUiModel,
                            showRbac
                        ) {
                            viewModel.sendEvent(CentralizedPromoEvent.UpdateRbacBottomSheet(it))
                        }
                    }
                )
            }

            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

private fun onPromoClicked(
    context: Context,
    selectedTabName: String,
    promoCreationUiModel: PromoCreationUiModel,
    isIgnoreBottomSheet: Boolean,
    ignoreBottomSheet: (String) -> Unit
) {
    if (promoCreationUiModel.isEligible()) {
        if (!isIgnoreBottomSheet) {
            RouteManager.route(
                context, promoCreationUiModel.ctaLink
            )
        } else {
            val detailPromoBottomSheet =
                DetailPromoBottomSheet.createInstance(promoCreationUiModel)
            detailPromoBottomSheet.show((context as FragmentActivity).supportFragmentManager)
            detailPromoBottomSheet.onCheckBoxListener { _ ->
                ignoreBottomSheet.invoke(promoCreationUiModel.title)
                CentralizedPromoTracking.sendClickCheckboxBottomSheet(
                    selectedTabName,
                    promoCreationUiModel.title
                )
            }
            detailPromoBottomSheet.onCreateCampaignTracking {
                CentralizedPromoTracking.sendClickCreateCampaign(
                    selectedTabName,
                    promoCreationUiModel.title
                )
            }

            detailPromoBottomSheet.onClickPaywallTracking {
                CentralizedPromoTracking.sendClickPaywall(
                    selectedTabName,
                    promoCreationUiModel.title,
                    promoCreationUiModel.ctaText
                )
            }


            detailPromoBottomSheet.onImpressionPaywallTracking {
                CentralizedPromoTracking.sendImpressionBottomSheetPaywall(
                    selectedTabName,
                    promoCreationUiModel.title,
                    promoCreationUiModel.ctaText
                )
            }

            detailPromoBottomSheet.onClickPerformanceListener {
                context.let {
                    RouteManager.route(it, getPlayPerformanceApplink())
                }
            }

            CentralizedPromoTracking.sendImpressionBottomSheetPromo(
                selectedTabName,
                promoCreationUiModel.title
            )
        }

    } else {
        RouteManager.route(context, ApplinkConstInternalSellerapp.ADMIN_RESTRICTION)
    }

    CentralizedPromoTracking.sendClickCampaignCard(
        selectedTabName,
        promoCreationUiModel.title
    )
}

private fun getPlayPerformanceApplink(): String {
    return String.format(
        Locale.getDefault(),
        CentralizedPromoFragment.WEBVIEW_APPLINK_FORMAT,
        ApplinkConst.WEBVIEW,
        CentralizedPromoFragment.PLAY_PERFORMANCE_URL
    )
}


private fun LazyGridScope.BodySection(
    promoCreationData: Result<PromoCreationListUiModel>?,
    selectedTabId: String,
    isLoadingPromoList: Boolean,
    onFilterClicked: (Pair<String, String>) -> Unit,
    onPromoClicked: (PromoCreationUiModel) -> Unit
) {
    TitleSection("Buat Promosi")

    if (promoCreationData is Success && promoCreationData.data.filterItems.isNotEmpty()) {
        FilterSection(promoCreationData.data.filterItems, selectedTabId) {
            onFilterClicked.invoke(it)
        }
    }

    if (isLoadingPromoList) {
        PromotionCardShimmerGrid(modifier = Modifier.padding(top = 8.dp))
    } else if (promoCreationData != null) {
        when (promoCreationData) {
            is Success -> {
                PromoCreationSection(promoCreationData.data.items, onPromoClicked)
            }
            is Fail -> {

            }
        }
    }
}

private fun LazyGridScope.FilterSection(
    filterItems: List<FilterPromoUiModel>,
    selectedTabId: String,
    onFilterClicked: (Pair<String, String>) -> Unit
) = item(
    span = { GridItemSpan(2) }
) {
    val result = remember(filterItems, selectedTabId) {
        filterItems.map {
            SortFilter(
                title = it.name,
                isSelected = it.id == selectedTabId,
                showChevron = false
            ) {
                onFilterClicked.invoke(it.id to it.name)
            }
        }
    }

    NestSortFilter(
        items = result,
        showClearFilterIcon = false
    )
}

private fun LazyGridScope.HeaderSection(
    onGoingResult: Result<OnGoingPromoListUiModel>?,
    isOnGoingLoading: Boolean,
    routeToUrl: (String) -> Unit
) {

    TitleSection("Fitur promosi aktifmu")
    if (isOnGoingLoading) {
        OnGoingCardShimmerRow()
    } else if (onGoingResult != null) {
        when (onGoingResult) {
            is Success -> {
                OnGoingPromoSection(
                    result = onGoingResult,
                    routeToUrl = routeToUrl
                )
            }
            is Fail -> {

            }
        }
    }
}

private fun LazyGridScope.TitleSection(
    title: String,
    modifier: Modifier = Modifier
) = item(span = { GridItemSpan(2) }) {
    PromoSectionTitle(
        text = title,
        modifier = modifier.padding(vertical = 16.dp)
    )
}

private fun LazyGridScope.PromoCreationSection(
    list: List<PromoCreationUiModel>,
    onPromoClicked: (PromoCreationUiModel) -> Unit
) = items(
    items = list,
    key = { it.pageId },
    contentType = { PROMO_CREATION }) { data ->
    PromotionCard(
        title = data.title,
        labelNew = data.titleSuffix,
        description = data.description,
        imageUrl = data.icon,
        modifier = Modifier.padding(top = 12.dp),
        notAvailableText = data.notAvailableText,
        onPromoClicked = {
            onPromoClicked.invoke(data)
        }
    )
}

private fun LazyGridScope.OnGoingPromoSection(
    result: Result<OnGoingPromoListUiModel>,
    routeToUrl: (String) -> Unit
) = item(span = { GridItemSpan(2) }) {
    when (result) {
        is Success -> {
            LazyRow {
                this@LazyRow.items(result.data.items) { onGoingData ->
                    OnGoingCard(
                        title = onGoingData.title,
                        counter = onGoingData.status.count,
                        counterTitle = onGoingData.status.text,
                        onTitleClicked = {
                            routeToUrl.invoke(onGoingData.status.url)
                        },
                        onFooterClicked = {
                            routeToUrl.invoke(onGoingData.status.url)
                        }
                    )
                }
            }
        }
        is Fail -> {

        }
    }
}

@Composable
private fun BackgroundDrawable() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(drawable.bg_bottom_circle), contentDescription = null
        )
    }
}

@Preview(name = "NEXUS_7", device = Devices.NEXUS_5)
@Composable
private fun CentralizedPromoScreenPreview() {
//    CentralizedPromoScreen()
}