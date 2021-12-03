package com.tokopedia.shop_widget.operationalhour.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import javax.inject.Inject

class ShopOperationalHourBottomSheetViewModel @Inject constructor(
    private val gqlGetShopOperationalHoursListUseCase: Lazy<GqlGetShopOperationalHoursListUseCase>,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    private val _shopOperationalHoursListData =
        MutableLiveData<Result<ShopOperationalHoursListResponse>>()
    val shopOperationalHoursListData: LiveData<Result<ShopOperationalHoursListResponse>>
        get() = _shopOperationalHoursListData

    fun getShopOperationalHoursList(shopId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val useCase = gqlGetShopOperationalHoursListUseCase.get().apply {
                params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
            }
            val response = useCase.executeOnBackground()
            _shopOperationalHoursListData.postValue(Success(response))
        }) {
            _shopOperationalHoursListData.postValue(Fail(it))
        }
    }
}