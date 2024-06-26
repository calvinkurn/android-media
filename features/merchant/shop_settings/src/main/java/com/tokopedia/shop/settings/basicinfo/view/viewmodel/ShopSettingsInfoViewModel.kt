package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseShopSettingsInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetIsShopOsUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ShopSettingsInfoViewModel @Inject constructor (
        private val checkOsMerchantUseCase: GqlGetIsShopOsUseCase,
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val getShopStatusUseCase: GetPMStatusUseCase,
        private val shopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase,
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

    private val _shopOperationalHourList = MutableLiveData<Result<ShopOperationalHoursListResponse>>()
    val shopOperationalHourList: LiveData<Result<ShopOperationalHoursListResponse>>
        get() = _shopOperationalHourList

    private val _shopStatusData = MutableLiveData<Result<PMStatusUiModel>>()
    val shopStatusData: LiveData<Result<PMStatusUiModel>>
        get() = _shopStatusData

    private val _shopInfoData = MutableLiveData<Result<ShopInfo>>()
    val shopInfoData: LiveData<Result<ShopInfo>>
        get() = _shopInfoData

    private val _updateScheduleResult = MutableLiveData<Result<String>>()
    val updateScheduleResult: LiveData<Result<String>>
        get() = _updateScheduleResult

    fun resetAllLiveData() {
        _checkOsMerchantTypeData.value = null
        _shopBasicData.value = null
        _shopStatusData.value = null
        _updateScheduleResult.value = null
        _shopInfoData.value = null
        _shopOperationalHourList.value = null
    }

    fun getShopData(shopId: String, includeOS: Boolean) {
        launchCatchError(block = {
            val shopBasicData = asyncCatchError(
                dispatchers.io,
                block = {
                    getShopBasicData()
                },
                onError = {
                    _shopBasicData.postValue(Fail(it))
                    null
                }
            )

            val shopInfoData = asyncCatchError(
                dispatchers.io,
                block = {
                    getShopInfo(shopId)
                },
                onError = {
                    _shopInfoData.postValue(Fail(it))
                    null
                }
            )

            val shopStatusData = asyncCatchError(
                dispatchers.io,
                block = {
                    getShopStatus(shopId, includeOS)
                },
                onError = {
                    _shopStatusData.postValue(Fail(it))
                    null
                }
            )

            shopBasicData.await()?.let { basicData ->
                _shopBasicData.postValue(Success(basicData))
                shopInfoData.await()?.let { shopInfoData ->
                    _shopInfoData.postValue(Success(shopInfoData))
                    shopStatusData.await()?.let { shopStatusData ->
                        _shopStatusData.postValue(Success(shopStatusData))
                    }
                }
            }
        }) {}
    }

    private suspend fun getShopInfo(shopId: String): ShopInfo {
        getShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(
            listOf(shopId.toIntOrZero()),
            "",
            source = GQLGetShopInfoUseCase.SHOP_PAGE_SOURCE,
            fields = listOf(
                    GQLGetShopInfoUseCase.FIELD_CLOSED_INFO,
                    GQLGetShopInfoUseCase.FIELD_OTHER_GOLD_OS
            )
        )
        getShopInfoUseCase.isFromCacheFirst = false
        return getShopInfoUseCase.executeOnBackground()
    }

    fun getOperationalHoursList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                shopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
                val response = shopOperationalHoursListUseCase.executeOnBackground()
                _shopOperationalHourList.postValue(Success(response))
            }
        }) {
            _shopOperationalHourList.postValue(Fail(it))
        }
    }

    fun updateShopSchedule(
            @ShopScheduleActionDef action: Int,
            closeNow: Boolean,
            closeStart: String,
            closeEnd: String,
            closeNote: String
    ) {
        launchCatchError(
            context = dispatchers.io,
            block = {
                val requestParams = UpdateShopScheduleUseCase.createRequestParams(
                        action = action,
                        closeNow = closeNow,
                        closeStart = closeStart,
                        closeEnd = closeEnd,
                        closeNote = closeNote
                )
                val updateScheduleResponse: String = updateShopScheduleUseCase.getData(requestParams)
                _updateScheduleResult.postValue(Success(updateScheduleResponse))
            },
            onError = {
                _updateScheduleResult.postValue(Fail(it))
            })
    }

    private fun getShopBasicData(): ShopBasicDataModel {
        return getShopBasicDataUseCase.getData(RequestParams.EMPTY)
    }

    private suspend fun getShopStatus(shopId: String, includeOS: Boolean): PMStatusUiModel {
        getShopStatusUseCase.params = GetPMStatusUseCase.createParams(shopId, includeOS)
        return getShopStatusUseCase.executeOnBackground()
    }

    fun validateOsMerchantType(shopId: Int) {
        launchCatchError(
            context = dispatchers.io,
            block = {
                checkOsMerchantUseCase.params = GqlGetIsShopOsUseCase.createParams(shopId)
                val osMerchantChecker = checkOsMerchantUseCase.executeOnBackground()
                osMerchantChecker.let {
                    _checkOsMerchantTypeData.postValue(Success(it))
                }
            }, onError = {
                _checkOsMerchantTypeData.postValue(Fail(it))
            }
        )
    }
}
