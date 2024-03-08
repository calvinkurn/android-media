package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.cartcommon.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 21/08/23.
 */

class BmgmMiniCartDetailViewModel @Inject constructor(
    private val localCacheUseCases: Lazy<MiniCartLocalCacheUseCases>,
    setCartListCheckboxStateUseCase: Lazy<SetCartlistCheckboxStateUseCase>,
    private val dispatchers: Lazy<CoroutineDispatchers>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get()) {
    val cartData: StateFlow<BmgmState<BmgmCommonDataModel>>
        get() = _cartData.asStateFlow()
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
