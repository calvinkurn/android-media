@file:OptIn(ExperimentalMaterialApi::class)

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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.R.drawable
import com.tokopedia.centralizedpromo.R.string
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.LayoutType.PROMO_CREATION
import com.tokopedia.centralizedpromo.view.bottomSheet.DetailPromoBottomSheet
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent.SwipeRefresh
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoUiState
import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.components.CoachMarkAnchor
import com.tokopedia.nest.components.coachmarkableOn
import com.tokopedia.nest.principles.utils.ImpressionHolder
import com.tokopedia.nest.principles.utils.addImpression
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.sortfilter.compose.NestSortFilter
import com.tokopedia.sortfilter.compose.SortFilter
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
    val screenState =
        rememberScreenState(
            pullRefreshState = rememberPullRefreshState(
                uiState.isSwipeRefresh,
                {
                    onEvent.invoke(SwipeRefresh)
                }
            )
        )

    Scaffold(
        topBar = {
            NestHeader(
                type = NestHeaderType.SingleLine().copy(
                    title = stringResource(string.centralized_promo_toolbar_title),
                    onBackClicked = onBackPressed
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(it)
                .pullRefresh(screenState.pullRefreshState)
        ) {
            BackgroundDrawable()

            CompositionLocalProvider(
                LocalScreenStateComposition provides screenState
            ) {
                LazyVerticalGrid(
                    columns = Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    state = screenState.lazyGridState,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HeaderSection(
                        onGoingResult = uiState.onGoingData,
                        onGoingPromoImpressed = {
                            impressOnGoingPromo(it)
                        }
                    ) {
                        onEvent.invoke(CentralizedPromoEvent.LoadOnGoingPromo)
                    }

                    BodySection(
                        promoCreationData = uiState.promoCreationData,
                        selectedTabId = uiState.selectedTabId(),
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
                                screenState.context,
                                uiState.selectedTabName(),
                                promoCreationUiModel,
                                !checkRbac.invoke(promoCreationUiModel.title)
                            ) {
                                onEvent.invoke(CentralizedPromoEvent.UpdateRbacBottomSheet(it))
                            }
                        },
                        promoCreationImpressed = {
                            impressPromoCreation(
                                it,
                                uiState.selectedTabId(),
                                uiState.selectedTabName()
                            )
                        },
                        onLocalLoadRefreshClicked = {
                            onEvent.invoke(CentralizedPromoEvent.LoadPromoCreation)
                        }
                    ) { pageId ->
                        // disable coachmarlableOn when we don't have to show coachmark anymore
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
                }

                PullRefreshIndicator(
                    uiState.isSwipeRefresh,
                    screenState.pullRefreshState,
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

private fun impressPromoCreation(title: String, currentFilterId: String, currentFilterName: String) {
    if (currentFilterId == ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE) {
        CentralizedPromoTracking.sendImpressionAovCard(title)
    } else {
        CentralizedPromoTracking.sendImpressionCard(title, currentFilterName)
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
                context,
                promoCreationUiModel.ctaLink
            )
        } else {
            val detailPromoBottomSheet =
                DetailPromoBottomSheet.createInstance(promoCreationUiModel)
            detailPromoBottomSheet.show((context as FragmentActivity).supportFragmentManager)
            var isCheckedRbac = false
            detailPromoBottomSheet.onCheckBoxListener { isChecked ->
                isCheckedRbac = isChecked
                CentralizedPromoTracking.sendClickCheckboxBottomSheet(
                    selectedTabName,
                    promoCreationUiModel.title
                )
            }
            detailPromoBottomSheet.setOnDismissListener {
                if (isCheckedRbac) {
                    ignoreBottomSheet.invoke(promoCreationUiModel.title)
                }
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
    promoCreationData: CentralizedPromoResult<BaseUiModel>,
    selectedTabId: String,
    onFilterClicked: (Pair<String, String>) -> Unit,
    onPromoClicked: (PromoCreationUiModel) -> Unit,
    promoCreationImpressed: (String) -> Unit,
    onLocalLoadRefreshClicked: () -> Unit,
    coachMarkListener: (String) -> Modifier
) {
    if (promoCreationData !is CentralizedPromoResult.Empty) {
        TitleBody()
    }

    when (promoCreationData) {
        is CentralizedPromoResult.Loading -> {
            PromotionCardShimmerGrid(modifier = Modifier.padding(top = 8.dp))
        }
        is CentralizedPromoResult.Success -> {
            val promoCreationDataCast =
                (promoCreationData as? CentralizedPromoResult.Success)?.data as? PromoCreationListUiModel

            if (promoCreationDataCast != null) {
                FilterSection(
                    promoCreationDataCast.filterItems,
                    selectedTabId,
                    promoCreationDataCast.aovFilterImpressionHolder,
                    onAovFilterImpressed = { isSelected ->
                        promoCreationDataCast.aovFilterImpressionHolder.impressed = true
                        CentralizedPromoTracking.sendImpressionAovFilter(isSelected)
                    }
                ) {
                    onFilterClicked.invoke(it)
                }

                PromoCreationSection(
                    list = promoCreationDataCast.items,
                    onPromoClicked = onPromoClicked,
                    promoCreationImpressed = promoCreationImpressed,
                    coachMarkListener = coachMarkListener
                )
            }
        }
        is CentralizedPromoResult.Fail -> {
            CentralizedPromoError(
                promoCreationData.throwable ?: Throwable(),
                promoCreationData.isLoading,
                onLocalLoadRefreshClicked
            )
        }
        else -> {
        }
    }
}

private fun LazyGridScope.FilterSection(
    filterItems: List<FilterPromoUiModel>,
    selectedTabId: String,
    aovFilterImpressionHolder: ImpressionHolder,
    onAovFilterImpressed: (Boolean) -> Unit,
    onFilterClicked: (Pair<String, String>) -> Unit
) = item(
    span = { GridItemSpan(2) }
) {
    val result = remember(filterItems, selectedTabId, aovFilterImpressionHolder) {
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

    val state = LocalScreenStateComposition.current

    trackAovImpression(state, selectedTabId, filterItems, aovFilterImpressionHolder, onAovFilterImpressed)

    NestSortFilter(
        items = result,
        showClearFilterIcon = false,
        state = state.filterListState
    )
}

private fun LazyGridScope.HeaderSection(
    onGoingResult: CentralizedPromoResult<BaseUiModel>,
    onGoingPromoImpressed: (String) -> Unit,
    onLocalLoadRefreshClicked: () -> Unit
) {
    if (onGoingResult !is CentralizedPromoResult.Empty) {
        TitleHeader()
    }

    when (onGoingResult) {
        is CentralizedPromoResult.Loading -> {
            OnGoingCardShimmerRow()
        }
        is CentralizedPromoResult.Success -> {
            val onGoingResultCast = onGoingResult.data as? OnGoingPromoListUiModel
            if (onGoingResultCast != null) {
                OnGoingPromoSection(
                    result = onGoingResultCast,
                    onGoingPromoImpressed = onGoingPromoImpressed
                )
            }
        }
        is CentralizedPromoResult.Fail -> {
            CentralizedPromoError(
                onGoingResult.throwable ?: Throwable(),
                onGoingResult.isLoading,
                onLocalLoadRefreshClicked
            )
        }
        else -> {
        }
    }
}

private fun LazyGridScope.TitleHeader() {
    TitleSection(title = string.sah_label_promo_and_ads, modifier = Modifier.tag("tvOnGoingPromo"))
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
    key = { it.pageId + it.title + it.currentTimeMillis },
    contentType = { PROMO_CREATION }
) { data ->

    val state = LocalScreenStateComposition.current

    PromotionCard(
        title = data.title,
        labelNew = data.titleSuffix,
        description = data.description,
        imageUrl = data.icon,
        modifier = Modifier
            .padding(top = 12.dp)
            .addImpression(
                // We append currentTimeMillis in uniqueId as we need to track promo card impression each time a filter tab is clicked
                // With this identifier, we could differentiate between promo cards that has been hit before and after filter tab changes
                // Without currentTimeMillis, whenever we change filter tab, we couldn't track new promo cards with same id+title that was impressed before
                uniqueIdentifier = data.pageId + data.title + data.currentTimeMillis,
                impressionState = data.impressHolderCompose,
                state = state.lazyGridState,
                onItemViewed = {
                    promoCreationImpressed.invoke(data.title)
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

@OptIn(ExperimentalMaterialApi::class)
private fun LazyGridScope.OnGoingPromoSection(
    result: OnGoingPromoListUiModel,
    onGoingPromoImpressed: (String) -> Unit
) = item(span = { GridItemSpan(2) }) {
    val state = LocalScreenStateComposition.current
    LazyRow(
        state = state.lazyListState,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        this@LazyRow.itemsIndexed(
            items = result.items,
            key = { idx, it ->
                it.title + idx.toString()
            }
        ) { idx, onGoingData ->
            OnGoingCard(
                title = onGoingData.title,
                counter = onGoingData.status.count,
                counterTitle = onGoingData.status.text,
                onTitleClicked = {
                    CentralizedPromoTracking.sendClickOnGoingPromoStatus(
                        widgetName = onGoingData.title
                    )
                    RouteManager.route(state.context, onGoingData.status.url)
                },
                onFooterClicked = {
                    CentralizedPromoTracking.sendClickOnGoingPromoFooter(
                        widgetName = onGoingData.title
                    )
                    RouteManager.route(state.context, onGoingData.status.url)
                },
                modifier = Modifier.addImpression(
                    uniqueIdentifier = onGoingData.title + idx.toString(),
                    impressionState = onGoingData.impressHolderCompose,
                    state = state.lazyListState,
                    onItemViewed = {
                        onGoingPromoImpressed.invoke(onGoingData.title)
                    },
                    impressInterval = 0L
                )
            )
        }
    }
}

private fun trackAovImpression(
    state: ScreenState,
    selectedTabId: String,
    filterItems: List<FilterPromoUiModel>,
    impressionHolder: ImpressionHolder,
    onAovFilterImpressed: (Boolean) -> Unit
) {
    val shouldTrackImpressionIndex =
        filterItems.indexOfFirst { it.id == ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE }
    if (shouldTrackImpressionIndex != RecyclerView.NO_POSITION) {
        val shouldTrackImpression =
            state.filterListState.layoutInfo.visibleItemsInfo.any { it.index == shouldTrackImpressionIndex } && !impressionHolder.impressed
        if (shouldTrackImpression) {
            val isSelected =
                filterItems.firstOrNull { it.id == ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE }?.id == selectedTabId
            onAovFilterImpressed.invoke(isSelected)
        }
    }
}

private val LocalScreenStateComposition =
    compositionLocalOf<ScreenState> { error("No Screen State provided") }

@Composable
private fun BackgroundDrawable() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(drawable.bg_bottom_circle),
            contentDescription = null
        )
    }
}
