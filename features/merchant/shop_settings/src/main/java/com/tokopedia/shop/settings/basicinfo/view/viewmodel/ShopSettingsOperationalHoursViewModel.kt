package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
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

    private val _shopOperationalHoursListData = MutableLiveData<Result<List<ShopOperationalHour>>>()
    val shopOperationalHoursListData: LiveData<Result<List<ShopOperationalHour>>>
        get() = _shopOperationalHoursListData

    fun getShopOperationalHoursList(shopId: String) {
        launchCatchError(dispatchers.io, block =  {
            gqlGetShopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
            val shopOperationalHoursListResponse = gqlGetShopOperationalHoursListUseCase.executeOnBackground()
            val opsHourList = shopOperationalHoursListResponse.getShopOperationalHoursList?.data
            opsHourList?.let {
                if (it.isNotEmpty()) {
                    _shopOperationalHoursListData.postValue(Success(it))
                }
            }

        }) {
            _shopOperationalHoursListData.postValue(Fail(it))
        }
    }

}