package com.tokopedia.shop.settings.basicinfo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.settings.basicinfo.data.SetShopOperationalHours
import com.tokopedia.shop.settings.basicinfo.domain.SetShopOperationalHoursUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Rafli Syam on 03/04/2021
 */
class ShopSetOperationalHoursViewModel @Inject constructor(
        private val shopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase,
        private val setShopOperationalHoursUseCase: SetShopOperationalHoursUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _shopOperationalHoursListData = MutableLiveData<Result<ShopOperationalHoursListResponse>>()
    val shopOperationalHoursListData: LiveData<Result<ShopOperationalHoursListResponse>>
        get() = _shopOperationalHoursListData

    private val _setShopOperationalHoursData = MutableLiveData<Result<SetShopOperationalHours>>()
    val setShopOperationalHoursData: LiveData<Result<SetShopOperationalHours>>
        get() = _setShopOperationalHoursData

    fun getOperationalHoursList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                shopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
                val response = shopOperationalHoursListUseCase.executeOnBackground()
                _shopOperationalHoursListData.postValue(Success(response))
            }
        }) {
            _shopOperationalHoursListData.postValue(Fail(it))
        }
    }

    fun updateOperationalHoursList(shopId: String, newOpsHourList: List<ShopOperationalHour>) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                setShopOperationalHoursUseCase.params = SetShopOperationalHoursUseCase.createRequestParams(shopId, newOpsHourList)
                val response = setShopOperationalHoursUseCase.executeOnBackground()
                _setShopOperationalHoursData.postValue(Success(response.setShopOperationalHours))
            }
        }) {
            _setShopOperationalHoursData.postValue(Fail(it))
        }
    }

}