package com.tokopedia.buy_more_get_more.olp.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.bmsm_widget.domain.entity.MainProduct
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetMiniCartUseCase
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.olp.data.mapper.GetOfferInfoForBuyerMapper
import com.tokopedia.buy_more_get_more.olp.data.mapper.GetOfferProductListMapper
import com.tokopedia.buy_more_get_more.olp.data.request.GetSharingDataByOfferIDParam
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.ShopData
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpEvent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpUiState
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.SelectedTierData
import com.tokopedia.buy_more_get_more.olp.domain.entity.SharingDataByOfferIdUiModel
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetSharingDataByOfferIDUseCase
import com.tokopedia.buy_more_get_more.olp.utils.BmgmUtil
import com.tokopedia.campaign.data.request.GetOfferingInfoForBuyerRequestParam
import com.tokopedia.campaign.data.request.GetOfferingInfoForBuyerRequestParam.UserLocation
import com.tokopedia.campaign.data.request.GetOfferingProductListRequestParam
import com.tokopedia.campaign.data.request.OfferingNowInfoParam
import com.tokopedia.campaign.data.request.WarehouseParam
import com.tokopedia.campaign.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.campaign.usecase.GetOfferProductListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class OfferLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase,
    private val getOfferProductListUseCase: GetOfferProductListUseCase,
    private val getSharingDataByOfferIDUseCase: GetSharingDataByOfferIDUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val getOfferingInfoForBuyerMapper: GetOfferInfoForBuyerMapper,
    private val getOfferingProductListMapper: GetOfferProductListMapper
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(OlpUiState())
    val uiState = _uiState.asStateFlow()

    private val mutex = Mutex()

    val currentState: OlpUiState
        get() = _uiState.value

    private val _offeringInfo = MutableLiveData<Result<OfferInfoForBuyerUiModel>>()
    val offeringInfo: LiveData<Result<OfferInfoForBuyerUiModel>>
        get() = _offeringInfo

    private val _productList = MutableLiveData<OfferProductListUiModel>()
    val productList: LiveData<OfferProductListUiModel>
        get() = _productList

    private val _sharingData = MutableLiveData<Result<SharingDataByOfferIdUiModel>>()
    val sharingData: LiveData<Result<SharingDataByOfferIdUiModel>>
        get() = _sharingData

    private val _navNotificationModel = MutableLiveData(TopNavNotificationModel())
    val navNotificationLiveData: LiveData<TopNavNotificationModel>
        get() = _navNotificationModel

    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    private val _tierGifts = MutableLiveData<Result<SelectedTierData>>()
    val tierGifts: LiveData<Result<SelectedTierData>>
        get() = _tierGifts

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    val bmgmImagePath = MutableLiveData<String>()

    val userId: String
        get() = userSession.userId

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    companion object {
        const val GET_CART_STATE_DEFAULT = 0
    }

    fun processEvent(event: OlpEvent) {
        when (event) {
            is OlpEvent.SetInitialUiState -> {
                setInitialUiState(
                    offerIds = event.offerIds,
                    offerTypeId = event.offerTypeId,
                    shopId = event.shopIds,
                    productIds = event.productIds,
                    warehouseIds = event.warehouseIds,
                    localCacheModel = event.localCacheModel
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

            is OlpEvent.GetSharingData -> {
                getSharingDataByOfferId()
            }

            is OlpEvent.AddToCart -> {
                addToCart(event.product)
            }

            is OlpEvent.SetWarehouseIds -> setWarehouseIds(event.warehouseIds)
            is OlpEvent.SetShopData -> setShopData(event.shopData)
            is OlpEvent.SetOfferingJsonData -> setOfferingJsonData(event.offeringJsonData)
            is OlpEvent.SetTncData -> setTncData(event.tnc)
            is OlpEvent.SetEndDate -> setEndDate(event.endDate)
            is OlpEvent.SetOfferTypeId -> setOfferTypeId(event.offerTypeId)
            is OlpEvent.SetSharingData -> setSharingData(event.sharingData)
            is OlpEvent.TapTier -> handleTapTier(event.selectedTier, event.offerInfo)
            is OlpEvent.SetCartId -> setCartId(event.cartId)
        }
    }

    private fun setInitialUiState(
        offerIds: List<Long>,
        offerTypeId: Long,
        shopId: Long,
        productIds: List<Long> = emptyList(),
        warehouseIds: List<Long> = emptyList(),
        sharingData: SharingDataByOfferIdUiModel = SharingDataByOfferIdUiModel(),
        localCacheModel: LocalCacheModel?
    ) {
        _uiState.update {
            it.copy(
                offerIds = offerIds,
                offerTypeId = offerTypeId,
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
                val result =
                    getOfferInfoForBuyerUseCase.execute(getOfferingInfoForBuyerRequestParam())
                val mappedResult = getOfferingInfoForBuyerMapper.map(result)
                _offeringInfo.postValue(Success(mappedResult))
            },
            onError = {
                _offeringInfo.postValue(Fail(it))
            }
        )
    }

    private fun getOfferingInfoForBuyerRequestParam(): GetOfferingInfoForBuyerRequestParam {
        return GetOfferingInfoForBuyerRequestParam(
            requestHeader = createGetOfferingInfoRequestHeader(),
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
            userId = userId.toLongOrZero()
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
                    requestHeader = createGetOfferingProductListRequestHeader(),
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
                val mappedResult = getOfferingProductListMapper.map(result)
                _productList.postValue(mappedResult)
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

    private fun addToCart(product: OfferProductListUiModel.Product) {
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
                        shopId = currentState.shopData.shopId.orZero().toString(),
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

    private fun getSharingDataByOfferId() {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetSharingDataByOfferIDParam(
                    offerId = currentState.offerIds.firstOrNull().orZero(),
                    shopId = currentState.shopData.shopId
                )
                val result = getSharingDataByOfferIDUseCase.execute(param)
                _sharingData.postValue(Success(result))
            },
            onError = {
                _sharingData.postValue(Fail(it))
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

    private fun setEndDate(endDate: String) {
        _uiState.update {
            it.copy(
                endDate = endDate
            )
        }
    }

    private fun setOfferTypeId(offerTypeId: Long) {
        _uiState.update {
            it.copy(
                offerTypeId = offerTypeId
            )
        }
    }

    private fun setSharingData(sharingData: SharingDataByOfferIdUiModel) {
        _uiState.update {
            it.copy(
                sharingData = sharingData
            )
        }
    }

    private fun setCartId(cartId: String) {
        _uiState.update {
            it.copy(
                cartId = cartId
            )
        }
    }

    fun addAvailableProductImpression(product: OfferProductListUiModel.Product) {
        _uiState.update {
            val list = it.availableProductImpressionList
            list.add(product)
            it.copy(
                availableProductImpressionList = list
            )
        }
    }

    fun clearAvailableProductImpression() {
        _uiState.update {
            it.copy(
                availableProductImpressionList = mutableSetOf()
            )
        }
    }

    fun getDeeplink(): String {
        return UriUtil.buildUri(
            ApplinkConst.BUY_MORE_GET_MORE_OLP,
            currentState.offerIds.firstOrNull().toString(),
            currentState.warehouseIds.firstOrNull().toString(),
            currentState.productIds.firstOrNull().toString(),
            currentState.shopData.shopId.toString()
        )
    }

    fun saveBmgmImageToPhoneStorage(context: Context?, shopSnippetUrl: String) {
        launchCatchError(dispatchers.io, {
            context?.let {
                BmgmUtil.loadImageWithEmptyTarget(
                    it,
                    shopSnippetUrl,
                    {
                        fitCenter()
                    },
                    MediaBitmapEmptyTarget(
                        onReady = { bitmap ->
                            val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                                bitmap,
                                Bitmap.CompressFormat.PNG
                            )

                            if (savedFile != null) {
                                bmgmImagePath.postValue(savedFile.absolutePath)
                            }
                        }
                    )
                )
            }
        }, onError = {
                it.printStackTrace()
            })
    }

    private fun handleTapTier(
        selectedTier: OfferInfoForBuyerUiModel.Offering.Tier,
        offerInfo: OfferInfoForBuyerUiModel
    ) {
        getTierGifts(selectedTier, offerInfo)
    }

    private fun getTierGifts(
        selectedTier: OfferInfoForBuyerUiModel.Offering.Tier,
        offerInfo: OfferInfoForBuyerUiModel
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MiniCartParam(
                    shopIds = listOf(currentState.shopData.shopId),
                    offerIds = currentState.offerIds,
                    offerJsonData = currentState.offeringJsonData,
                    warehouseIds = currentState.warehouseIds
                )

                val miniCartData = getMiniCartUseCase(param = param)
                val tierGifts = miniCartData.toTierGifts()
                val mainProducts = miniCartData.toMainProducts()

                _tierGifts.postValue(
                    Success(
                        SelectedTierData(
                            selectedTier,
                            offerInfo,
                            tierGifts,
                            mainProducts
                        )
                    )
                )
            },
            onError = { error ->
                _tierGifts.postValue(Fail(error))
            }
        )
    }

    private fun BmgmMiniCartDataUiModel.toTierGifts(): List<TierGifts> {
        if (!isTierAchieved) return emptyList()

        val tierGifts = mutableListOf<TierGifts>()

        val gifts = tiers.filterIsInstance<BmgmMiniCartVisitable.TierUiModel>()

        gifts.forEach { gift ->
            val giftProducts = gift.productsBenefit.map { product ->
                TierGifts.GiftProduct(
                    product.productId.toLongOrZero(),
                    product.quantity
                )
            }

            tierGifts.add(TierGifts(giftProducts, gift.tierId))
        }

        return tierGifts
    }

    private fun BmgmMiniCartDataUiModel.toMainProducts(): List<MainProduct> {
        return this.products.map {
            MainProduct(it.productId.toLongOrZero(), it.productQuantity)
        }
    }

    private fun createGetOfferingInfoRequestHeader(): GetOfferingInfoForBuyerRequestParam.RequestHeader {
        return GetOfferingInfoForBuyerRequestParam.RequestHeader(
            nowInfo = OfferingNowInfoParam(createWarehousesParam())
        )
    }

    private fun createGetOfferingProductListRequestHeader(): GetOfferingProductListRequestParam.RequestHeader {
        return GetOfferingProductListRequestParam.RequestHeader(
            nowInfo = OfferingNowInfoParam(createWarehousesParam()),
            shopId = currentState.shopData.shopId
        )
    }

    private fun createWarehousesParam(): List<WarehouseParam> {
        return currentState.localCacheModel?.warehouses.orEmpty().map {
            WarehouseParam(it.warehouse_id, it.service_type)
        }
    }
}
