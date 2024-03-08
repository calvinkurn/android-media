package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetMiniCartUseCase
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.cartcommon.domain.usecase.SetCartlistCheckboxStateUseCase
import dagger.Lazy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartViewModel @Inject constructor(
    setCartListCheckboxStateUseCase: Lazy<SetCartlistCheckboxStateUseCase>,
    private val getMiniCartUseCase: Lazy<GetMiniCartUseCase>,
    private val miniCartLocalCacheUseCases: Lazy<MiniCartLocalCacheUseCases>,
    private val dispatchers: Lazy<CoroutineDispatchers>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get()) {

    private val _cartData = MutableStateFlow<BmgmState<BmgmMiniCartDataUiModel>>(BmgmState.Loading)
    val cartData: StateFlow<BmgmState<BmgmMiniCartDataUiModel>>
        get() = _cartData.asStateFlow()

    private var miniCartJob: Job? = null

    fun getMiniCartData(param: MiniCartParam, showLoadingState: Boolean) {
        miniCartJob?.cancel()
        miniCartJob = viewModelScope.launch {
            runCatching {
                if (showLoadingState) {
                    _cartData.update { BmgmState.Loading }
                }
                val data = withContext(dispatchers.get().io) {
                    getMiniCartUseCase.get().invoke(param)
                }
                _cartData.update { BmgmState.Success(data) }
            }.onFailure { t ->
                _cartData.update { BmgmState.Error(t) }
            }
        }
        miniCartJob?.start()
    }

    fun clearCartDataLocalCache() {
        viewModelScope.launch {
            miniCartLocalCacheUseCases.get().clearLocalCache()
        }
    }

    fun saveCartDataToLocalStorage(shopId: Long, warehouseId: Long, offerEndDate: String) {
        viewModelScope.launch {
            val result = _cartData.value
            if (result is BmgmState.Success) {
                miniCartLocalCacheUseCases.get()
                    .saveToLocalCache(result.data, shopId, warehouseId, offerEndDate)
            }
        }
    }
}
