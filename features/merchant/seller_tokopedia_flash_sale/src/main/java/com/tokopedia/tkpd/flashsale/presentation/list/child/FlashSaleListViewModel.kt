package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerCategoryUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.FinishedFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.OngoingFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.RegisteredFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.UpcomingFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEvent
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiState
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.ceil

class FlashSaleListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase,
    private val getFlashSaleListForSellerCategoryUseCase: GetFlashSaleListForSellerCategoryUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PERCENT = 100
    }

    private val _uiState = MutableStateFlow(FlashSaleListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FlashSaleListUiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: FlashSaleListUiState
        get() = _uiState.value
    

    fun processEvent(event : FlashSaleListUiEvent) {
        when (event) {
            is FlashSaleListUiEvent.GetFlashSaleCategory -> getFlashSaleListCategory(event.tabName)
            is FlashSaleListUiEvent.LoadPage -> onLoadPage(event.tabId, event.tabName, event.offset)
            is FlashSaleListUiEvent.ChangeSort -> onChangeSort()
            is FlashSaleListUiEvent.ApplySort -> onApplySort(event.selectedSort)
            is FlashSaleListUiEvent.ChangeCategory -> onChangeCategory()
            is FlashSaleListUiEvent.ApplyCategoryFilter -> onApplyCategory(event.categories)
            FlashSaleListUiEvent.ChangeStatus -> onChangeStatus()
            is FlashSaleListUiEvent.ApplyStatusFilter -> onApplyStatusFilter(event.statuses)
            FlashSaleListUiEvent.ClearFilter -> onClearFilter()
        }
    }

    private fun onLoadPage(tabId: Int, tabName: String, offset: Int) {
        _uiState.update {
            it.copy(
                tabId = tabId,
                tabName = tabName,
                offset = offset
            )
        }
        getFlashSaleList()
    }

    private fun onChangeSort() {
        _uiEffect.tryEmit(FlashSaleListUiEffect.ShowSortBottomSheet(currentState.selectedSort.id))
    }

    private fun onApplySort(selectedSort: SingleSelectionItem) {
        _uiState.update {
            it.copy(
                selectedSort = selectedSort,
                offset = 0,
                allItems = listOf(),
                isFilterActive = selectedSort.id != "DEFAULT_VALUE_PLACEHOLDER",
                isLoading = true
            )
        }

        getFlashSaleList()
    }

    private fun onChangeCategory() {
        _uiEffect.tryEmit(
            FlashSaleListUiEffect.ShowCategoryBottomSheet(
                currentState.selectedCategoryIds,
                currentState.flashSaleCategories
            )
        )
    }

    private fun onApplyCategory(categories: List<MultipleSelectionItem>) {
        val categoryIds = categories.map { category -> category.id.toLongOrZero() }

        _uiState.update {
            it.copy(
                selectedCategoryIds = categoryIds,
                offset = 0,
                allItems = listOf(),
                isFilterActive = categories.isNotEmpty(),
                isLoading = true
            )
        }

        getFlashSaleList()
    }

    private fun onChangeStatus() {
        _uiEffect.tryEmit(FlashSaleListUiEffect.ShowStatusBottomSheet(currentState.selectedStatusIds))
    }

    private fun onApplyStatusFilter(statuses: List<MultipleSelectionItem>) {
        val statusIds = statuses.map { status -> status.id }

        _uiState.update {
            it.copy(
                selectedStatusIds = statusIds,
                offset = 0,
                allItems = listOf(),
                isFilterActive = statusIds.isNotEmpty()
            )
        }

        getFlashSaleList()
    }

    private fun onClearFilter() {
        _uiState.update {
            it.copy(
                selectedCategoryIds = listOf(),
                selectedSort = SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "", isSelected = false, direction = "ASC"),
                selectedStatusIds = listOf(),
                offset = 0,
                allItems = listOf(),
                isFilterActive = false,
                isLoading = true
            )
        }
        getFlashSaleList()
    }

    private fun getFlashSaleListCategory(tabName: String) {
        _uiState.update { it.copy(tabName = tabName) }

        launchCatchError(
            dispatchers.io,
            block = {
                val categories = getFlashSaleListForSellerCategoryUseCase.execute(tabName)
                _uiState.update { it.copy(flashSaleCategories = categories) }
            },
            onError = { error ->
                _uiEffect.tryEmit(FlashSaleListUiEffect.FetchCategoryError(error))
            }
        )

    }

    private fun getFlashSaleList() {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = GetFlashSaleListForSellerUseCase.Param(
                    currentState.tabName,
                    currentState.offset,
                    categoryIds = currentState.selectedCategoryIds,
                    statusIds = currentState.selectedStatusIds,
                    sortOrderBy = currentState.selectedSort.id,
                    sortOrderRule = currentState.selectedSort.direction,
                    requestProductMetaData = currentState.tabName == "finished" || currentState.tabName == "ongoing"
                )
                val response = getFlashSaleListForSellerUseCase.execute(params)
                val formattedFlashSales = formatFlashSaleData(currentState.tabId, response.flashSales)

                val allItems = currentState.allItems + formattedFlashSales
                _uiEffect.emit(FlashSaleListUiEffect.LoadNextPageSuccess(response.totalFlashSaleCount, allItems, formattedFlashSales))

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allItems = allItems,
                        totalFlashSaleOnCurrentPage = response.flashSales.size,
                        totalFlashSale = response.totalFlashSaleCount
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false) }
                _uiEffect.tryEmit(FlashSaleListUiEffect.FetchFlashSaleError(error))
            }
        )

    }

    private fun formatFlashSaleData(tabId: Int, flashSales: List<FlashSale>) : List<DelegateAdapterItem> {
        return flashSales.map { flashSale ->
            when(tabId) {
                TabConstant.TAB_ID_UPCOMING -> flashSale.toUpcomingItem()
                TabConstant.TAB_ID_REGISTERED -> flashSale.toRegisteredItem()
                TabConstant.TAB_ID_ONGOING -> flashSale.toOngoingItem()
                TabConstant.TAB_ID_FINISHED -> flashSale.toFinishedItem()
                else -> flashSale.toUpcomingItem()
            }
        }
    }

    private fun FlashSale.toUpcomingItem() : DelegateAdapterItem {
        return UpcomingFlashSaleItem(
            campaignId,
            name,
            coverImage,
            remainingQuota,
            maxProductSubmission,
            formattedDate.startDate,
            formattedDate.endDate,
            endDateUnix,
            findQuotaUsagePercentage(),
            submissionEndDateUnix,
            useMultiLocation
        )
    }

    private fun FlashSale.toFinishedItem() : DelegateAdapterItem {
        return FinishedFlashSaleItem(
            campaignId,
            name,
            coverImage,
            formattedDate.startDate,
            formattedDate.endDate,
            status,
            statusText,
            productMeta,
            cancellationReason,
            findProductSoldPercentage(),
            useMultiLocation
        )
    }

    private fun FlashSale.findProductSoldPercentage() : Int {
        val sold = (productMeta.totalStockSold.toFloat() / productMeta.totalProductStock.toFloat()) * PERCENT
        val soldPercentage = ceil(sold)
        return soldPercentage.toInt()
    }

    private fun FlashSale.toRegisteredItem() : DelegateAdapterItem {
        return RegisteredFlashSaleItem(
            campaignId,
            name,
            coverImage,
            startDateUnix,
            endDateUnix,
            formattedDate.startDate,
            formattedDate.endDate,
            reviewStartDateUnix,
            reviewEndDateUnix,
            status,
            statusText,
            useMultiLocation
        )
    }

    private fun FlashSale.findQuotaUsagePercentage() : Int {
        val usedQuota = maxProductSubmission - remainingQuota
        return ((usedQuota.toFloat() / maxProductSubmission.toFloat()) * PERCENT).toInt()
    }

    private fun FlashSale.toOngoingItem(): DelegateAdapterItem {
        return OngoingFlashSaleItem(
            campaignId,
            name,
            coverImage,
            formattedDate.startDate,
            formattedDate.endDate,
            endDateUnix,
            productMeta.totalStockSold,
            productMeta.totalProductStock,
            status,
            statusText,
            useMultiLocation
        )
    }
}
