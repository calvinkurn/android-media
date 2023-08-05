package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.bmgm.presentation.model.BaseMiniCartUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _cartData = MutableLiveData<BaseMiniCartUiModel>()
    val cartData: LiveData<BaseMiniCartUiModel>
        get() = _cartData

    fun getCartData() {
        launchCatchError(block = {

        }, onError = {

        })
    }
}