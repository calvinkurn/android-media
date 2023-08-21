package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.domain.usecase.GetBmgmMiniCartDataUseCase
import com.tokopedia.minicart.bmgm.domain.usecase.LocalCacheUseCase
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartViewModel @Inject constructor(
    private val getMiniCartDataUseCase: Lazy<GetBmgmMiniCartDataUseCase>,
    private val localCacheUseCase: Lazy<LocalCacheUseCase>,
    private val setCartListCheckboxStateUseCase: Lazy<SetCartListCheckboxStateUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _cartData = MutableLiveData<BmgmState<BmgmMiniCartDataUiModel>>()
    val cartData: LiveData<BmgmState<BmgmMiniCartDataUiModel>>
        get() = _cartData

    private val _setCheckListState = MutableLiveData<BmgmState<Boolean>>()
    val setCheckListState: LiveData<BmgmState<Boolean>>
        get() = _setCheckListState

    fun getMiniCartData(param: BmgmParamModel) {
        launchCatchError(block = {
            _cartData.value = BmgmState.Loading
            val data = withContext(dispatchers.io) {
                getMiniCartDataUseCase.get().invoke(userSession.get().shopId, param)
            }
            _cartData.value = BmgmState.Success(data)
        }, onError = {
            _cartData.value = BmgmState.Error(it)
        })
    }

    fun clearCartDataLocalCache() = launch {
        localCacheUseCase.get().clearLocalCache()
    }

    fun setCartListCheckboxState(cartIds: List<String>) {
        launchCatchError(block = {
            _setCheckListState.value = BmgmState.Loading
            storeCartDataToLocalCache()
            val result = withContext(dispatchers.io) {
                setCartListCheckboxStateUseCase.get().invoke(cartIds)
            }
            _setCheckListState.value = BmgmState.Success(result)
        }, onError = {
            _setCheckListState.value = BmgmState.Error(it)
        })
    }

    private fun storeCartDataToLocalCache() = launch {
        val result = _cartData.value
        if (result is BmgmState.Success) {
            localCacheUseCase.get().saveToLocalCache(result.data)
        }
    }
}