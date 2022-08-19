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
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.OngoingFlashSaleItem
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
         val flashSales: List<DelegateAdapterItem> = emptyList(),
         val shouldResetList: Boolean = false,
         val tabName: String = "",
         val tabId: Int = TabConstant.TAB_ID_UPCOMING,
         val offset: Int = 0,
         val selectedSort : SingleSelectionItem = SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "",isSelected = false, direction = "ASC"),
         val selectedCategoryIds: List<Long> = emptyList(),
         val flashSaleCategories : List<FlashSaleCategory> = emptyList(),
         val error: Throwable? = null
    )

    sealed class UiEvent {
        data class Init(val tabName : String, val tabId: Int) : UiEvent()
        data class LoadPage( val offset : Int) : UiEvent()
        object ChangeSort : UiEvent()
        data class ApplySort(val selectedSort: SingleSelectionItem) : UiEvent()
        object ChangeCategory : UiEvent()
        data class ApplyCategoryFilter(val selectedCategoryIds: List<MultipleSelectionItem>) : UiEvent()
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
            is UiEvent.Init -> {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        tabName = event.tabName,
                        tabId = event.tabId,
                        offset = 0,
                        shouldResetList = false
                    )
                }

                getFlashSaleList()
                getFlashSaleListCategory()
            }
            is UiEvent.LoadPage -> {
                _uiState.update {
                    it.copy(
                        offset = event.offset,
                        shouldResetList = false
                    )
                }

                getFlashSaleList()
            }
            is UiEvent.ChangeSort -> {
                _uiEffect.tryEmit(UiEffect.ShowSortBottomSheet(_uiState.value.selectedSort.id))
            }
            is UiEvent.ApplySort -> {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        selectedSort = event.selectedSort,
                        shouldResetList = true,
                        offset = 0
                    )
                }

                getFlashSaleList()
            }
            is UiEvent.ChangeCategory -> {
                _uiEffect.tryEmit(
                    UiEffect.ShowCategoryBottomSheet(
                        _uiState.value.selectedCategoryIds,
                        _uiState.value.flashSaleCategories
                    )
                )
            }
            is UiEvent.ApplyCategoryFilter -> {
                val categoryIds = event.selectedCategoryIds.map { category -> category.id.toLongOrZero() }

                _uiState.update {
                    it.copy(
                        isLoading = true,
                        shouldResetList = true,
                        selectedCategoryIds = categoryIds,
                        offset = 0
                    )
                }

                getFlashSaleList()
            }
            UiEvent.ClearFilter -> {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        selectedCategoryIds = listOf(),
                        selectedSort = SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "", isSelected = false, direction = "ASC"),
                        shouldResetList = true,
                        offset = 0
                    )
                }
                getFlashSaleList()
            }
        }
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
                val campaigns = getFlashSaleListForSellerUseCase.execute(params)
                _uiState.update { it.copy(isLoading = false, error = null, flashSales = formatFlashSaleData(_uiState.value.tabId, campaigns)) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )

    }

    private fun formatFlashSaleData(tabId: Int, flashSales: List<FlashSale>) : List<DelegateAdapterItem> {
        return flashSales.map { flashSale ->
            when(tabId) {
                TabConstant.TAB_ID_UPCOMING -> flashSale.toUpcomingItem()
                TabConstant.TAB_ID_REGISTERED -> flashSale.toUpcomingItem()
                TabConstant.TAB_ID_ONGOING -> flashSale.toOngoingItem()
                TabConstant.TAB_ID_FINISHED -> flashSale.toUpcomingItem()
                else -> flashSale.toUpcomingItem()
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

    private fun FlashSale.findQuotaUsagePercentage() : Int {
        val usedQuota = maxProductSubmission - remainingQuota
        return ((usedQuota.toFloat() / maxProductSubmission.toFloat()) * PERCENT).toInt()
    }

    private fun FlashSale.toOngoingItem() : DelegateAdapterItem {
        return OngoingFlashSaleItem(campaignId, name)
    }
}
