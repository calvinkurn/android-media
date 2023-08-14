package com.tokopedia.buy_more_get_more.olp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.olp.data.request.GetOfferingInfoForBuyerRequestParam
import com.tokopedia.buy_more_get_more.olp.data.request.GetOfferingInfoForBuyerRequestParam.UserLocation
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import javax.inject.Inject

class OfferLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase
) : BaseViewModel(dispatchers.main) {

    private val _offeringInfo = MutableLiveData<OfferInfoForBuyerUiModel>()
    val offeringInfo: LiveData<OfferInfoForBuyerUiModel>
        get() = _offeringInfo

    private val _productList = MutableLiveData<OfferProductListUiModel>()
    val productList: LiveData<OfferProductListUiModel>
        get() = _productList

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun getOfferingIndo(offerIds: List<Int>, shopId: String, localCacheModel: LocalCacheModel?) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferingInfoForBuyerRequestParam(
                    offerIds = offerIds,
                    shopIds = listOf(shopId.toIntOrZero()),
                    userLocation = UserLocation(
                        addressId = localCacheModel?.address_id.toIntOrZero(),
                        districtId = localCacheModel?.district_id.toIntOrZero(),
                        postalCode = localCacheModel?.postal_code.orEmpty(),
                        latitude = localCacheModel?.lat.orEmpty(),
                        longitude = localCacheModel?.long.orEmpty(),
                        cityId = localCacheModel?.city_id.toIntOrZero()
                    )
                )
                val result = getOfferInfoForBuyerUseCase.execute(param)
                _offeringInfo.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
