package com.tokopedia.seller.menu.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.domain.usecase.ShopMultilocWhitelistUseCase
import com.tokopedia.seller.menu.domain.usecase.GetShopScoreLevelUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerSettingViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val shopLocWhitelist: ShopMultilocWhitelistUseCase,
) : BaseViewModel(dispatchers.main) {

    val shopLocEligible: LiveData<Result<Boolean>>
        get() = _shopLocEligible

    private val _shopLocEligible = MutableLiveData<Result<Boolean>>()

    fun getShopLocEligible(shopId: Long) {
        launchCatchError(block = {
            val shopLocWhitelist = shopLocWhitelist.invoke(shopId).shopLocWhitelist
            val eligibilityState = shopLocWhitelist.data.eligibilityState
            val isMultilocation = eligibilityState==1
            _shopLocEligible.postValue(Success(isMultilocation))
        }, onError = {
            _shopLocEligible.postValue(Fail(it))
        })
    }
}