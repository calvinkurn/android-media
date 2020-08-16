package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCaseCoroutine
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataQuery
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCaseCoroutine
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsPowerMerchantModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.shop.settings.basicinfo.domain.CheckPowerMerchantTypeUseCase
import com.tokopedia.shop.settings.common.util.ShopSettingDispatcherProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShopSettingsInfoViewModel @Inject constructor (
        private val checkOsMerchantUseCase: CheckOfficialStoreTypeUseCase,
        private val checkPowerMerchantUseCase: CheckPowerMerchantTypeUseCase,
        private val getShopBasicDataUseCase: GetShopBasicDataUseCaseCoroutine,
        private val getShopStatusUseCase: GetShopStatusUseCaseCoroutine,
        private val dispatchers: ShopSettingDispatcherProvider
): BaseViewModel(dispatchers.ui()) {

    private val _checkOsMerchantType = MutableLiveData<Result<CheckShopIsOfficialModel>>()
    val checkOsMerchantType: LiveData<Result<CheckShopIsOfficialModel>>
        get() = _checkOsMerchantType

    private val _checkPowerMerchantType = MutableLiveData<Result<CheckShopIsPowerMerchantModel>>()
    val checkPowerMerchantType: LiveData<Result<CheckShopIsPowerMerchantModel>>
        get() = _checkPowerMerchantType

    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataQuery>>()
    val shopBasicData: LiveData<Result<ShopBasicDataQuery>>
        get() = _shopBasicData

    private val _shopStatusData = MutableLiveData<Result<GoldGetPmOsStatus>>()
    val shopStatusData: LiveData<Result<GoldGetPmOsStatus>>
        get() = _shopStatusData



    fun validateOsMerchantType(shopId: Int) {
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

    fun validatePowerMerchantType(shopId: Int) {
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

    fun getShopData(shopId: Int, includeOS: Boolean) {
        launchCatchError(block = {
            _shopBasicData.postValue(Success(getShopBasicData().await()))
            _shopStatusData.postValue(Success(getShopStatus(shopId, includeOS).await()))
        }, onError = {})
    }

    private fun getShopBasicData(): Deferred<ShopBasicDataQuery> {
        return async(dispatchers.io()) {
            var shopBasicData = ShopBasicDataQuery()
            try {
                shopBasicData = getShopBasicDataUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _shopBasicData.postValue(Fail(t))
            }
            shopBasicData
        }
    }

    private fun getShopStatus(shopId: Int, includeOS: Boolean): Deferred<GoldGetPmOsStatus> {
        return async(dispatchers.io()) {
            var shopStatusData = GoldGetPmOsStatus()
            try {
                getShopStatusUseCase.params = GetShopStatusUseCaseCoroutine
                        .createRequestParam(shopId, includeOS)
                shopStatusData = getShopStatusUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _shopStatusData.postValue(Fail(t))
            }
            shopStatusData
        }
    }
}