package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseShopSettingsInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopOperationalHourStatusUseCase
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetIsShopOsUseCase
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
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
        private val checkOsMerchantUseCase: GqlGetIsShopOsUseCase,
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val getShopStatusUseCase: GetPMStatusUseCase,
        private val getShopOperationalHourStatusUseCase: GQLGetShopOperationalHourStatusUseCase,
        private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
        @GqlGetShopInfoUseCaseShopSettingsInfoQualifier
        private val getShopInfoUseCase: GQLGetShopInfoUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _checkOsMerchantTypeData = MutableLiveData<Result<GetIsShopOfficialStore>>()
    val checkOsMerchantTypeData: LiveData<Result<GetIsShopOfficialStore>>
        get() = _checkOsMerchantTypeData

    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataModel>>()
    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData

    private val _shopOperationalHourStatus = MutableLiveData<Result<ShopOperationalHourStatus>>()
    val shopOperationalHourStatus: LiveData<Result<ShopOperationalHourStatus>>
        get() = _shopOperationalHourStatus

    private val _shopStatusData = MutableLiveData<Result<PMStatusUiModel>>()
    val shopStatusData: LiveData<Result<PMStatusUiModel>>
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
        _shopOperationalHourStatus.value = null
    }

    fun getShopData(shopId: String, includeOS: Boolean) {
        launchCatchError(dispatchers.io, block = {
            _shopBadgeData.postValue(Success(getShopBadgeData(shopId).await().orEmpty()))
            _shopBasicData.postValue(Success(getShopBasicDataAsync().await()))
            _shopStatusData.postValue(Success(getShopStatusAsync(shopId, includeOS).await()))
            _shopOperationalHourStatus.postValue(Success(getShopOperationalHourStatusAsync(shopId).await()))
        }, onError = {})
    }

    private fun getShopOperationalHourStatusAsync(shopId: String): Deferred<ShopOperationalHourStatus> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
            var operationalHourStatus = ShopOperationalHourStatus()
            try {
                getShopOperationalHourStatusUseCase.params = GQLGetShopOperationalHourStatusUseCase.createParams(shopIds = shopId)
                operationalHourStatus = getShopOperationalHourStatusUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _shopOperationalHourStatus.postValue(Fail(t))
            }
            operationalHourStatus
        }
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

    private fun getShopStatusAsync(shopId: String, includeOS: Boolean): Deferred<PMStatusUiModel> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
            return@async try {
                getShopStatusUseCase.params = GetPMStatusUseCase.createParams(shopId, includeOS)
                getShopStatusUseCase.executeOnBackground()
            } catch (t: Throwable) {
                _shopStatusData.postValue(Fail(t))
                PMStatusUiModel()
            }
        }
    }

    fun validateOsMerchantType(shopId: Int) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                checkOsMerchantUseCase.params = GqlGetIsShopOsUseCase.createParams(shopId)
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