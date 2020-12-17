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
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ShopSettingsInfoViewModel @Inject constructor (
        private val checkOsMerchantUseCase: CheckOfficialStoreTypeUseCase,
        private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
        private val getShopStatusUseCase: GetShopStatusUseCase,
        private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val checkOsMerchantTypeData: LiveData<Result<CheckShopIsOfficialModel>>
        get() = _checkOsMerchantTypeData
    val updateScheduleResult: LiveData<Result<String>>
        get() = _updateScheduleResult
    val shopBasicData: LiveData<Result<ShopBasicDataModel>>
        get() = _shopBasicData
    val shopStatusData: LiveData<Result<GoldGetPmOsStatus>>
        get() = _shopStatusData

    private val _checkOsMerchantTypeData = MutableLiveData<Result<CheckShopIsOfficialModel>>()
    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataModel>>()
    private val _shopStatusData = MutableLiveData<Result<GoldGetPmOsStatus>>()
    private val _updateScheduleResult = MutableLiveData<Result<String>>()

    fun getShopData(shopId: String, includeOS: Boolean) {
        launch {
            flow {
                emit(getShopBasicDataAsync().await())
            }       .flowOn(dispatchers.io)
                    .catch {
                        _shopBasicData.value = Fail(it)
                    }
                    .collectLatest {
                        _shopBasicData.value = Success(it)
                    }

            flow {
                emit(getShopStatusAsync(shopId, includeOS).await())
            }       .flowOn(dispatchers.io)
                    .catch {
                        _shopStatusData.value = Fail(it)
                    }
                    .collectLatest {
                        _shopStatusData.value = Success(it)
                    }
            }
    }

    fun updateShopSchedule(
            @ShopScheduleActionDef action: Int,
            closeNow: Boolean,
            closeStart: String,
            closeEnd: String,
            closeNote: String
    ) {
        launch {
            flow {
                val requestParams = UpdateShopScheduleUseCase.createRequestParams(
                        action = action,
                        closeNow = closeNow,
                        closeStart = closeStart,
                        closeEnd = closeEnd,
                        closeNote = closeNote
                )
                emit(updateShopScheduleUseCase.getData(requestParams))
            }       .flowOn(dispatchers.io)
                    .catch {
                        _updateScheduleResult.value = Fail(it)
                    }
                    .collectLatest {
                        _updateScheduleResult.value = Success(it)
                    }
        }
    }

    fun validateOsMerchantType(shopId: Int) {
        launch {
            flow {
                checkOsMerchantUseCase.params = CheckOfficialStoreTypeUseCase.createRequestParam(shopId)
                emit(checkOsMerchantUseCase.executeOnBackground())
            }       .flowOn(dispatchers.io)
                    .catch {
                        _checkOsMerchantTypeData.value = Fail(it)
                    }
                    .collectLatest {
                        _checkOsMerchantTypeData.value = Success(it)
                    }
        }
    }

    private fun getShopBasicDataAsync(): Deferred<ShopBasicDataModel> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
            getShopBasicDataUseCase.getData(RequestParams.EMPTY)
        }
    }

    private fun getShopStatusAsync(shopId: String, includeOS: Boolean): Deferred<GoldGetPmOsStatus> {
        return async(start = CoroutineStart.LAZY, context = dispatchers.io) {
            val requestParams = GetShopStatusUseCase.createRequestParams(shopId, includeOS)
            getShopStatusUseCase.getData(requestParams)
        }
    }
}