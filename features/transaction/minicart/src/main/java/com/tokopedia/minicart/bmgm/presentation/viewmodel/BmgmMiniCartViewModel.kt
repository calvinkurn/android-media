package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    setCartListCheckboxStateUseCase: Lazy<SetCartListCheckboxStateUseCase>,
    private val getMiniCartDataUseCase: Lazy<GetBmgmMiniCartDataUseCase>,
    private val localCacheUseCase: Lazy<LocalCacheUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: Lazy<CoroutineDispatchers>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get()) {

    private val _cartData = MutableLiveData<BmgmState<BmgmMiniCartDataUiModel>>()
    val cartData: LiveData<BmgmState<BmgmMiniCartDataUiModel>>
        get() = _cartData

    fun getMiniCartData(param: BmgmParamModel, showLoadingState: Boolean = false) {
        launchCatchError(block = {
            if (showLoadingState) {
                _cartData.value = BmgmState.Loading
            }
            val data = withContext(dispatchers.get().io) {
                getMiniCartDataUseCase.get().invoke(userSession.get().shopId, param)
            }
            _cartData.value = BmgmState.Success(data)
            storeCartDataToLocalCache()
        }, onError = {
            _cartData.value = BmgmState.Error(it)
        })
    }

    fun clearCartDataLocalCache() {
        launch {
            localCacheUseCase.get().clearLocalCache()
        }
    }

    fun storeCartDataToLocalCache() {
        launch {
            val result = _cartData.value
            if (result is BmgmState.Success) {
                localCacheUseCase.get().saveToLocalCache(result.data)
            }
        }
    }
}