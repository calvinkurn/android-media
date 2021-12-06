package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.di.GqlGetShopCloseDetailInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.settings.basicinfo.view.model.ShopSettingsOperationalHoursListUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Rafli Syam on 12/05/2021
 */
class ShopSettingsOperationalHoursViewModel @Inject constructor(
        @GqlGetShopCloseDetailInfoQualifier
        private val getShopCloseDetailInfoUseCase: Lazy<GQLGetShopInfoUseCase>,
        private val gqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase,
        private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopSettingsOperationalHoursListUiModel = MutableLiveData<Result<ShopSettingsOperationalHoursListUiModel>>()
    val shopSettingsOperationalHoursListUiModel: LiveData<Result<ShopSettingsOperationalHoursListUiModel>>
        get() = _shopSettingsOperationalHoursListUiModel

    private val _shopInfoCloseSchedule = MutableLiveData<Result<String>>()
    val shopInfoCloseSchedule: LiveData<Result<String>>
        get() = _shopInfoCloseSchedule

    private val _shopInfoAbortSchedule = MutableLiveData<Result<String>>()
    val shopInfoAbortSchedule: LiveData<Result<String>>
        get() = _shopInfoAbortSchedule

    fun getShopOperationalHoursInitialData(shopId: String) {
        val shopSettingsOperationalHoursListUiModel = ShopSettingsOperationalHoursListUiModel()
        launchCatchError(dispatchers.io, block = {

            // get shop info
            val getShopInfo = asyncCatchError(dispatchers.io, block = { executeGetShopCloseInfo(shopId) }) {
                _shopSettingsOperationalHoursListUiModel.postValue(Fail(it))
                null
            }

            getShopInfo.await()?.let { shopInfo ->
                // getShopOperationalHoursList after get shop info finished
                val opsHourList = executeGetShopOperationalHoursList(shopId)
                opsHourList?.let { list ->
                    shopSettingsOperationalHoursListUiModel.operationalHourList = list
                    shopSettingsOperationalHoursListUiModel.closeInfo = shopInfo.closedInfo
                    shopSettingsOperationalHoursListUiModel.statusInfo = shopInfo.statusInfo
                    _shopSettingsOperationalHoursListUiModel.postValue(Success(shopSettingsOperationalHoursListUiModel))
                }
            }

        }) {
            _shopSettingsOperationalHoursListUiModel.postValue(Fail(it))
        }
    }

    fun setShopCloseSchedule(
            @ShopScheduleActionDef action: Int,
            closeNow: Boolean = false,
            closeStart: String = "",
            closeEnd: String = "",
            closeNote: String = ""
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
            when (action) {
                ShopScheduleActionDef.CLOSED -> _shopInfoCloseSchedule.postValue(Success(updateScheduleResponse))
                ShopScheduleActionDef.ABORT -> _shopInfoAbortSchedule.postValue(Success(updateScheduleResponse))
            }
        }) {
            when (action) {
                ShopScheduleActionDef.CLOSED -> _shopInfoCloseSchedule.postValue(Fail(it))
                ShopScheduleActionDef.ABORT -> _shopInfoAbortSchedule.postValue(Fail(it))
            }
        }
    }

    private suspend fun executeGetShopCloseInfo(shopId: String): ShopInfo? {
        val shopCloseInfoUseCase = getShopCloseDetailInfoUseCase.get()
        shopCloseInfoUseCase.isFromCacheFirst = false
        shopCloseInfoUseCase.params = GQLGetShopInfoUseCase.createParams(
                shopIds = listOf(shopId.toIntOrZero()),
                fields = listOf(GQLGetShopInfoUseCase.FIELD_CLOSED_INFO, GQLGetShopInfoUseCase.FIELD_STATUS),
        )
        return shopCloseInfoUseCase.executeOnBackground()
    }

    private suspend fun executeGetShopOperationalHoursList(shopId: String): List<ShopOperationalHour>? {
        gqlGetShopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
        val shopOperationalHoursListResponse = gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        return shopOperationalHoursListResponse.getShopOperationalHoursList?.data
    }

}