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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _cartData = MutableLiveData<Result<BmgmMiniCartDataUiModel>>()
    val cartData: LiveData<Result<BmgmMiniCartDataUiModel>>
        get() = _cartData

    fun getMiniCartData(param: BmgmParamModel) {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getMiniCartDataUseCase.get().invoke(userSession.get().shopId, param)
            }
            _cartData.value = Success(data)
            storeCartDataToLocalCache()
        }, onError = {
            _cartData.value = Fail(it)
        })
    }

    fun clearCartDataLocalCache() = launch {
        localCacheUseCase.get().clearLocalCache()
    }

    private fun storeCartDataToLocalCache() = launch {
        val result = _cartData.value
        if (result is Success) {
            localCacheUseCase.get().saveToLocalCache(result.data)
        }
    }
}