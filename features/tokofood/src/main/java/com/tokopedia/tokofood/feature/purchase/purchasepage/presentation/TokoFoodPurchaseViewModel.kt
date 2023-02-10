package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListData
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.tokofood.common.presentation.mapper.CustomOrderDetailsMapper
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.TokofoodExt.getGlobalErrorType
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCaseNew
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getAccordionUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getAllUnavailableProducts
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getPartiallyLoadedModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductByCartId
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductById
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductByUpdateParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductWithChangedQuantity
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getTickerErrorShopLevelUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUpdatedCartId
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.isLastAvailableProduct
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.setCartId
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.setCollapsedUnavailableProducts
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper.updatePromoData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper.updateShippingData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper.updateSummaryData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper.updateTickerErrorShopLevelData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper.updateTickersData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper.updateTotalAmountData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
class TokoFoodPurchaseViewModel @Inject constructor(
    private val keroEditAddressUseCase: Lazy<KeroEditAddressUseCase>,
    private val keroGetAddressUseCase: Lazy<KeroGetAddressUseCase>,
    private val cartListTokofoodUseCase: Lazy<CheckoutTokoFoodUseCaseNew>,
    private val checkoutGeneralTokoFoodUseCase: Lazy<CheckoutGeneralTokoFoodUseCase>,
    val dispatcher: CoroutineDispatchers
) :
    BaseViewModel(dispatcher.main) {

    private val _uiEvent = SingleLiveEvent<PurchaseUiEvent>()
    val purchaseUiEvent: LiveData<PurchaseUiEvent>
        get() = _uiEvent

    private val _fragmentUiModel = MutableLiveData<TokoFoodPurchaseFragmentUiModel>()
    val fragmentUiModel: LiveData<TokoFoodPurchaseFragmentUiModel>
        get() = _fragmentUiModel

    // List of recyclerview items
    private val _visitables = MutableLiveData<MutableList<Visitable<*>>>()
    val visitables: LiveData<MutableList<Visitable<*>>>
        get() = _visitables

    private val checkoutTokoFoodResponse = MutableStateFlow<CartGeneralCartListData?>(null)
    private val shopId = MutableStateFlow("")
    private val isConsentAgreed = MutableStateFlow(false)

    // Temporary field to store collapsed unavailable products
    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    private val _isAddressHasPinpoint = MutableStateFlow("" to false)

    private val _updateQuantityState: MutableSharedFlow<List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>> =
        MutableSharedFlow()

    private val _updateQuantityStateFlow: MutableStateFlow<UpdateQuantityTokofoodParam?> = MutableStateFlow(null)
    val updateQuantityStateFlow = _updateQuantityStateFlow.asStateFlow()

    private val _shouldRefreshCartData = MutableSharedFlow<Boolean>()
    val shouldRefreshCartData = _shouldRefreshCartData.asSharedFlow()

    private val _trackerLoadCheckoutData = MutableSharedFlow<CartListData>()
    val trackerLoadCheckoutData = _trackerLoadCheckoutData.asSharedFlow()

    private val _trackerPaymentCheckoutData = MutableSharedFlow<CheckoutTokoFoodData>()
    val trackerPaymentCheckoutData = _trackerPaymentCheckoutData.asSharedFlow()

    init {
        viewModelScope.launch {
            _updateQuantityState
                .debounce(UPDATE_QUANTITY_DEBOUCE_TIME)
                .flatMapConcat { productList ->
                    flow {
                        showPartialLoading()
                        emit(TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateQuantityParam(productList))
                    }
                }
                .collect {
                    _visitables.value?.filterIsInstance<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()?.forEach { productUiModel ->
                        productUiModel.isQuantityChanged = false
                    }
                    _updateQuantityStateFlow.emit(it)
                }
        }
    }

    private fun getVisitablesValue(): MutableList<Visitable<*>> {
        return visitables.value ?: mutableListOf()
    }

    fun resetValues() {
        viewModelScope.launch {
            _visitables.value = mutableListOf(LoadingModel())
            _updateQuantityStateFlow.value = null
            _shouldRefreshCartData.emit(false)
        }
    }

    fun setIsHasPinpoint(addressId: String, hasPinpoint: Boolean) {
        _isAddressHasPinpoint.value = addressId to hasPinpoint
    }

    fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        val dataList = getVisitablesValue()
        val from = currentIndex + Int.ONE
        val to = from + count
        if (from > dataList.size || to >= dataList.size) {
            return emptyList()
        }
        return dataList.subList(from, to)
    }

    fun loadData() {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                cartListTokofoodUseCase.get().execute(SOURCE)
            }.let {
                val businessData = it.data.getTokofoodBusinessData()

                if (it.isEmptyProducts()) {
                    _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS)
                    return@launchCatchError
                }
                _fragmentUiModel.value =
                    TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(businessData.customResponse.shop)
                val existingCheckoutData = getExistingCheckoutData()
                val isPreviousPopupPromo = existingCheckoutData?.getTokofoodBusinessData()?.isPromoPopupType() == true
                checkoutTokoFoodResponse.value = it
                shopId.value = it.data.getTokofoodBusinessData().customResponse.shop.shopId
                isConsentAgreed.value = existingCheckoutData?.getTokofoodBusinessData()?.customResponse?.bottomSheet?.isShowBottomSheet != true
                val isEnabled = it.isEnabled()
                _visitables.value =
                    TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                        it,
                        isEnabled,
                        !_isAddressHasPinpoint.value.second
                    ).toMutableList()
                _trackerLoadCheckoutData.emit(it.data)
                if (_isAddressHasPinpoint.value.second) {
                    _uiEvent.value = PurchaseUiEvent(
                        state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                        data = it to isPreviousPopupPromo
                    )
                } else {
                    val cacheAddressId = _isAddressHasPinpoint.value.first
                    if (cacheAddressId.isEmpty()) {
                        // Check pinpoint remotely if cache address id is empty
                        val remoteAddressId = businessData.customResponse.userAddress.addressId.toString()
                        val secondAddress = keroGetAddressUseCase.get().execute(remoteAddressId)?.secondAddress
                        secondAddress?.isNotEmpty().let { hasPinpointRemotely ->
                            if (hasPinpointRemotely == true) {
                                _uiEvent.value = PurchaseUiEvent(
                                    state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                                    data = it to isPreviousPopupPromo
                                )
                            } else {
                                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_NO_PINPOINT)
                            }
                        }
                    } else {
                        _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_NO_PINPOINT)
                    }
                }
            }
        }, onError = {
            if (_isAddressHasPinpoint.value.second) {
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE,
                    throwable = it
                )
                _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(
                    isLastLoadStateSuccess = false,
                    shopName = "",
                    shopLocation = ""
                )
            } else {
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_NO_PINPOINT)
            }
        })
    }

    fun loadDataPartial() {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                cartListTokofoodUseCase.get().execute(SOURCE)
            }.let {
                val businessData = it.data.getTokofoodBusinessData()

                if (it.isEmptyProducts()) {
                    _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS)
                    return@launchCatchError
                }
                val existingCheckoutData = getExistingCheckoutData()
                val isPreviousPopupPromo = existingCheckoutData?.getTokofoodBusinessData()?.isPromoPopupType() == true
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                    data = it to isPreviousPopupPromo
                )
                _fragmentUiModel.value =
                    TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(businessData.customResponse.shop)
                checkoutTokoFoodResponse.value = it
                shopId.value = it.data.getTokofoodBusinessData().customResponse.shop.shopId
                isConsentAgreed.value =
                    existingCheckoutData?.getTokofoodBusinessData()?.customResponse?.bottomSheet?.isShowBottomSheet != true
                val isEnabled = it.isEnabled()
                val partialData = TokoFoodPurchaseUiModelMapper.mapResponseToPartialUiModel(
                    it,
                    isEnabled,
                    !_isAddressHasPinpoint.value.second
                )
                val dataList = getUpdatePartialVisitables(partialData)

                _visitables.value = dataList
                _trackerLoadCheckoutData.emit(it.data)
            }
        }, onError = {
                if (_isAddressHasPinpoint.value.second) {
                    _visitables.value = checkoutTokoFoodResponse.value?.let { lastResponse ->
                        TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                            lastResponse,
                            lastResponse.isEnabled(),
                            false
                        ).toMutableList()
                    }
                    _uiEvent.value = PurchaseUiEvent(
                        state = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE_PARTIAL,
                        throwable = it
                    )
                    _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(
                        isLastLoadStateSuccess = false,
                        shopName = "",
                        shopLocation = ""
                    )
                } else {
                    _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_NO_PINPOINT)
                }
            })
    }

    private fun deleteProducts(visitables: List<Visitable<*>>, productCount: Int) {
        val dataList = getVisitablesValue().toMutableList()
        dataList.removeAll(visitables)
        if (dataList.hasRemainingProduct()) {
            _visitables.value = dataList
            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_REMOVE_PRODUCT, data = productCount)
        } else {
            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS)
        }
    }

    fun deleteProduct(productId: String, previousCartId: String) {
        refreshPartialCartInformation()
        val dataList = getVisitablesValue()
        val toBeDeletedProduct = dataList.getProductById(productId, previousCartId)
        if (toBeDeletedProduct != null) {
            val toBeDeleteItems = mutableListOf<Visitable<*>>()
            toBeDeleteItems.add(toBeDeletedProduct.second)

            if (dataList.isLastAvailableProduct()) {
                var from = toBeDeletedProduct.first - INDEX_BEFORE_FROM_HEADER
                val tickerShopErrorData = getVisitablesValue().getTickerErrorShopLevelUiModel()
                if (tickerShopErrorData != null) {
                    from = toBeDeletedProduct.first - INDEX_BEFORE_FROM_TICKER
                }
                val to = toBeDeletedProduct.first
                val availableHeaderAndDivider = dataList.subList(from, to).toMutableList()
                toBeDeleteItems.addAll(availableHeaderAndDivider)
            }

            deleteProducts(toBeDeleteItems, Int.ONE)
        }
    }

    fun deleteProductNew(cartId: String) {
        refreshPartialCartInformation()
        val dataList = getVisitablesValue()
        val toBeDeletedProduct = dataList.getProductByCartId(cartId)
        if (toBeDeletedProduct != null) {
            val toBeDeleteItems = mutableListOf<Visitable<*>>()
            toBeDeleteItems.add(toBeDeletedProduct.second)

            if (dataList.isLastAvailableProduct()) {
                var from = toBeDeletedProduct.first - INDEX_BEFORE_FROM_HEADER
                val tickerShopErrorData = getVisitablesValue().getTickerErrorShopLevelUiModel()
                if (tickerShopErrorData != null) {
                    from = toBeDeletedProduct.first - INDEX_BEFORE_FROM_TICKER
                }
                val to = toBeDeletedProduct.first
                val availableHeaderAndDivider = dataList.subList(from, to).toMutableList()
                toBeDeleteItems.addAll(availableHeaderAndDivider)
            }

            deleteProducts(toBeDeleteItems, Int.ONE)
        }
    }

    private fun List<Visitable<*>>.hasRemainingProduct(): Boolean {
        return any { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel }
    }

    fun validateBulkDelete() {
        val unavailableProducts = getVisitablesValue().getAllUnavailableProducts()
        val collapsedUnavailableProducts = tmpCollapsedUnavailableItems.getAllUnavailableProducts()
        _uiEvent.value = PurchaseUiEvent(
            state = PurchaseUiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG,
            data = unavailableProducts.second.size + collapsedUnavailableProducts.second.size
        )
    }

    fun bulkDeleteUnavailableProducts() {
        refreshPartialCartInformation()
        val dataList = getVisitablesValue()
        val unavailableSectionItems = mutableListOf<Visitable<*>>()
        getVisitablesValue().getTickerErrorShopLevelUiModel()?.let {
            unavailableSectionItems.add(it.second)
        }
        val unavailableProducts = getVisitablesValue().getAllUnavailableProducts()
        val accordionUiModel = getVisitablesValue().getAccordionUiModel()
        val unavailableProductIndex = unavailableProducts.first.takeIf { it > RecyclerView.NO_POSITION }
        unavailableProductIndex?.let {
            val indexOfUnavailableHeaderDivider = it - INDEX_BEFORE_FROM_DIVIDER
            val unavailableSectionDividerHeaderAndReason = dataList.subList(
                indexOfUnavailableHeaderDivider,
                it
            )
            unavailableSectionItems.addAll(unavailableSectionDividerHeaderAndReason)
            unavailableSectionItems.addAll(unavailableProducts.second)
        }
        accordionUiModel?.let {
            val accordionDivider = dataList[accordionUiModel.first - Int.ONE]
            unavailableSectionItems.add(accordionDivider)
            unavailableSectionItems.add(it.second)
        }
        val collapsedUnavailableProducts = tmpCollapsedUnavailableItems.getAllUnavailableProducts()
        unavailableSectionItems.addAll(tmpCollapsedUnavailableItems.toMutableList())
        deleteProducts(unavailableSectionItems, unavailableProducts.second.size + collapsedUnavailableProducts.second.size)
        tmpCollapsedUnavailableItems.clear()
    }

    fun toggleUnavailableProductsAccordion() {
        val dataList = getVisitablesValue()
        val accordionData = dataList.getAccordionUiModel()
        accordionData?.let { mAccordionData ->
            val newAccordionUiModel = accordionData.second.copy()

            if (mAccordionData.second.isCollapsed) {
                expandUnavailableProducts(newAccordionUiModel, dataList, mAccordionData)
            } else {
                collapseUnavailableProducts(newAccordionUiModel, dataList, mAccordionData)
            }

            _visitables.value = dataList
        }
    }

    private fun collapseUnavailableProducts(
        newAccordionUiModel: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel,
        dataList: MutableList<Visitable<*>>,
        mAccordionData: Pair<Int, TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>
    ) {
        newAccordionUiModel.isCollapsed = true
        dataList[mAccordionData.first] = newAccordionUiModel
        tmpCollapsedUnavailableItems.setCollapsedUnavailableProducts(dataList, mAccordionData)
        dataList.removeAll(tmpCollapsedUnavailableItems)
    }

    private fun expandUnavailableProducts(
        newAccordionUiModel: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel,
        dataList: MutableList<Visitable<*>>,
        mAccordionData: Pair<Int, TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>
    ) {
        newAccordionUiModel.isCollapsed = false
        dataList[mAccordionData.first] = newAccordionUiModel
        val index = mAccordionData.first - INDEX_BEFORE_FROM_UNAVAILABLE_ACCORDION
        dataList.addAll(index, tmpCollapsedUnavailableItems.toMutableList())
        tmpCollapsedUnavailableItems.clear()
    }

    fun scrollToUnavailableItem() {
        val dataList = getVisitablesValue()
        var targetIndex = RecyclerView.NO_POSITION
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel && !data.isAvailableHeader) {
                targetIndex = index
                break@loop
            }
        }

        if (targetIndex > RecyclerView.NO_POSITION) {
            _uiEvent.value = PurchaseUiEvent(
                state = PurchaseUiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS,
                data = targetIndex
            )
        }
    }

    fun updateNotes(product: CartTokoFood) {
        val productData = getVisitablesValue().getProductById(product.productId, product.cartId)
        productData?.let {
            val dataList = getVisitablesValue()
            val newProductData = it.second.copy()
            newProductData.notes = product.getMetadata()?.notes.orEmpty()
            newProductData.cartId = product.cartId
            dataList[it.first] = newProductData
            _visitables.value = dataList
        }
    }

    fun updateCartId(updateParam: UpdateParam, cartData: CartTokoFoodData) {
        updateParam.productList.forEach { param ->
            getVisitablesValue().getProductByUpdateParam(param)?.let { productData ->
                productData.second.let { product ->
                    product.getUpdatedCartId(cartData)?.let { cartId ->
                        visitables.value.setCartId(productData.first, cartId)
                    }
                }
            }
        }
    }

    fun triggerEditQuantity() {
        viewModelScope.launch {
            val dataList = getVisitablesValue().getProductWithChangedQuantity()
            _updateQuantityState.emit(dataList)
        }
    }

    fun validateSetPinpoint() {
        _uiEvent.value = PurchaseUiEvent(
            state = PurchaseUiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT,
            data = LocationPass().apply {
                latitude = TOTO_LATITUDE
                longitude = TOTO_LONGITUDE
            }
        )
    }

    fun updateAddressPinpoint(
        latitude: String,
        longitude: String
    ) {
        _isAddressHasPinpoint.value.first.takeIf { it.isNotEmpty() }.let { addressId ->
            if (addressId == null) {
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
            } else {
                launchCatchError(
                    block = {
                        val isSuccess = withContext(dispatcher.io) {
                            keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
                        }
                        if (isSuccess) {
                            _isAddressHasPinpoint.value = addressId to (latitude.isNotEmpty() && longitude.isNotEmpty())
                            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT)
                        } else {
                            // todo
                            _isAddressHasPinpoint.value = addressId to false
                            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
                        }
                    },
                    onError = {
                        _isAddressHasPinpoint.value = addressId to false
                        _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT, data = it)
                    }
                )
            }
        }
    }

    fun refreshPartialCartInformation() {
        launch {
            showPartialLoading()
            _shouldRefreshCartData.emit(true)
        }
    }

    fun checkUserConsent() {
        if (isConsentAgreed.value) {
            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT)
        } else {
            val checkoutData = getExistingCheckoutData()
            val consentBottomSheet = checkoutData?.getTokofoodBusinessData()?.customResponse?.bottomSheet
            consentBottomSheet?.let { userConsent ->
                if (userConsent.isShowBottomSheet) {
                    _uiEvent.value = PurchaseUiEvent(
                        state = PurchaseUiEvent.EVENT_SUCCESS_GET_CONSENT,
                        data = userConsent
                    )
                } else {
                    _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT)
                }
            }
        }
    }

    fun setConsentAgreed(isAgreed: Boolean) {
        isConsentAgreed.value = isAgreed
    }

    fun checkoutGeneral() {
        launchCatchError(block = {
            checkoutTokoFoodResponse.value?.let { checkoutResponse ->
                withContext(dispatcher.io) {
                    // TODO: Change checkout general
                    checkoutGeneralTokoFoodUseCase.get().execute(checkoutResponse)
                }.let { response ->
                    if (response.checkoutGeneralTokoFood.data.isSuccess()) {
                        _uiEvent.value = PurchaseUiEvent(
                            state = PurchaseUiEvent.EVENT_SUCCESS_CHECKOUT_GENERAL,
                            data = response.checkoutGeneralTokoFood.data.data
                        )
                        _trackerPaymentCheckoutData.emit(checkoutResponse.data)
                    } else {
                        val checkoutGeneralData = response.checkoutGeneralTokoFood.data
                        val errorMessage = checkoutGeneralData.getErrorMessage()
                        _uiEvent.value = PurchaseUiEvent(
                            state = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_TOASTER,
                            data = response.checkoutGeneralTokoFood.data,
                            throwable = MessageErrorException(errorMessage)
                        )
                    }
                }
            }
        }, onError = {
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_BOTTOMSHEET,
                    data = it.getGlobalErrorType(),
                    throwable = it
                )
            })
    }

    fun setPaymentButtonLoading(isLoading: Boolean) {
        val dataList = getVisitablesValue().toMutableList().apply {
            getUiModel<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>()?.let { pair ->
                val (totalAmountIndex, uiModel) = pair
                removeAt(totalAmountIndex)
                add(totalAmountIndex, uiModel.copy(isButtonLoading = isLoading && uiModel.isEnabled))
            }
        }
        _visitables.value = dataList
    }

    fun updateProductVariant(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        launch(coroutineContext) {
            val checkoutData = getExistingCheckoutData()
            val availableProducts = checkoutData?.getTokofoodBusinessData()
                ?.let { TokoFoodPurchaseUiModelMapper.getAvailableSectionProducts(it) }
            availableProducts?.let { products ->
                val customOrderDetails =
                    CustomOrderDetailsMapper.mapTokoFoodProductsToCustomOrderDetails(products)
                val productUiModel = TokoFoodPurchaseUiModelMapper.mapUiModelToCustomizationUiModel(element, customOrderDetails)
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_GO_TO_ORDER_CUSTOMIZATION,
                    data = productUiModel
                )
            }
        }
    }

    /**
     * This method will show loading for shipping, promo, summary, and cart sections
     */
    private fun showPartialLoading() {
        val dataList = getVisitablesValue()
        val partialLoadingLayout = dataList.getPartiallyLoadedModel(true)
        partialLoadingLayout.forEach { (index, model) ->
            dataList.removeAt(index)
            dataList.add(index, model)
        }
        _visitables.value = dataList
    }

    private fun getUpdatePartialVisitables(partialData: PartialTokoFoodUiModel): MutableList<Visitable<*>> {
        val dataList = getVisitablesValue().toMutableList()
        dataList.updateShippingData(partialData.shippingUiModel)
        dataList.updatePromoData(partialData.promoUiModel)
        dataList.updateSummaryData(partialData.summaryUiModel)
        dataList.updateTotalAmountData(partialData.totalAmountUiModel)
        dataList.updateTickerErrorShopLevelData(partialData.tickerErrorShopLevelUiModel)
        dataList.updateTickersData(partialData.topTickerUiModel, partialData.bottomTickerUiModel)
        return dataList
    }

    private fun getExistingCheckoutData() = checkoutTokoFoodResponse.value?.data

    companion object {
        private const val SOURCE = "checkout_page"

        const val TOTO_LATITUDE = "-6.2216771"
        const val TOTO_LONGITUDE = "106.8184023"

        private const val UPDATE_QUANTITY_DEBOUCE_TIME = 500L

        private const val INDEX_BEFORE_FROM_HEADER = 2
        private const val INDEX_BEFORE_FROM_TICKER = 3
        private const val INDEX_BEFORE_FROM_DIVIDER = 3
        private const val INDEX_BEFORE_FROM_UNAVAILABLE_ACCORDION = 1
    }
    
}
