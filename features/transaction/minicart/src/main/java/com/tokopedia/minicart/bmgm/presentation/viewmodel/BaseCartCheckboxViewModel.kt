package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by @ilhamsuaib on 28/08/23.
 */

open class BaseCartCheckboxViewModel(
    private val setCartListCheckboxStateUseCase: SetCartListCheckboxStateUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _setCheckListState = MutableSharedFlow<BmgmState<Boolean>>()
    val setCheckListState: SharedFlow<BmgmState<Boolean>>
        get() = _setCheckListState

    fun setCartListCheckboxState(cartIds: List<String>) {
        viewModelScope.launch {
            runCatching {
                _setCheckListState.emit(BmgmState.Loading)
                val result = withContext(dispatchers.io) {
                    setCartListCheckboxStateUseCase.invoke(cartIds)
                }
                _setCheckListState.emit(BmgmState.Success(result))
            }.onFailure {
                _setCheckListState.emit(BmgmState.Error(it))
            }
        }
    }
}