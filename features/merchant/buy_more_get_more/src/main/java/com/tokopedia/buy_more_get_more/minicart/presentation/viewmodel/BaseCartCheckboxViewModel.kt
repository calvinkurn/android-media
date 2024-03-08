package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.cartcommon.data.request.checkbox.SetCartlistCheckboxStateRequest
import com.tokopedia.cartcommon.domain.usecase.SetCartlistCheckboxStateUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by @ilhamsuaib on 28/08/23.
 */

open class BaseCartCheckboxViewModel(
    private val setCartListCheckboxStateUseCase: SetCartlistCheckboxStateUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _setCheckListState = MutableSharedFlow<BmgmState<Boolean>>()
    val setCheckListState: SharedFlow<BmgmState<Boolean>>
        get() = _setCheckListState.asSharedFlow()

    fun setCartListCheckboxState(cartIds: List<String>) {
        viewModelScope.launch {
            runCatching {
                _setCheckListState.emit(BmgmState.Loading)
                val result = withContext(dispatchers.io) {
                    setCartListCheckboxStateUseCase.invoke(getCartIdList(cartIds))
                }
                _setCheckListState.emit(BmgmState.Success(result))
            }.onFailure {
                _setCheckListState.emit(BmgmState.Error(it))
            }
        }
    }

    private fun getCartIdList(cartIds: List<String>): List<SetCartlistCheckboxStateRequest> {
        return cartIds.map { SetCartlistCheckboxStateRequest(it, true) }
    }
}
