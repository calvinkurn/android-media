package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsPowerMerchantModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.shop.settings.basicinfo.domain.CheckPowerMerchantTypeUseCase
import com.tokopedia.shop.settings.common.util.ShopSettingDispatcherProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShopSettingsInfoViewModel @Inject constructor (
        private val checkOsMerchantUseCase: CheckOfficialStoreTypeUseCase,
        private val checkPowerMerchantUseCase: CheckPowerMerchantTypeUseCase,
        private val dispatchers: ShopSettingDispatcherProvider
): BaseViewModel(dispatchers.ui()) {

    private val _checkOsMerchantType = MutableLiveData<Result<CheckShopIsOfficialModel>>()
    val checkOsMerchantType: LiveData<Result<CheckShopIsOfficialModel>>
        get() = _checkOsMerchantType

    private val _checkPowerMerchantType = MutableLiveData<Result<CheckShopIsPowerMerchantModel>>()
    val checkPowerMerchantType: LiveData<Result<CheckShopIsPowerMerchantModel>>
        get() = _checkPowerMerchantType



    fun validateOsMerchantType() {
        launchCatchError(block = {
            withContext(dispatchers.io()) {
                checkOsMerchantUseCase.params = CheckOfficialStoreTypeUseCase
                        .createRequestParam(shopId = 67726)
                val osMerchantChecker = checkOsMerchantUseCase.executeOnBackground()
                osMerchantChecker.let {
                    _checkOsMerchantType.postValue(Success(it))
                }
            }
        }) {
            _checkOsMerchantType.value = Fail(it)
        }
    }

    fun validatePowerMerchantType() {
        launchCatchError(block = {
            withContext(dispatchers.io()) {
                checkPowerMerchantUseCase.params = CheckPowerMerchantTypeUseCase
                        .createRequestParam(shopID = 67726, includeOS = false)
                val osMerchantChecker = checkPowerMerchantUseCase.executeOnBackground()
                osMerchantChecker.let {
                    _checkPowerMerchantType.postValue(Success(it))
                }
            }
        }) {
            _checkPowerMerchantType.value = Fail(it)
        }
    }
}