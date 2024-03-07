package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.campaign.data.request.GetOfferingInfoForBuyerRequestParam
import com.tokopedia.campaign.data.request.GetOfferingInfoForBuyerRequestParam.*
import com.tokopedia.campaign.data.request.GetOfferingProductListRequestParam
import com.tokopedia.campaign.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.campaign.usecase.GetOfferProductListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import com.tokopedia.shop_widget.buy_more_save_more.data.mapper.GetOfferingInfoForBuyerMapper
import com.tokopedia.shop_widget.buy_more_save_more.data.mapper.GetOfferingProductListMapper
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoByShopIdUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.BmsmWidgetUiState
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.shop_widget.buy_more_save_more.presentation.fragment.BmsmWidgetTabFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BmsmWidgetTabViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase,
    private val getOfferProductListUseCase: GetOfferProductListUseCase,
    private val getOfferingInfoForBuyerMapper: GetOfferingInfoForBuyerMapper,
    private val getOfferingProductListMapper: GetOfferingProductListMapper,
    private val addToCartUseCase: AddToCartUseCase,
    private val getMiniCartDataUseCase: Lazy<GetMiniCartWidgetUseCase>,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main) {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.main

    private val _uiState = MutableStateFlow(BmsmWidgetUiState())
    val uiState = _uiState.asStateFlow()

    private val mutex = Mutex()

    val currentState: BmsmWidgetUiState
        get() = _uiState.value

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
        get() = _productList

    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    val userId: String
        get() = userSession.userId

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    companion object {
        private const val MINICART_USECASE_PARAM = "offer-shop-widget"
    }

    fun setInitialUiState(
        offerIds: List<Long>,
        shopId: Long,
        defaultOfferingData: OfferingInfoByShopIdUiModel,
        localCacheModel: LocalCacheModel?
    ) {
        _uiState.update {
            it.copy(
                offeringInfo = defaultOfferingData.toOfferingInfoForBuyerUiModel(),
                isNeedToRefreshOfferingData = defaultOfferingData.products.isEmpty(),
                offerIds = offerIds,
                shopId = shopId,
                localCacheModel = localCacheModel
            )
        }

        if (!currentState.isNeedToRefreshOfferingData) {
            _uiState.update {
                it.copy(
                    isShowLoading = false
                )
            }
            _productList.postValue(
                defaultOfferingData.toProductListUiModel().take(BmsmWidgetTabFragment.PAGE_SIZE)
            )
        }
    }

    fun getOfferingData() {
        launchCatchError(
            dispatchers.io + coroutineExceptionHandler,
            block = {
                val offeringInfoDeffered =
                    async { getOfferInfoForBuyerUseCase.execute(getOfferingInfoForBuyerRequestParam()) }
                val offeringInfo = getOfferingInfoForBuyerMapper.map(offeringInfoDeffered.await())
                _uiState.update {
                    it.copy(
                        isShowLoading = false,
                        offeringInfo = offeringInfo
                    )
                }
                if (currentState.isNeedToRefreshOfferingData) {
                    getProductListData(Int.ONE)
                }
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun getMinicartV3() {
        launchCatchError(
            dispatchers.io,
            block = {

                val miniCartDataDeffered = async {
                    getMiniCartDataUseCase.get().invoke(
                        GetMiniCartParam(
                            shopIds = listOf(currentState.shopId.toString()),
                            source = MiniCartSource.ShopPage.value,
                            useCase = MINICART_USECASE_PARAM,
                            bmgm = GetMiniCartParam.GetMiniCartBmgmParam(
                                offerIds = currentState.offerIds,
                                offerJsonData = currentState.offeringInfo.offeringJsonData,
                                warehouseIds = currentState.offeringInfo.nearestWarehouseIds
                            )
                        )
                    ).toSimplifiedData()
                }
                val miniCartSimplifiedData = if (isLogin) miniCartDataDeffered.await() else MiniCartSimplifiedData()

                _uiState.update {
                    val appliedTierId =
                        miniCartSimplifiedData.bmgmData.tiersApplied.firstOrNull()?.tierId
                            ?: currentState.offeringInfo.offerings.firstOrNull()?.tierList?.firstOrNull()?.tierId
                    it.copy(
                        isShowLoading = false,
                        isWidgetOnInitialState = false,
                        miniCartData = miniCartSimplifiedData,
                        isUpdateGiftImage = appliedTierId == currentState.currentAppliedId,
                        currentAppliedId = appliedTierId.orZero()
                    )
                }
                _miniCartSimplifiedData.postValue(miniCartSimplifiedData)
            },
            onError =  { }
        )
    }

    private fun getOfferingInfoForBuyerRequestParam(): GetOfferingInfoForBuyerRequestParam {
        return GetOfferingInfoForBuyerRequestParam(
            offerIds = currentState.offerIds,
            shopIds = if (currentState.shopId.isMoreThanZero()) {
                listOf(currentState.shopId)
            } else {
                emptyList()
            },
            requestHeader = RequestHeader(
                source = "shop_page",
                useCase = "offer-shop-widget"
            ),
            userLocation = UserLocation(
                addressId = currentState.localCacheModel?.address_id.toLongOrZero(),
                districtId = currentState.localCacheModel?.district_id.toLongOrZero(),
                postalCode = currentState.localCacheModel?.postal_code.orEmpty(),
                latitude = currentState.localCacheModel?.lat.orEmpty(),
                longitude = currentState.localCacheModel?.long.orEmpty(),
                cityId = currentState.localCacheModel?.city_id.toLongOrZero()
            ),
            userId = userId.toLongOrZero(),
            additionalParams = AdditionalParams()
        )
    }

    private fun getOfferingProductListRequestParam(
        warehouseIds: List<Long> = currentState.offeringInfo.nearestWarehouseIds,
        page: Int = BmsmWidgetTabFragment.FIRST_PAGE,
        pageSize: Int = BmsmWidgetTabFragment.PAGE_SIZE
    ): GetOfferingProductListRequestParam {
        return GetOfferingProductListRequestParam(
            offerIds = currentState.offerIds,
            productAnchor = GetOfferingProductListRequestParam.ProductAnchor(
                warehouseIds
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
            pageSize = pageSize
        )
    }

    fun getProductListData(page: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val offeringProductListDeffered = async {
                    getOfferProductListUseCase.execute(
                        getOfferingProductListRequestParam(page = page)
                    )
                }
                val offeringProductList =
                    getOfferingProductListMapper.map(offeringProductListDeffered.await())
                _productList.postValue(offeringProductList.productList)
                _uiState.update {
                    it.copy(isNeedToRefreshOfferingData = false)
                }
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun addToCart(product: Product) {
        launchCatchError(
            dispatchers.io,
            block = {
                mutex.withLock {
                    val minOrder = if (product.minOrder.isMoreThanZero()) {
                        product.minOrder
                    } else {
                        Int.ONE
                    }
                    val param = AddToCartUseCase.getMinimumParams(
                        productId = product.productId.toString(),
                        shopId = currentState.shopId.orZero().toString(),
                        quantity = minOrder
                    )
                    addToCartUseCase.setParams(param)
                    val result = addToCartUseCase.executeOnBackground()
                    _miniCartAdd.postValue(Success(result))
                }
            },
            onError = {
                _miniCartAdd.postValue(Fail(it))
            }
        )
    }
}
