package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.di.GqlGetShopCloseDetailInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
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
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopSettingsOperationalHoursListUiModel = MutableLiveData<Result<ShopSettingsOperationalHoursListUiModel>>()
    val shopSettingsOperationalHoursListUiModel: LiveData<Result<ShopSettingsOperationalHoursListUiModel>>
        get() = _shopSettingsOperationalHoursListUiModel

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
                    if (list.isNotEmpty()) {
                        shopSettingsOperationalHoursListUiModel.operationalHourList = list
                        shopSettingsOperationalHoursListUiModel.closeInfo = shopInfo.closedInfo
                        _shopSettingsOperationalHoursListUiModel.postValue(Success(shopSettingsOperationalHoursListUiModel))
                    }
                }
            }

        }) {
            _shopSettingsOperationalHoursListUiModel.postValue(Fail(it))
        }
    }

    private suspend fun executeGetShopCloseInfo(shopId: String): ShopInfo? {
        val shopCloseInfoUseCase = getShopCloseDetailInfoUseCase.get()
        shopCloseInfoUseCase.isFromCacheFirst = false
        shopCloseInfoUseCase.params = GQLGetShopInfoUseCase.createParams(
                shopIds = listOf(shopId.toIntOrZero()),
                fields = listOf(GQLGetShopInfoUseCase.FIELD_CLOSED_INFO),
        )
        return shopCloseInfoUseCase.executeOnBackground()
    }

    private suspend fun executeGetShopOperationalHoursList(shopId: String): List<ShopOperationalHour>? {
        gqlGetShopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
        val shopOperationalHoursListResponse = gqlGetShopOperationalHoursListUseCase.executeOnBackground()
        return shopOperationalHoursListResponse.getShopOperationalHoursList?.data
    }

}