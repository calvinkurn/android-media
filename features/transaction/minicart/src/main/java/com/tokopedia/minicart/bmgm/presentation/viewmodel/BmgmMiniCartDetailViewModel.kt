package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.bmgm.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.minicart.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 21/08/23.
 */

class BmgmMiniCartDetailViewModel @Inject constructor(
    private val localCacheUseCases: Lazy<MiniCartLocalCacheUseCases>,
    setCartListCheckboxStateUseCase: Lazy<SetCartListCheckboxStateUseCase>,
    private val dispatchers: Lazy<CoroutineDispatchers>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get()) {
    val cartData: StateFlow<BmgmState<BmgmCommonDataModel>>
        get() = _cartData
    private val _cartData = MutableStateFlow<BmgmState<BmgmCommonDataModel>>(BmgmState.None)

    fun getCartData() = viewModelScope.launch(dispatchers.get().io) {
        runCatching {
            val cartData = localCacheUseCases.get().getCartData()
            _cartData.emit(BmgmState.Success(cartData))
        }.onFailure {
            _cartData.emit(BmgmState.Error(it))
        }
    }
}