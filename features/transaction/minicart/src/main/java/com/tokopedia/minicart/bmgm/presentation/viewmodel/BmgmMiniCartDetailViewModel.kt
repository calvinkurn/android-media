package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 21/08/23.
 */

class BmgmMiniCartDetailViewModel @Inject constructor(
    private val setCartListCheckboxStateUseCase: Lazy<SetCartListCheckboxStateUseCase>,
    private val dispatchers: Lazy<CoroutineDispatchers>
) : BaseViewModel(dispatchers.get().main) {

    private val _setCheckListState = MutableLiveData<BmgmState<Boolean>>()
    val setCheckListState: LiveData<BmgmState<Boolean>>
        get() = _setCheckListState

    fun setCartListCheckboxState(cartIds: List<String>) {
        launchCatchError(block = {
            _setCheckListState.value = BmgmState.Loading
            val result = withContext(dispatchers.get().io) {
                setCartListCheckboxStateUseCase.get().invoke(cartIds)
            }
            _setCheckListState.value = BmgmState.Success(result)
        }, onError = {
            _setCheckListState.value = BmgmState.Error(it)
        })
    }
}