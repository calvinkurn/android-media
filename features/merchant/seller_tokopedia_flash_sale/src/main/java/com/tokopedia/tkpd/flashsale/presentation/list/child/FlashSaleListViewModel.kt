package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.campaign.entity.SingleSelectionItem
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
import com.tokopedia.tkpd.flashsale.util.extension.hoursDifference
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

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
            is FlashSaleListUiEvent.Init -> onPageFirstAppear(event.tabName, event.tabId)
            is FlashSaleListUiEvent.LoadPage -> onLoadPage(event.offset)
            is FlashSaleListUiEvent.ChangeSort -> onChangeSort()
            is FlashSaleListUiEvent.ApplySort -> onApplySort(event.selectedSort)
            is FlashSaleListUiEvent.ChangeCategory -> onChangeCategory()
            is FlashSaleListUiEvent.ApplyCategoryFilter -> onApplyCategory(event.categories)
            FlashSaleListUiEvent.ChangeStatus -> onChangeStatus()
            is FlashSaleListUiEvent.ApplyStatusFilter -> onApplyStatusFilter(event.statuses)
            FlashSaleListUiEvent.ClearFilter -> onClearFilter()
        }
    }

    private fun onPageFirstAppear(tabName: String, tabId: Int) {
        _uiState.update {
            it.copy(tabName = tabName, tabId = tabId)
        }

        getFlashSaleListCategory()
    }

    private fun onLoadPage(offset: Int) {
        _uiState.update { it.copy(offset = offset) }
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
                isFilterActive = selectedSort.id != "DEFAULT_VALUE_PLACEHOLDER"
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
                isFilterActive = categories.isNotEmpty()
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
                isFilterActive = false
            )
        }

        getFlashSaleList()
    }

    private fun getFlashSaleListCategory() {
        launchCatchError(
            dispatchers.io,
            block = {
                val categories = getFlashSaleListForSellerCategoryUseCase.execute(currentState.tabName)
                _uiState.update { it.copy(flashSaleCategories = categories) }
            },
            onError = { error ->
                _uiEffect.tryEmit(FlashSaleListUiEffect.FetchCategoryError(error))
            }
        )

    }

    private fun getFlashSaleList() {
        _uiState.update { it.copy(isLoading = true) }

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
                    requestProductMetaData = currentState.tabName == "ongoing"
                )
                val response = getFlashSaleListForSellerUseCase.execute(params)
                val formattedFlashSales = formatFlashSaleData(currentState.tabId, response.flashSales)

                val allItems = currentState.allItems + formattedFlashSales
                _uiEffect.emit(FlashSaleListUiEffect.LoadNextPageSuccess(allItems, formattedFlashSales))

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allItems = allItems,
                        totalFlashSaleCount = response.totalFlashSaleCount
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
                else -> throw IllegalArgumentException("Cannot map to model. Tab id is not registered.")
            }
        }
    }

    private fun FlashSale.toUpcomingItem() : DelegateAdapterItem {
        val now = Date()
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
            hoursDifference(now, submissionEndDateUnix),
            submissionEndDateUnix
        )
    }

    private fun FlashSale.toFinishedItem() : DelegateAdapterItem {
        val now = Date()
        return FinishedFlashSaleItem(
            campaignId,
            name,
            coverImage,
            remainingQuota,
            maxProductSubmission,
            formattedDate.startDate,
            formattedDate.endDate,
            endDateUnix,
            findQuotaUsagePercentage(),
            hoursDifference(now, submissionEndDateUnix),
            submissionEndDateUnix
        )
    }

    private fun FlashSale.toRegisteredItem() : DelegateAdapterItem {
        val now = Date()
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
            hoursDifference(now, startDateUnix),
            hoursDifference(now, reviewStartDateUnix),
            hoursDifference(now, reviewEndDateUnix),
            status,
            statusText
        )
    }

    private fun FlashSale.findQuotaUsagePercentage() : Int {
        val usedQuota = maxProductSubmission - remainingQuota
        return ((usedQuota.toFloat() / maxProductSubmission.toFloat()) * PERCENT).toInt()
    }

    private fun FlashSale.toOngoingItem(): DelegateAdapterItem {
        val now = Date()
        return OngoingFlashSaleItem(
            campaignId,
            name,
            coverImage,
            formattedDate.startDate,
            formattedDate.endDate,
            endDateUnix,
            hoursDifference(now, endDateUnix),
            productMeta.totalStockSold,
            productMeta.totalProductStock,
            status,
            statusText
        )
    }
}
