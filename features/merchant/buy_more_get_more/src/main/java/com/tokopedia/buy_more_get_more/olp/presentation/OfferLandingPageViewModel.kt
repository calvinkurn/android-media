package com.tokopedia.buy_more_get_more.olp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.buy_more_get_more.olp.data.request.GetOfferingInfoForBuyerRequestParam
import com.tokopedia.buy_more_get_more.olp.data.request.GetOfferingInfoForBuyerRequestParam.UserLocation
import com.tokopedia.buy_more_get_more.olp.data.request.GetOfferingProductListRequestParam
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferProductListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class OfferLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase,
    private val getOfferProductListUseCase: GetOfferProductListUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : BaseViewModel(dispatchers.main) {

    private val _offeringInfo = MutableLiveData<OfferInfoForBuyerUiModel>()
    val offeringInfo: LiveData<OfferInfoForBuyerUiModel>
        get() = _offeringInfo

    private val _productList = MutableLiveData<OfferProductListUiModel>()
    val productList: LiveData<OfferProductListUiModel>
        get() = _productList

    private val _navNotificationModel = MutableLiveData(TopNavNotificationModel())
    val navNotificationLiveData: LiveData<TopNavNotificationModel>
        get() = _navNotificationModel

    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun getOfferingInfo(
        offerIds: List<Int>,
        shopId: String,
        productIds: List<Int>? = emptyList(),
        warehouseIds: List<Int>? = emptyList(),
        localCacheModel: LocalCacheModel?
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferingInfoForBuyerRequestParam(
                    offerIds = offerIds,
                    shopIds = listOf(shopId.toIntOrZero()),
                    productAnchor = GetOfferingInfoForBuyerRequestParam.ProductAnchor(
                        productIds.orEmpty(),
                        warehouseIds.orEmpty()
                    ),
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

    fun getOfferingProductList(
        offerIds: List<Int>,
        warehouseIds: List<Int>? = emptyList(),
        localCacheModel: LocalCacheModel?,
        page: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferingProductListRequestParam(
                    offerIds = offerIds,
                    productAnchor = GetOfferingProductListRequestParam.ProductAnchor(
                        warehouseIds.orEmpty()
                    ),
                    userLocation = GetOfferingProductListRequestParam.UserLocation(
                        addressId = localCacheModel?.address_id.toIntOrZero(),
                        districtId = localCacheModel?.district_id.toIntOrZero(),
                        postalCode = localCacheModel?.postal_code.orEmpty(),
                        latitude = localCacheModel?.lat.orEmpty(),
                        longitude = localCacheModel?.long.orEmpty(),
                        cityId = localCacheModel?.city_id.toIntOrZero()
                    ),
                    page = page,
                    pageSize = 5
                )

                val result = getOfferProductListUseCase.execute(param)
                _productList.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun getNotification() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getNotificationUseCase.executeOnBackground()
                _navNotificationModel.postValue(result)
            },
            onError = {

            }
        )
    }

    fun addToCart(
        product: OfferProductListUiModel.Product,
        shopId: String
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = AddToCartUseCase.getMinimumParams(
                    productId = product.productId.toString(),
                    shopId = shopId,
                )
                addToCartUseCase.setParams(param)
                val result = addToCartUseCase.executeOnBackground()
                _miniCartAdd.postValue(Success(result))
            },
            onError = {
                _miniCartAdd.postValue(Fail(it))
            }
        )
    }
}
