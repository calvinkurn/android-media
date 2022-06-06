package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.TokofoodExt.getGlobalErrorType
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getAccordionUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getAllUnavailableProducts
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getPartiallyLoadedModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductById
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductByUpdateParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getTickerErrorShopLevelUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUiModelIndex
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUnavailableReasonUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.lifecycle.SingleLiveEvent
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
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val keroGetAddressUseCase: KeroGetAddressUseCase,
    private val checkoutTokoFoodUseCase: CheckoutTokoFoodUseCase,
    private val checkoutGeneralTokoFoodUseCase: CheckoutGeneralTokoFoodUseCase,
    val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

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

    private val checkoutTokoFoodResponse = MutableStateFlow<CheckoutTokoFood?>(null)
    private val shopId = MutableStateFlow("")
    private val isConsentAgreed = MutableStateFlow(false)

    // Temporary field to store collapsed unavailable products
    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    private val _isAddressHasPinpoint = MutableStateFlow("" to false)

    private val _updateQuantityState: MutableSharedFlow<List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>?> =
        MutableSharedFlow()

    private val _updateQuantityStateFlow: MutableStateFlow<UpdateParam?> = MutableStateFlow(null)
    val updateQuantityStateFlow = _updateQuantityStateFlow.asStateFlow()

    private val _shouldRefreshCartData = MutableSharedFlow<Boolean>()
    val shouldRefreshCartData = _shouldRefreshCartData.asSharedFlow()

    private val _trackerLoadCheckoutData = MutableSharedFlow<CheckoutTokoFoodData>()
    val trackerLoadCheckoutData = _trackerLoadCheckoutData.asSharedFlow()

    private val _trackerPaymentCheckoutData = MutableSharedFlow<CheckoutTokoFoodData>()
    val trackerPaymentCheckoutData = _trackerPaymentCheckoutData.asSharedFlow()

    init {
        viewModelScope.launch {
            _updateQuantityState
                .debounce(UPDATE_QUANTITY_DEBOUCE_TIME)
                .flatMapConcat { productList ->
                    flow {
                        productList?.let {
                            showPartialLoading()
                            emit(TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(it, shopId.value))
                        }
                    }
                }
                .collect {
                    _updateQuantityStateFlow.emit(it)
                }
        }
    }

    private fun getVisitablesValue(): MutableList<Visitable<*>> {
        return visitables.value ?: mutableListOf()
    }

    fun resetValues() {
        viewModelScope.launch {
            _updateQuantityStateFlow.value = null
            _shouldRefreshCartData.emit(false)
        }
    }

    fun setIsHasPinpoint(addressId: String, hasPinpoint: Boolean) {
        _isAddressHasPinpoint.value = addressId to hasPinpoint
    }

    fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        val dataList = getVisitablesValue()
        val from = currentIndex + 1
        val to = from + count
        if (from > dataList.size || to >= dataList.size) {
            return emptyList()
        }
        return dataList.subList(from, to)
    }

    fun loadData() {
        launchCatchError(block = {
            checkoutTokoFoodUseCase(SOURCE).collect {
                if (it.isEmptyProduct()) {
                    _uiEvent.value = PurchaseUiEvent(
                        state = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS,
                        data = it.data.shop.shopId
                    )
                    return@collect
                }
                _fragmentUiModel.value = TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(it.data.shop)
                val isPreviousPopupPromo = checkoutTokoFoodResponse.value?.data?.isPromoPopupType() == true
                checkoutTokoFoodResponse.value = it
                shopId.value = it.data.shop.shopId
                isConsentAgreed.value = !it.data.checkoutConsentBottomSheet.isShowBottomsheet
                val isEnabled = it.isEnabled()
                _visitables.value =
                    TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                        it,
                        isEnabled,
                        !_isAddressHasPinpoint.value.second
                    ).toMutableList()
                checkoutTokoFoodResponse.value?.let { checkoutResponse ->
                    _trackerLoadCheckoutData.emit(checkoutResponse.data)
                }
                if (_isAddressHasPinpoint.value.second) {
                    _uiEvent.value = PurchaseUiEvent(
                        state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                        data = it to isPreviousPopupPromo
                    )
                } else {
                    val cacheAddressId = _isAddressHasPinpoint.value.first
                    if (cacheAddressId.isEmpty()) {
                        // Check pinpoint remotely if cache address id is empty
                        val remoteAddressId = it.data.userAddress.addressId
                        keroGetAddressUseCase.execute(remoteAddressId)?.secondAddress?.isNotEmpty().let { hasPinpointRemotely ->
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
            _uiEvent.value = PurchaseUiEvent(
                state = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE,
                throwable = it
            )
            _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(
                isLastLoadStateSuccess = false,
                shopName = "",
                shopLocation = ""
            )
        })
    }

    fun loadDataPartial() {
        launchCatchError(block = {
            checkoutTokoFoodUseCase(SOURCE).collect {
                if (it.isEmptyProduct()) {
                    _uiEvent.value = PurchaseUiEvent(
                        state = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS,
                        data = it.data.shop.shopId
                    )
                    return@collect
                }
                val isPreviousPopupPromo = checkoutTokoFoodResponse.value?.data?.isPromoPopupType() == true
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                    data = it to isPreviousPopupPromo
                )
                _fragmentUiModel.value = TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(it.data.shop)
                checkoutTokoFoodResponse.value = it
                shopId.value = it.data.shop.shopId
                isConsentAgreed.value = !it.data.checkoutConsentBottomSheet.isShowBottomsheet
                val isEnabled = it.isSuccess()
                val partialData = TokoFoodPurchaseUiModelMapper.mapResponseToPartialUiModel(
                    it,
                    isEnabled,
                    !_isAddressHasPinpoint.value.second
                )
                val dataList = getVisitablesValue().toMutableList().apply {
                    getUiModelIndex<TokoFoodPurchaseShippingTokoFoodPurchaseUiModel>().let { shippingIndex ->
                        if (shippingIndex >= Int.ZERO) {
                            removeAt(shippingIndex)
                            partialData.shippingUiModel?.let { shippingUiModel ->
                                add(shippingIndex, shippingUiModel)
                            }
                        }
                    }
                    getUiModelIndex<TokoFoodPurchasePromoTokoFoodPurchaseUiModel>().let { promoIndex ->
                        if (promoIndex >= Int.ZERO) {
                            removeAt(promoIndex)
                            partialData.promoUiModel?.let { promoUiModel ->
                                add(promoIndex, promoUiModel)
                            }
                        }
                    }
                    getUiModelIndex<TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel>().let { summaryIndex ->
                        if (summaryIndex >= Int.ZERO) {
                            removeAt(summaryIndex)
                            partialData.summaryUiModel?.let { summaryUiModel ->
                                add(summaryIndex, summaryUiModel)
                            }
                        }
                    }
                    getUiModelIndex<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>().let { totalAmountIndex ->
                        if (totalAmountIndex >= Int.ZERO) {
                            removeAt(totalAmountIndex)
                            add(totalAmountIndex, partialData.totalAmountUiModel)
                        }
                    }
                    getUiModelIndex<TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel>().let { tickerErrorIndex ->
                        when {
                            partialData.tickerErrorShopLevelUiModel == null && tickerErrorIndex >= Int.ZERO -> {
                                removeAt(tickerErrorIndex)
                            }
                            partialData.tickerErrorShopLevelUiModel != null && tickerErrorIndex < Int.ZERO -> {
                                getUiModelIndex<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>().let { firstProductIndex ->
                                    if (firstProductIndex >= Int.ZERO) {
                                        add(firstProductIndex, partialData.tickerErrorShopLevelUiModel)
                                    }
                                }
                            }
                            else -> {
                                if (tickerErrorIndex >= Int.ZERO) {
                                    removeAt(tickerErrorIndex)
                                    partialData.tickerErrorShopLevelUiModel?.let { tickerErrorUiModel ->
                                        add(tickerErrorIndex, tickerErrorUiModel)
                                    }
                                }
                            }
                        }
                    }

                }

                _visitables.value = dataList
                checkoutTokoFoodResponse.value?.let { checkoutResponse ->
                    _trackerLoadCheckoutData.emit(checkoutResponse.data)
                }
            }
        }, onError = {
            _visitables.value = checkoutTokoFoodResponse.value?.let { lastResponse ->
                TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                    lastResponse,
                    lastResponse.isEnabled(),
                    !_isAddressHasPinpoint.value.second
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
        })
    }

    private fun deleteProducts(visitables: List<Visitable<*>>, productCount: Int) {
        val dataList = getVisitablesValue().toMutableList()
        dataList.removeAll(visitables)
        if (dataList.hasRemainingProduct()) {
            _visitables.value = dataList
            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_REMOVE_PRODUCT, data = productCount)
        } else {
            _uiEvent.value = PurchaseUiEvent(
                state = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS,
                data = shopId
            )
        }
    }

    fun deleteProduct(productId: String, previousCartId: String) {
        refreshPartialCartInformation()
        val toBeDeletedProduct = getVisitablesValue().getProductById(productId, previousCartId)
        if (toBeDeletedProduct != null) {
            val toBeDeleteItems = mutableListOf<Visitable<*>>()
            val dataList = getVisitablesValue()
            toBeDeleteItems.add(toBeDeletedProduct.second)

            if (isLastAvailableProduct()) {
                var from = toBeDeletedProduct.first - 2
                val tickerShopErrorData = getVisitablesValue().getTickerErrorShopLevelUiModel()
                if (tickerShopErrorData != null) {
                    from = toBeDeletedProduct.first - 3
                }
                var to = toBeDeletedProduct.first
                if (from < Int.ZERO) from = Int.ZERO
                if (to >= dataList.size) to = dataList.size - Int.ONE
                val availableHeaderAndDivider = dataList.subList(from, to).toMutableList()
                toBeDeleteItems.addAll(availableHeaderAndDivider)
            }

            deleteProducts(toBeDeleteItems, Int.ONE)
        }
    }

    private fun List<Visitable<*>>.hasRemainingProduct(): Boolean {
        loop@ for (data in this) {
            when (data) {
                is TokoFoodPurchaseProductTokoFoodPurchaseUiModel -> {
                    return true
                }
                is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    return false
                }
            }
        }
        return false
    }

    private fun isLastAvailableProduct(): Boolean {
        val dataList = getVisitablesValue()
        var count = Int.ZERO
        loop@ for (data in dataList) {
            when {
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && data.isAvailable -> {
                    count++
                }
                (data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !data.isAvailable) || data is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return count == 1
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
        // TODO: Check if ticer error section is only related to unavailable products
        getVisitablesValue().getTickerErrorShopLevelUiModel()?.let {
            unavailableSectionItems.add(it.second)
        }
        val unavailableProducts = getVisitablesValue().getAllUnavailableProducts()
        var indexOfUnavailableHeaderDivider = unavailableProducts.first - 3
        var indexOfFirstUnavailableProduct = unavailableProducts.first
        if (indexOfUnavailableHeaderDivider < Int.ZERO) indexOfUnavailableHeaderDivider = Int.ZERO
        if (indexOfFirstUnavailableProduct >= dataList.size) indexOfFirstUnavailableProduct = dataList.size - Int.ONE
        val unavailableSectionDividerHeaderAndReason = dataList.subList(indexOfUnavailableHeaderDivider, indexOfFirstUnavailableProduct)
        unavailableSectionItems.addAll(unavailableSectionDividerHeaderAndReason)
        unavailableSectionItems.addAll(unavailableProducts.second)
        val accordionUiModel = getVisitablesValue().getAccordionUiModel()
        accordionUiModel?.let {
            val accordionDivider = dataList.get(accordionUiModel.first - Int.ONE)
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

    private fun collapseUnavailableProducts(newAccordionUiModel: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel,
                                            dataList: MutableList<Visitable<*>>,
                                            mAccordionData: Pair<Int, TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>) {
        newAccordionUiModel.isCollapsed = true
        dataList[mAccordionData.first] = newAccordionUiModel
        val unavailableReasonData = getVisitablesValue().getUnavailableReasonUiModel()
        unavailableReasonData?.let { mUnavailableReasonData ->
            var from = mUnavailableReasonData.first + 2
            var to = mAccordionData.first - 1
            tmpCollapsedUnavailableItems.clear()
            if (from < Int.ZERO) from = Int.ZERO
            if (to >= dataList.size) to = dataList.size - 1
            tmpCollapsedUnavailableItems = dataList.subList(from, to).toMutableList()
        }
        dataList.removeAll(tmpCollapsedUnavailableItems)
    }

    private fun expandUnavailableProducts(newAccordionUiModel: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel,
                                          dataList: MutableList<Visitable<*>>,
                                          mAccordionData: Pair<Int, TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>) {
        newAccordionUiModel.isCollapsed = false
        dataList[mAccordionData.first] = newAccordionUiModel
        val index = mAccordionData.first - 1
        dataList.addAll(index, tmpCollapsedUnavailableItems.toMutableList())
        tmpCollapsedUnavailableItems.clear()
    }

    fun scrollToUnavailableItem() {
        val dataList = getVisitablesValue()
        var targetIndex = -1
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel && !data.isAvailableHeader) {
                targetIndex = index
                break@loop
            }
        }

        if (targetIndex > -1) {
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
                    cartData.carts.find { cartData ->
                        cartData.productId == product.id && cartData.getMetadata()?.variants?.any { cartVariant ->
                            var isSameVariants = false
                            run checkVariant@ {
                                product.variantsParam.forEach { productVariant ->
                                    if (cartVariant.variantId == productVariant.variantId && cartVariant.optionId == productVariant.optionId) {
                                        isSameVariants = true
                                        return@checkVariant
                                    }
                                }
                            }
                            isSameVariants
                        } == true
                    }?.let {
                        (visitables.value?.getOrNull(productData.first) as? TokoFoodPurchaseProductTokoFoodPurchaseUiModel)?.cartId = it.cartId
                    }
                }
            }
        }
    }

    fun triggerEditQuantity() {
        viewModelScope.launch {
            val dataList = getVisitablesValue()
                .filterIsInstance<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
                .filter { it.isAvailable && it.isEnabled }
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

    fun updateAddressPinpoint(latitude: String,
                              longitude: String) {
        _isAddressHasPinpoint.value.first.takeIf { it.isNotEmpty() }.let { addressId ->
            if (addressId == null) {
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
            } else {
                launchCatchError(
                    block = {
                        val isSuccess = withContext(dispatcher.io) {
                            keroEditAddressUseCase.execute(addressId, latitude, longitude)
                        }
                        if (isSuccess) {
                            _isAddressHasPinpoint.value = addressId to (latitude.isNotEmpty() && longitude.isNotEmpty())
                            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT)
                        } else {
                            _isAddressHasPinpoint.value = addressId to false
                            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
                        }
                    }, onError = {
                        _isAddressHasPinpoint.value = addressId to false
                        _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
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
        launchCatchError(block = {
            if (isConsentAgreed.value) {
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT)
            } else {
                checkoutTokoFoodResponse.value?.data?.checkoutConsentBottomSheet?.let { userConsent ->
                    if (userConsent.isShowBottomsheet) {
                        _uiEvent.value = PurchaseUiEvent(
                            state = PurchaseUiEvent.EVENT_SUCCESS_GET_CONSENT,
                            data = userConsent
                        )
                    } else {
                        _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT)
                    }
                }
            }
        }, onError = {
            _uiEvent.value = PurchaseUiEvent(
                state = PurchaseUiEvent.EVENT_FAILED_VALIDATE_CONSENT,
                throwable = it
            )
        })
    }

    fun setConsentAgreed() {
        isConsentAgreed.value = true
    }

    fun checkoutGeneral() {
        launchCatchError(block = {
            checkoutTokoFoodResponse.value?.let { checkoutResponse ->
                checkoutGeneralTokoFoodUseCase(checkoutResponse).collect { response ->
                    if (response.checkoutGeneralTokoFood.data.isSuccess()) {
                        _uiEvent.value = PurchaseUiEvent(
                            state = PurchaseUiEvent.EVENT_SUCCESS_CHECKOUT_GENERAL,
                            data = response.checkoutGeneralTokoFood.data.data
                        )
                        checkoutTokoFoodResponse.value?.let { checkoutResponse ->
                            _trackerPaymentCheckoutData.emit(checkoutResponse.data)
                        }
                    } else {
                        _uiEvent.value = PurchaseUiEvent(
                            state = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_TOASTER,
                            data = response.checkoutGeneralTokoFood.data,
                            throwable = MessageErrorException(response.checkoutGeneralTokoFood.data.message)
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
                if (totalAmountIndex >= Int.ZERO) {
                    removeAt(totalAmountIndex)
                    add(totalAmountIndex, uiModel.copy(isButtonLoading = isLoading))
                }
            }
        }
        _visitables.value = dataList
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

    private fun CheckoutTokoFood.isEmptyProduct(): Boolean {
        return data.availableSection.products.isEmpty() && data.unavailableSection.products.isEmpty()
    }

    companion object {
        private const val SOURCE = "checkout_page"

        private const val TOTO_LATITUDE = "-6.2216771"
        private const val TOTO_LONGITUDE = "106.8184023"

        private const val UPDATE_QUANTITY_DEBOUCE_TIME = 1000L
    }

}