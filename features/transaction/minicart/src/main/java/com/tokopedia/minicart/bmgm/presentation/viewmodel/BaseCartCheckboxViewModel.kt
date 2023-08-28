package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import kotlinx.coroutines.withContext

/**
 * Created by @ilhamsuaib on 28/08/23.
 */

open class BaseCartCheckboxViewModel(
    private val setCartListCheckboxStateUseCase: SetCartListCheckboxStateUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _setCheckListState = MutableLiveData<BmgmState<Boolean>>()
    val setCheckListState: LiveData<BmgmState<Boolean>>
        get() = _setCheckListState

    fun setCartListCheckboxState(cartIds: List<String>) {
        launchCatchError(block = {
            _setCheckListState.value = BmgmState.Loading
            val result = withContext(dispatchers.io) {
                setCartListCheckboxStateUseCase.invoke(cartIds)
            }
            _setCheckListState.value = BmgmState.Success(result)
        }, onError = {
            _setCheckListState.value = BmgmState.Error(it)
        })
    }
}