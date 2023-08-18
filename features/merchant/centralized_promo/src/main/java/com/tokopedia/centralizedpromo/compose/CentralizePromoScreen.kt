package com.tokopedia.centralizedpromo.compose

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.R.drawable
import com.tokopedia.centralizedpromo.R.string
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.LayoutType.PROMO_CREATION
import com.tokopedia.centralizedpromo.view.LoadingType
import com.tokopedia.centralizedpromo.view.LoadingType.ALL
import com.tokopedia.centralizedpromo.view.bottomSheet.DetailPromoBottomSheet
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoUiState
import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.centralizedpromo.view.model.Status
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.components.CoachMarkAnchor
import com.tokopedia.nest.components.coachmarkableOn
import com.tokopedia.nest.principles.utils.addImpression
import com.tokopedia.sortfilter.compose.NestSortFilter
import com.tokopedia.sortfilter.compose.SortFilter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*

/**
 * enableCoachmark used to disable globalLayoutListener if we don't want to show toaster (reducing recomposition)
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CentralizedPromoScreen(
    uiState: CentralizedPromoUiState,
    onEvent: (CentralizedPromoEvent) -> Unit = {},
    checkRbac: (String) -> Boolean = { false },
    enableCoachMark: Boolean = false,
    coachMarkAnchor: (CoachMarkAnchor, String) -> Unit = { _, _ -> },
    onBackPressed: () -> Unit = {}
) {

    val pullRefreshState = rememberPullRefreshState(uiState.isSwipeRefresh, {
        onEvent.invoke(CentralizedPromoEvent.SwipeRefresh)
    })
    val context = LocalContext.current

    Scaffold(
        topBar = {
            NestHeader(
                type = NestHeaderType.SingleLine().copy(
                    title = stringResource(string.centralized_promo_toolbar_title),
                    onBackClicked = onBackPressed
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

            val rememberLazyGridState = rememberLazyGridState()
            val rememberLazyListState = rememberLazyListState()

            CompositionLocalProvider(
                LocalGridStateComposition provides rememberLazyGridState,
                LocalListStateComposition provides rememberLazyListState
            ) {
                LazyVerticalGrid(
                    columns = Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    state = rememberLazyGridState,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {

                    HeaderSection(
                        uiState.onGoingData,
                        uiState.isLoadingHeader(),
                        onGoingPromoImpressed = {
                            impressOnGoingPromo(it)
                        },
                        routeToUrl = {
                            RouteManager.route(
                                context, it
                            )
                        },
                        onLocalLoadRefreshClicked = {
                            if (!uiState.isSwipeRefresh) {
                                onEvent.invoke(CentralizedPromoEvent.SwipeRefresh)
                            }
                        }
                    )

                    BodySection(
                        promoCreationData = uiState.promoCreationData,
                        selectedTabId = uiState.selectedTabId(),
                        isLoadingPromoList = uiState.isLoadingBody(),
                        onFilterClicked = { tabData ->
                            CentralizedPromoTracking.sendClickFilter(tabData.first)
                            onEvent.invoke(
                                CentralizedPromoEvent.FilterUpdate(
                                    selectedTabFilterData = tabData.first to tabData.second
                                )
                            )
                        },
                        onPromoClicked = { promoCreationUiModel ->
                            onPromoClicked(
                                context,
                                uiState.selectedTabName(),
                                promoCreationUiModel,
                                !checkRbac.invoke(promoCreationUiModel.title)
                            ) {
                                onEvent.invoke(CentralizedPromoEvent.UpdateRbacBottomSheet(it))
                            }
                        },
                        promoCreationImpressed = {
                            impressPromoCreation(it, uiState.selectedTabName())
                        },
                        onLocalLoadRefreshClicked = {
                            if (!uiState.isSwipeRefresh) {
                                onEvent.invoke(CentralizedPromoEvent.SwipeRefresh)
                            }
                        },
                        coachMarkListener = { pageId ->
                            //disable coachmarlableOn when we don't have to show coachmark anymore
                            if (enableCoachMark) {
                                Modifier.coachmarkableOn(
                                    pageId == PromoCreationUiModel.PAGE_ID_SHOP_COUPON
                                ) {
                                    coachMarkAnchor.invoke(it, pageId)
                                }
                            } else {
                                Modifier
                            }
                        }
                    )
                }

                PullRefreshIndicator(
                    uiState.isSwipeRefresh,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

private fun impressOnGoingPromo(title: String) {
    CentralizedPromoTracking.sendImpressionOnGoingPromoStatus(
        widgetName = title
    )
}

private fun impressPromoCreation(title: String, currentFilterName: String) {
    CentralizedPromoTracking.sendImpressionCard(title, currentFilterName)
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
    promoCreationData: Result<BaseUiModel>?,
    selectedTabId: String,
    isLoadingPromoList: Boolean,
    onFilterClicked: (Pair<String, String>) -> Unit,
    onPromoClicked: (PromoCreationUiModel) -> Unit,
    promoCreationImpressed: (String) -> Unit,
    onLocalLoadRefreshClicked: () -> Unit,
    coachMarkListener: (String) -> Modifier
) {
    val promoCreationDataCast = (promoCreationData as? Success)?.data as? PromoCreationListUiModel

    TitleBody()

    if (promoCreationDataCast != null) {
        FilterSection(promoCreationDataCast.filterItems, selectedTabId) {
            onFilterClicked.invoke(it)
        }
    }

    if (isLoadingPromoList) {
        PromotionCardShimmerGrid(modifier = Modifier.padding(top = 8.dp))
    } else if (promoCreationData != null) {
        when (promoCreationData) {
            is Success -> {
                if (promoCreationDataCast != null) {
                    PromoCreationSection(
                        list = promoCreationDataCast.items,
                        onPromoClicked = onPromoClicked,
                        promoCreationImpressed = promoCreationImpressed,
                        coachMarkListener = coachMarkListener,
                    )
                }
            }
            is Fail -> {
                CentralizedPromoError(promoCreationData.throwable, onLocalLoadRefreshClicked)
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
    onGoingResult: Result<BaseUiModel>?,
    isOnGoingLoading: Boolean,
    routeToUrl: (String) -> Unit,
    onGoingPromoImpressed: (String) -> Unit,
    onLocalLoadRefreshClicked: () -> Unit
) {

    if (isOnGoingLoading) {
        TitleHeader()
        OnGoingCardShimmerRow()
    } else if (onGoingResult != null) {
        when (onGoingResult) {
            is Success -> {
                val onGoingResultCast = onGoingResult.data as? OnGoingPromoListUiModel

                if (onGoingResultCast != null && onGoingResultCast.items.isNotEmpty()) {
                    TitleHeader()
                    OnGoingPromoSection(
                        result = onGoingResultCast,
                        routeToUrl = routeToUrl,
                        onGoingPromoImpressed = onGoingPromoImpressed
                    )
                }
            }
            is Fail -> {
                TitleHeader()
                CentralizedPromoError(onGoingResult.throwable, onLocalLoadRefreshClicked)
            }
        }
    }
}

private fun LazyGridScope.TitleHeader() {
    TitleSection(string.sah_label_promo_and_ads)
}

private fun LazyGridScope.TitleBody() {
    TitleSection(string.sh_lbl_create_promotion)
}

private fun LazyGridScope.PromoCreationSection(
    list: List<PromoCreationUiModel>,
    onPromoClicked: (PromoCreationUiModel) -> Unit,
    promoCreationImpressed: (String) -> Unit,
    coachMarkListener: (String) -> Modifier
) = items(
    items = list,
    key = { it.pageId + it.title },
    contentType = { PROMO_CREATION }) { data ->

    val state = LocalGridStateComposition.current

    PromotionCard(
        title = data.title,
        labelNew = data.titleSuffix,
        description = data.description,
        imageUrl = data.icon,
        modifier = Modifier.padding(top = 12.dp).addImpression(
            uniqueIdentifier = data.pageId + data.title,
            impressionState = data.impressHolderCompose,
            listState = state,
            onItemViewed = {
                promoCreationImpressed.invoke(it)
            },
            impressInterval = 0
        ),
        notAvailableText = data.notAvailableText,
        onPromoClicked = {
            onPromoClicked.invoke(data)
        },
        titleModifier = coachMarkListener.invoke(data.pageId)
    )
}

private fun LazyGridScope.OnGoingPromoSection(
    result: OnGoingPromoListUiModel,
    routeToUrl: (String) -> Unit,
    onGoingPromoImpressed: (String) -> Unit
) = item(span = { GridItemSpan(2) }) {
    val state = LocalListStateComposition.current
    LazyRow(state = state) {
        this@LazyRow.items(items = result.items,
            key = {
                it.title
            }
        ) { onGoingData ->
            OnGoingCard(
                title = onGoingData.title,
                counter = onGoingData.status.count,
                counterTitle = onGoingData.status.text,
                onTitleClicked = {
                    routeToUrl.invoke(onGoingData.status.url)
                },
                onFooterClicked = {
                    routeToUrl.invoke(onGoingData.status.url)
                },
                modifier = Modifier.addImpression(
                    uniqueIdentifier = onGoingData.title,
                    impressionState = onGoingData.impressHolderCompose,
                    lazyListState = state,
                    onItemViewed = {
                        onGoingPromoImpressed.invoke(it)
                    },
                    impressInterval = 0L
                )
            )
        }
    }
}

private val LocalGridStateComposition =
    compositionLocalOf<LazyGridState> { error("No Lazy Grid State provided") }

private val LocalListStateComposition =
    compositionLocalOf<LazyListState> { error("No Lazy List State provided") }

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
private fun CentralizedPromoScreenLoadingPreview() {
    CentralizedPromoScreen(
        uiState = CentralizedPromoUiState(
            isLoading = ALL
        )
    )
}

@Preview(name = "NEXUS_7", device = Devices.NEXUS_5)
@Composable
private fun CentralizedPromoScreenSuccessPreview() {
    CentralizedPromoScreen(
        uiState = CentralizedPromoUiState(
            isLoading = LoadingType.PROMO_LIST,
            onGoingData = Success(
                OnGoingPromoListUiModel(
                    title = "Fitur Ku", items = listOf(
                        OnGoingPromoUiModel(
                            title = "Flash Sale Tokopedia",
                            status = Status(
                                text = "Mendatang",
                                count = 3,
                                url = "test"
                            ),
                            footer = Footer(
                                text = "",
                                url = "test"
                            )
                        ),
                        OnGoingPromoUiModel(
                            title = "Flash Sale Tokopedia",
                            status = Status(
                                text = "Mendatang",
                                count = 3,
                                url = "test"
                            ),
                            footer = Footer(
                                text = "",
                                url = "test"
                            )
                        )
                    ),
                    errorMessage = ""
                )
            )
        )
    )
}