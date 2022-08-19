package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.campaign.entity.SingleSelectionItem
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerCategoryUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.FinishedFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.OngoingFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.RegisteredFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.UpcomingFlashSaleItem
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

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

     data class UiState(
         val isLoading: Boolean = true,
         val totalFlashSaleCount: Int = 0,
         val isLoadingNextPage: Boolean = false,
         val flashSales: List<DelegateAdapterItem> = emptyList(),
         val tabName: String = "",
         val tabId: Int = TabConstant.TAB_ID_UPCOMING,
         val offset: Int = 0,
         val selectedSort : SingleSelectionItem = SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "", isSelected = false, direction = "ASC"),
         val selectedCategoryIds: List<Long> = emptyList(),
         val flashSaleCategories : List<FlashSaleCategory> = emptyList(),
         val isUsingFilter: Boolean = false,
         val error: Throwable? = null
    )

    sealed class UiEvent {
        data class Init(val tabName : String, val tabId: Int, val totalFlashSaleCount: Int) : UiEvent()
        data class LoadNextPage(val offset : Int) : UiEvent()
        object ChangeSort : UiEvent()
        data class ApplySort(val selectedSort: SingleSelectionItem) : UiEvent()
        object ChangeCategory : UiEvent()
        data class ApplyCategoryFilter(val categories: List<MultipleSelectionItem>) : UiEvent()
        object ClearFilter : UiEvent()
    }

    sealed class UiEffect {
        data class FetchFlashSaleError(val throwable: Throwable) : UiEffect()
        data class ShowSortBottomSheet(val selectedSortId : String) : UiEffect()
        data class ShowCategoryBottomSheet(
            val selectedCategoryIds: List<Long>,
            val categories: List<FlashSaleCategory>
        ) : UiEffect()
    }

    fun processEvent(event : UiEvent) {
        when (event) {
            is UiEvent.Init -> onPageFirstAppear(event.tabName, event.tabId, event.totalFlashSaleCount)
            is UiEvent.LoadNextPage -> onLoadNextPage(event.offset)
            is UiEvent.ChangeSort -> onChangeSort()
            is UiEvent.ApplySort -> onApplySort(event.selectedSort)
            is UiEvent.ChangeCategory -> onChangeCategory()
            is UiEvent.ApplyCategoryFilter -> onApplyCategory(event.categories)
            UiEvent.ClearFilter -> onClearFilter()
        }
    }

    private fun onPageFirstAppear(tabName: String, tabId: Int, totalFlashSaleCount: Int) {
        _uiState.update {
            it.copy(
                isLoading = true,
                tabName = tabName,
                tabId = tabId,
                offset = 0,
                totalFlashSaleCount = totalFlashSaleCount
            )
        }

        getFlashSaleList()
        getFlashSaleListCategory()
    }

    private fun onLoadNextPage(offset: Int) {
        _uiState.update { it.copy(isLoadingNextPage = true, offset = offset) }
        getFlashSaleList()
    }

    private fun onChangeSort() {
        _uiEffect.tryEmit(UiEffect.ShowSortBottomSheet(_uiState.value.selectedSort.id))
    }

    private fun onApplySort(selectedSort: SingleSelectionItem) {
        _uiState.update {
            it.copy(
                isLoading = true,
                selectedSort = selectedSort,
                flashSales = emptyList(),
                offset = 0,
                isUsingFilter = true
            )
        }

        getFlashSaleList()
    }

    private fun onChangeCategory() {
        _uiEffect.tryEmit(
            UiEffect.ShowCategoryBottomSheet(
                _uiState.value.selectedCategoryIds,
                _uiState.value.flashSaleCategories
            )
        )
    }

    private fun onApplyCategory(categories: List<MultipleSelectionItem>) {
        val categoryIds = categories.map { category -> category.id.toLongOrZero() }

        _uiState.update {
            it.copy(
                isLoading = true,
                selectedCategoryIds = categoryIds,
                flashSales = emptyList(),
                offset = 0,
                isUsingFilter = categoryIds.isNotEmpty()
            )
        }

        getFlashSaleList()
    }

    private fun onClearFilter() {
        _uiState.update {
            it.copy(
                isLoading = true,
                selectedCategoryIds = listOf(),
                selectedSort = SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "", isSelected = false, direction = "ASC"),
                flashSales = emptyList(),
                offset = 0,
                isUsingFilter = false
            )
        }
        getFlashSaleList()
    }

    private fun getFlashSaleListCategory() {
        launchCatchError(
            dispatchers.io,
            block = {
                val categories = getFlashSaleListForSellerCategoryUseCase.execute(_uiState.value.tabName)
                _uiState.update { it.copy(isLoading = false, error = null, flashSaleCategories = categories) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )

    }

    private fun getFlashSaleList() {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = GetFlashSaleListForSellerUseCase.Param(
                    _uiState.value.tabName,
                    _uiState.value.offset,
                    categoryIds = _uiState.value.selectedCategoryIds,
                    sortOrderBy = _uiState.value.selectedSort.id,
                    sortOrderRule = _uiState.value.selectedSort.direction
                )
                val flashSales = getFlashSaleListForSellerUseCase.execute(params)
                val formattedFlashSales = formatFlashSaleData(_uiState.value.tabId, flashSales)

                _uiState.update { it.copy(isLoading = false, isLoadingNextPage = false, error = null, flashSales = formattedFlashSales) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, isLoadingNextPage = false,  error = error) }
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
                else -> throw IllegalArgumentException("Unknown")
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

    private fun FlashSale.findQuotaUsagePercentage() : Int {
        val usedQuota = maxProductSubmission - remainingQuota
        return ((usedQuota.toFloat() / maxProductSubmission.toFloat()) * PERCENT).toInt()
    }

    private fun FlashSale.toOngoingItem() : DelegateAdapterItem {
        return OngoingFlashSaleItem(campaignId, name)
    }
}
