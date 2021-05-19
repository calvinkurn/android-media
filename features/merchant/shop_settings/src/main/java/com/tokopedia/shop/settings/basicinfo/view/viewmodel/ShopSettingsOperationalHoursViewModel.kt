package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.basicinfo.view.model.ShopOperationalHourUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Rafli Syam on 12/05/2021
 */
class ShopSettingsOperationalHoursViewModel @Inject constructor(
        private val gqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopOperationalHoursListData = MutableLiveData<Result<List<ShopOperationalHourUiModel>>>()
    val shopOperationalHoursListData: LiveData<Result<List<ShopOperationalHourUiModel>>>
        get() = _shopOperationalHoursListData

    fun getShopOperationalHoursList(shopId: String) {
        launchCatchError(dispatchers.io, block =  {
            gqlGetShopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
            val shopOperationalHoursListResponse = gqlGetShopOperationalHoursListUseCase.executeOnBackground()
            val opsHourList = shopOperationalHoursListResponse.getShopOperationalHoursList?.data?.let {
                mapToShopOperationalHoursUiModel(it)
            }
            opsHourList?.let {
                if (it.isNotEmpty()) {
                    _shopOperationalHoursListData.postValue(Success(it))
                }
            }

        }) {
            _shopOperationalHoursListData.postValue(Fail(it))
        }
    }

    private fun mapToShopOperationalHoursUiModel(
            opsHourList: List<ShopOperationalHour>
    ): List<ShopOperationalHourUiModel> {
        return opsHourList.map {
            ShopOperationalHourUiModel(
                    dayName = OperationalHoursUtil.getDayName(it.day),
                    // will not include "Jam" so it start from 4th index
                    operationalHours = OperationalHoursUtil.generateDatetime(it.startTime, it.endTime).substring(4)
            )
        }
    }

}