package com.tokopedia.bmsm_widget.presentation.bottomsheet

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.bmsm_widget.domain.entity.TierGift
import com.tokopedia.bmsm_widget.domain.usecase.GetOfferProductsBenefitListUseCase
import com.tokopedia.bmsm_widget.presentation.bottomsheet.uimodel.GiftListEvent
import com.tokopedia.bmsm_widget.presentation.bottomsheet.uimodel.GiftListUiState
import com.tokopedia.bmsm_widget.util.constant.BundleConstant
import com.tokopedia.bmsm_widget.util.tracker.GiftListBottomSheetTracker
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class GiftListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getGiftListUseCase: GetOfferProductsBenefitListUseCase,
    private val tracker: GiftListBottomSheetTracker
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(GiftListUiState())
    val uiState = _uiState.asStateFlow()

    private val currentState: GiftListUiState
        get() = _uiState.value

    fun processEvent(event: GiftListEvent) {
        when (event) {
            is GiftListEvent.OpenScreen -> handleOpenScreenEvent(event)
            GiftListEvent.GetGiftList -> getGiftList()
            is GiftListEvent.ChangeGiftTier -> handleChangeGiftTier(event.selectedTier)
            GiftListEvent.TapIconCloseBottomSheet -> handleTapIconCloseBottomSheet()
        }
    }

    private fun handleOpenScreenEvent(event: GiftListEvent.OpenScreen) {
        _uiState.update {
            it.copy(
                offerId = event.offerId,
                warehouseId = event.warehouseId,
                tierProducts = event.giftProducts,
                source = event.source,
                selectedTierId = event.selectedTierId,
                userCache = event.userCache,
                shopId = event.shopId,
                mainProducts = event.mainProducts
            )
        }
        tracker.sendImpressionOpenGiftListBottomSheet(
            offerId = event.offerId,
            warehouseId = event.warehouseId,
            shopId = currentState.shopId
        )
    }

    private fun getGiftList() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferProductsBenefitListUseCase.Param(
                    source = currentState.source,
                    filter = GetOfferProductsBenefitListUseCase.Param.Filter(
                        offerId = currentState.offerId,
                        warehouseId = currentState.warehouseId,
                        tierProduct = currentState.tierProducts,
                        mainProducts = currentState.mainProducts
                    ),
                    userCache = currentState.userCache
                )

                val tierGift = getGiftListUseCase.execute(param)
                val selectedTier = if (currentState.selectedTierId == BundleConstant.ID_NO_SELECTED_TIER) {
                    tierGift.firstOrNull()
                } else {
                    tierGift.find { it.tierId == currentState.selectedTierId }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tierGift = tierGift,
                        error = null,
                        selectedTier = selectedTier
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun handleChangeGiftTier(tier: TierGift) {
        _uiState.update { it.copy(selectedTier = tier) }
        tracker.sendClickTapChip(
            offerId = currentState.offerId,
            warehouseId = currentState.warehouseId,
            tierId = tier.tierId,
            chipName = tier.tierName,
            shopId = currentState.shopId
        )
    }

    private fun handleTapIconCloseBottomSheet() {
        tracker.sendClickCloseGiftListBottomSheet(
            offerId = currentState.offerId,
            warehouseId = currentState.warehouseId,
            shopId = currentState.shopId
        )
    }
}
