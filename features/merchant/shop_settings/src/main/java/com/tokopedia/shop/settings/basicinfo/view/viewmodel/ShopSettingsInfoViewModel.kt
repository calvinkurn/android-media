package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.shop.settings.common.coroutine.CoroutineDispatchers
import com.tokopedia.usecase.RequestParams
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
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val getShopStatusUseCase: GetShopStatusUseCase,
        private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _checkOsMerchantTypeData = MutableLiveData<Result<CheckShopIsOfficialModel>>()
    val checkOsMerchantTypeData: LiveData<Result<CheckShopIsOfficialModel>>
        get() = _checkOsMerchantTypeData

    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataModel>>()
    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData

    private val _shopStatusData = MutableLiveData<Result<GoldGetPmOsStatus>>()
    val shopStatusData: LiveData<Result<GoldGetPmOsStatus>>
        get() = _shopStatusData

    private val _updateScheduleResult = MutableLiveData<Result<String>>()
    val updateScheduleResult: LiveData<Result<String>>
        get() = _updateScheduleResult


    fun detachView() {
        getShopBasicDataUseCase.unsubscribe()
        getShopStatusUseCase.unsubscribe()
        updateShopScheduleUseCase.unsubscribe()
    }

    fun getShopData(shopId: String, includeOS: Boolean) {
        launchCatchError(block = {
            _shopBasicData.postValue(Success(getShopBasicData().await()))
            _shopStatusData.postValue(Success(getShopStatus(shopId, includeOS).await()))
        }, onError = {})
    }

    fun updateShopSchedule(
            @ShopScheduleActionDef action: Int,
            closeNow: Boolean,
            closeStart: String,
            closeEnd: String,
            closeNote: String
    ) {
        updateShopScheduleUseCase.unsubscribe()

        launchCatchError(block = {
            val updateScheduleResponse: String = withContext(dispatchers.io) {
                val requestParams = UpdateShopScheduleUseCase.createRequestParams(
                        action = action,
                        closeNow = closeNow,
                        closeStart = closeStart,
                        closeEnd = closeEnd,
                        closeNote = closeNote
                )
                updateShopScheduleUseCase.getData(requestParams)
            }
            _updateScheduleResult.postValue(Success(updateScheduleResponse))
        }) {
            _updateScheduleResult.postValue(Fail(it))
        }
    }

    private fun getShopBasicData(): Deferred<ShopBasicDataModel> {
        return async(dispatchers.io) {
            var shopBasicData = ShopBasicDataModel()
            try {
                shopBasicData = getShopBasicDataUseCase.getData(RequestParams.EMPTY) // getShopBasicDataUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _shopBasicData.postValue(Fail(t))
            }
            shopBasicData
        }
    }

    private fun getShopStatus(shopId: String, includeOS: Boolean): Deferred<GoldGetPmOsStatus> {
        return async(dispatchers.io) {
            var shopStatusData = GoldGetPmOsStatus()
            try {
                val requestParams = GetShopStatusUseCase.createRequestParams(shopId, includeOS)
                shopStatusData = getShopStatusUseCase.getData(requestParams)
            } catch (t: Throwable) {
                _shopStatusData.postValue(Fail(t))
            }
            shopStatusData
        }
    }

    fun validateOsMerchantType(shopId: Int) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                checkOsMerchantUseCase.params = CheckOfficialStoreTypeUseCase
                        .createRequestParam(shopId = 67726)
                val osMerchantChecker = checkOsMerchantUseCase.executeOnBackground()
                osMerchantChecker.let {
                    _checkOsMerchantTypeData.postValue(Success(it))
                }
            }
        }) {
            _checkOsMerchantTypeData.value = Fail(it)
        }
    }
}