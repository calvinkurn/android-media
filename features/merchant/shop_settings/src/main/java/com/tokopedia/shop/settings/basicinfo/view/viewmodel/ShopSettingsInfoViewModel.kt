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
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseShopSettingsInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShopSettingsInfoViewModel @Inject constructor (
        private val checkOsMerchantUseCase: CheckOfficialStoreTypeUseCase,
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val getShopStatusUseCase: GetShopStatusUseCase,
        private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
        @GqlGetShopInfoUseCaseShopSettingsInfoQualifier
        private val getShopInfoUseCase: GQLGetShopInfoUseCase,
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

    private val _shopBadgeData = MutableLiveData<Result<String>>()
    val shopBadgeData: LiveData<Result<String>>
        get() = _shopBadgeData

    private val _updateScheduleResult = MutableLiveData<Result<String>>()
    val updateScheduleResult: LiveData<Result<String>>
        get() = _updateScheduleResult

    fun resetAllLiveData() {
        _checkOsMerchantTypeData.value = null
        _shopBasicData.value = null
        _shopStatusData.value = null
        _updateScheduleResult.value = null
        _shopBadgeData.value = null
    }

    fun getShopData(shopId: String, includeOS: Boolean) {
        launchCatchError(dispatchers.io, block = {
            _shopBadgeData.postValue(Success(getShopBadgeData(shopId).await().orEmpty()))
            _shopBasicData.postValue(Success(getShopBasicDataAsync().await()))
            _shopStatusData.postValue(Success(getShopStatusAsync(shopId, includeOS).await()))
        }, onError = {})
    }

    private fun getShopBadgeData(shopId: String): Deferred<String?> {
        return asyncCatchError(
                dispatchers.io,
                block = {
                    getShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(
                            listOf(shopId.toIntOrZero()),
                            "",
                            source = GQLGetShopInfoUseCase.SHOP_PAGE_SOURCE,
                            fields = listOf(
                                    GQLGetShopInfoUseCase.FIELD_OTHER_GOLD_OS
                            )
                    )
                    getShopInfoUseCase.executeOnBackground().goldOS.badge
                },
                onError = {
                    null
                })
    }

    fun updateShopSchedule(
            @ShopScheduleActionDef action: Int,
            closeNow: Boolean,
            closeStart: String,
            closeEnd: String,
            closeNote: String
    ) {
        launchCatchError(dispatchers.io, block = {
            val requestParams = UpdateShopScheduleUseCase.createRequestParams(
                    action = action,
                    closeNow = closeNow,
                    closeStart = closeStart,
                    closeEnd = closeEnd,
                    closeNote = closeNote
            )
            val updateScheduleResponse: String = updateShopScheduleUseCase.getData(requestParams)
            _updateScheduleResult.postValue(Success(updateScheduleResponse))
        }) {
            _updateScheduleResult.postValue(Fail(it))
        }
    }

    private fun getShopBasicDataAsync(): Deferred<ShopBasicDataModel> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
            var shopBasicData = ShopBasicDataModel()
            try {
                shopBasicData = getShopBasicDataUseCase.getData(RequestParams.EMPTY) // getShopBasicDataUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _shopBasicData.postValue(Fail(t))
            }
            shopBasicData
        }
    }

    private fun getShopStatusAsync(shopId: String, includeOS: Boolean): Deferred<GoldGetPmOsStatus> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
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
                        .createRequestParam(shopId)
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