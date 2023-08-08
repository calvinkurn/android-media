package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.domain.usecase.GetBmgmMiniCartDataUseCase
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartViewModel @Inject constructor(
    private val getMiniCartDataUseCase: GetBmgmMiniCartDataUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _cartData = MutableLiveData<Result<BmgmCommonDataUiModel>>()
    val cartData: LiveData<Result<BmgmCommonDataUiModel>>
        get() = _cartData

    fun getMiniCartData(param: BmgmParamModel) {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                getMiniCartDataUseCase.invoke(userSession.shopId, param)
            }
            _cartData.value = Success(data)
        }, onError = {
            _cartData.value = Fail(it)
        })
    }
}