package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.domain.usecase.GetBmgmMiniCartDataUseCase
import com.tokopedia.minicart.bmgm.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartViewModel @Inject constructor(
    setCartListCheckboxStateUseCase: Lazy<SetCartListCheckboxStateUseCase>,
    private val getMiniCartDataUseCase: Lazy<GetBmgmMiniCartDataUseCase>,
    private val miniCartLocalCacheUseCases: Lazy<MiniCartLocalCacheUseCases>,
    private val dispatchers: Lazy<CoroutineDispatchers>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get()) {

    private val _cartData = MutableStateFlow<BmgmState<BmgmMiniCartDataUiModel>>(BmgmState.Loading)
    val cartData: StateFlow<BmgmState<BmgmMiniCartDataUiModel>>
        get() = _cartData

    fun getMiniCartData(shopIds: List<Long>, param: BmgmParamModel, showLoadingState: Boolean) {
        viewModelScope.launch {
            runCatching {
                if (showLoadingState) {
                    _cartData.emit(BmgmState.Loading)
                }
                val data = withContext(dispatchers.get().io) {
                    getMiniCartDataUseCase.get().invoke(shopIds, param)
                }
                _cartData.emit(BmgmState.Success(data))
                storeCartDataToLocalCache()
            }.onFailure {
                _cartData.emit(BmgmState.Error(it))
            }
        }
    }

    fun clearCartDataLocalCache() {
        miniCartLocalCacheUseCases.get().clearLocalCache()
    }

    fun storeCartDataToLocalCache() {
        val result = _cartData.value
        if (result is BmgmState.Success) {
            miniCartLocalCacheUseCases.get().saveToLocalCache(result.data)
        }
    }
}