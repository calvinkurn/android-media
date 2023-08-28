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
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.*
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.ShopData
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferProductListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class OfferLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase,
    private val getOfferProductListUseCase: GetOfferProductListUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(OlpUiState())
    val uiState = _uiState.asStateFlow()

    val currentState: OlpUiState
        get() = _uiState.value

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

    private val userId: String
        get() = userSession.userId


    fun processEvent(event: OlpEvent) {
        when (event) {
            is OlpEvent.SetInitialUiState -> {
                setInitialUiState(
                    offerIds = event.offerIds,
                    shopId = event.shopIds,
                    productIds = event.productIds,
                    warehouseIds = event.warehouseIds,
                    localCacheModel = event.localCacheModel,
                )
            }

            is OlpEvent.GetOfferingInfo -> {
                getOfferingInfo()
            }

            is OlpEvent.GetOffreringProductList -> {
                getOfferingProductList(page = event.page, pageSize = event.pageSize)
            }

            is OlpEvent.SetSort -> {
                setSort(event.sortId, event.sortName)
            }

            is OlpEvent.GetNotification -> {
                getNotification()
            }

            is OlpEvent.AddToCart -> {
                addToCart(event.product)
            }

            is OlpEvent.SetWarehouseIds -> setWarehouseIds(event.warehouseIds)
            is OlpEvent.SetShopData -> setShopData(event.shopData)
            is OlpEvent.SetOfferingJsonData -> setOfferingJsonData(event.offeringJsonData)
            is OlpEvent.SetTncData -> {
                setTncData(event.tnc)
            }
        }
    }

    private fun setInitialUiState(
        offerIds: List<Long>,
        shopId: Long,
        productIds: List<Long> = emptyList(),
        warehouseIds: List<Long> = emptyList(),
        localCacheModel: LocalCacheModel?
    ) {
        _uiState.update {
            it.copy(
                offerIds = offerIds,
                shopData = ShopData(shopId = shopId),
                productIds = productIds,
                warehouseIds = warehouseIds,
                localCacheModel = localCacheModel
            )
        }
    }

    private fun getOfferingInfo() {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferingInfoForBuyerRequestParam(
                    offerIds = currentState.offerIds,
                    shopIds = if (currentState.shopData.shopId.isMoreThanZero()) {
                        listOf(currentState.shopData.shopId)
                    } else {
                        emptyList()
                    },
                    productAnchor = if (currentState.warehouseIds.isNotEmpty()) {
                        GetOfferingInfoForBuyerRequestParam.ProductAnchor(
                            currentState.productIds,
                            currentState.warehouseIds
                        )
                    } else {
                        null
                    },
                    userLocation = UserLocation(
                        addressId = currentState.localCacheModel?.address_id.toLongOrZero(),
                        districtId = currentState.localCacheModel?.district_id.toLongOrZero(),
                        postalCode = currentState.localCacheModel?.postal_code.orEmpty(),
                        latitude = currentState.localCacheModel?.lat.orEmpty(),
                        longitude = currentState.localCacheModel?.long.orEmpty(),
                        cityId = currentState.localCacheModel?.city_id.toLongOrZero()
                    ),
                    userId = userId.toLongOrZero(),
                )
                val result = getOfferInfoForBuyerUseCase.execute(param)
                _offeringInfo.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    private fun getOfferingProductList(
        page: Int,
        pageSize: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetOfferingProductListRequestParam(
                    offerIds = currentState.offerIds,
                    productAnchor = GetOfferingProductListRequestParam.ProductAnchor(
                        currentState.warehouseIds
                    ),
                    userLocation = GetOfferingProductListRequestParam.UserLocation(
                        addressId = currentState.localCacheModel?.address_id.toLongOrZero(),
                        districtId = currentState.localCacheModel?.district_id.toLongOrZero(),
                        postalCode = currentState.localCacheModel?.postal_code.orEmpty(),
                        latitude = currentState.localCacheModel?.lat.orEmpty(),
                        longitude = currentState.localCacheModel?.long.orEmpty(),
                        cityId = currentState.localCacheModel?.city_id.toLongOrZero()
                    ),
                    userId = userId.toLongOrZero(),
                    page = page,
                    pageSize = pageSize,
                    orderBy = currentState.sortId.toIntOrZero()
                )

                val result = getOfferProductListUseCase.execute(param)
                _productList.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    private fun getNotification() {
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

    private fun addToCart(
        product: OfferProductListUiModel.Product
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = AddToCartUseCase.getMinimumParams(
                    productId = product.productId.toString(),
                    shopId = currentState.shopData.shopId.orZero().toString()
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

    private fun setSort(sortId: String, sortName: String) {
        _uiState.update {
            it.copy(
                sortId = sortId,
                sortName = sortName
            )
        }
    }

    private fun setWarehouseIds(warehouseIds: List<Long>) {
        _uiState.update {
            it.copy(
                warehouseIds = warehouseIds
            )
        }
    }

    private fun setShopData(shopData: ShopData?) {
        if (shopData != null) {
            _uiState.update {
                it.copy(
                    shopData = shopData
                )
            }
        }
    }

    private fun setOfferingJsonData(offeringJsonData: String) {
        _uiState.update {
            it.copy(
                offeringJsonData = offeringJsonData
            )
        }
    }

    private fun setTncData(tnc: List<String>) {
        _uiState.update {
            it.copy(
                tnc = tnc
            )
        }
    }
}
