package com.tokopedia.tokofood.purchase.purchasepage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.purchase.purchasepage.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getAccordionUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getAddressUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getAllUnavailableProducts
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getPartiallyLoadedModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getProductById
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getTickerErrorShopLevelUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getUiModelIndex
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getUnavailableReasonUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.RuntimeException

@FlowPreview
class TokoFoodPurchaseViewModel @Inject constructor(
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val checkoutTokoFoodUseCase: CheckoutTokoFoodUseCase,
    val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    companion object {
        private const val SOURCE = "checkout_page"
    }

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

    private val checkoutTokoFoodResponse = MutableStateFlow<CheckoutTokoFoodResponse?>(null)
    private val isConsentAgreed = MutableStateFlow(false)

    // Temporary field to store collapsed unavailable products
    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    private val _isAddressHasPinpoint = MutableStateFlow(false)

    private val _updateQuantityState: MutableSharedFlow<List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>?> =
        MutableSharedFlow()

    private val _updateQuantityStateFlow: MutableStateFlow<UpdateParam?> = MutableStateFlow(null)
    val updateQuantityStateFlow = _updateQuantityStateFlow.asStateFlow()

    private val _shouldRefreshCartData = MutableSharedFlow<Boolean>()
    val shouldRefreshCartData = _shouldRefreshCartData.asSharedFlow()

    val isDebug = true

    init {
        viewModelScope.launch {
            _updateQuantityState
                .debounce(1000)
                .flatMapConcat { productList ->
                    flow {
                        productList?.let {
                            showPartialLoading()
                            emit(TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(it))
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

    fun setIsHasPinpoint(hasPinpoint: Boolean) {
        _isAddressHasPinpoint.value = hasPinpoint
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
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                    data = it
                )
                // TODO: Add loading state for shop toolbar
                _fragmentUiModel.value = TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(it.data.shop)
                checkoutTokoFoodResponse.value = it
                isConsentAgreed.value = !it.data.checkoutConsentBottomSheet.isShowBottomsheet
                // TODO: Check for success status
                val isEnabled = it.isSuccess()
                _visitables.value =
                    TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(it, isEnabled, !_isAddressHasPinpoint.value)
                        .toMutableList()
            }
        }, onError = {
            if (checkoutTokoFoodResponse.value == null) {
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_FAILED_LOAD_FIRST_TIME_PURCHASE_PAGE,
                    throwable = it
                )
            } else {
                _visitables.value = checkoutTokoFoodResponse.value?.let { lastResponse ->
                    TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                        lastResponse,
                        lastResponse.isSuccess(),
                        !_isAddressHasPinpoint.value
                    ).toMutableList()
                }
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE,
                    throwable = it
                )
            }
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
                // TODO: Improvement Load partial data only from query
                _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE,
                    data = it
                )
                // TODO: Add loading state for shop toolbar
                _fragmentUiModel.value = TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(it.data.shop)
                checkoutTokoFoodResponse.value = it
                isConsentAgreed.value = !it.data.checkoutConsentBottomSheet.isShowBottomsheet
                // TODO: Check for success status
                val isEnabled = it.status == 1
                val partialData = TokoFoodPurchaseUiModelMapper.mapResponseToPartialUiModel(
                    it,
                    isEnabled,
                    !_isAddressHasPinpoint.value
                )
                val dataList = getVisitablesValue().toMutableList().apply {
                    getUiModelIndex<TokoFoodPurchaseShippingTokoFoodPurchaseUiModel>().let { shippingIndex ->
                        if (shippingIndex >= 0) {
                            removeAt(shippingIndex)
                            add(shippingIndex, partialData.shippingUiModel)
                        }
                    }
                    getUiModelIndex<TokoFoodPurchasePromoTokoFoodPurchaseUiModel>().let { promoIndex ->
                        if (promoIndex >= 0) {
                            removeAt(promoIndex)
                            add(promoIndex, partialData.promoUiModel)
                        }
                    }
                    getUiModelIndex<TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel>().let { summaryIndex ->
                        if (summaryIndex >= 0) {
                            removeAt(summaryIndex)
                            add(summaryIndex, partialData.summaryUiModel)
                        }
                    }
                    getUiModelIndex<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>().let { totalAmountIndex ->
                        if (totalAmountIndex >= 0) {
                            removeAt(totalAmountIndex)
                            add(totalAmountIndex, partialData.totalAmountUiModel)
                        }
                    }

                }

                _visitables.value = dataList
            }
        }, onError = {
            _visitables.value = checkoutTokoFoodResponse.value?.let { lastResponse ->
                TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                    lastResponse,
                    lastResponse.isSuccess(),
                    !_isAddressHasPinpoint.value
                ).toMutableList()
            }
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

    private fun deleteProducts(visitables: List<Visitable<*>>, productCount: Int) {
        val dataList = getVisitablesValue().toMutableList()
        dataList.removeAll(visitables)
        if (!hasRemainingProduct()) {
            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_REMOVE_ALL_PRODUCT)
        } else {
            _visitables.value = dataList
            _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_REMOVE_PRODUCT, data = productCount)
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
                if (from < 0) from = 0
                if (to >= dataList.size) to = dataList.size - 1
                val availableHeaderAndDivider = dataList.subList(from, to).toMutableList()
                toBeDeleteItems.addAll(availableHeaderAndDivider)
            }

            deleteProducts(toBeDeleteItems, 1)
        }
    }

    private fun hasRemainingProduct(): Boolean {
        val dataList = getVisitablesValue()
        loop@ for (data in dataList) {
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
        var count = 0
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
        if (indexOfUnavailableHeaderDivider < 0) indexOfUnavailableHeaderDivider = 0
        if (indexOfFirstUnavailableProduct >= dataList.size) indexOfFirstUnavailableProduct = dataList.size - 1
        val unavailableSectionDividerHeaderAndReason = dataList.subList(indexOfUnavailableHeaderDivider, indexOfFirstUnavailableProduct)
        unavailableSectionItems.addAll(unavailableSectionDividerHeaderAndReason)
        unavailableSectionItems.addAll(unavailableProducts.second)
        val accordionUiModel = getVisitablesValue().getAccordionUiModel()
        accordionUiModel?.let {
            val accordionDivider = dataList.get(accordionUiModel.first - 1)
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
            if (from < 0) from = 0
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

    private fun deleteErrorsUnblocking() {

    }

    private fun convertProductListToUpdateParam(productList: List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>): CartTokoFoodParam {
        val cartList = productList.map {
            CartItemTokoFoodParam(
                productId = it.id,
                quantity = it.quantity
            )
        }
        return CartTokoFoodParam(
            carts = cartList
        )
    }

    private fun Throwable.getGlobalErrorType(): Int {
        return when(this) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> GlobalError.NO_CONNECTION
            is RuntimeException -> {
                when (localizedMessage?.toIntOrNull()) {
                    ResponseStatus.SC_GATEWAY_TIMEOUT, ResponseStatus.SC_REQUEST_TIMEOUT -> GlobalError.NO_CONNECTION
                    ResponseStatus.SC_NOT_FOUND -> GlobalError.PAGE_NOT_FOUND
                    ResponseStatus.SC_INTERNAL_SERVER_ERROR -> GlobalError.SERVER_ERROR
                    ResponseStatus.SC_BAD_GATEWAY -> GlobalError.MAINTENANCE
                    else -> GlobalError.SERVER_ERROR
                }
            }
            else -> GlobalError.SERVER_ERROR
        }
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

    fun updateNotes(product: CartTokoFood, previousCartId: String) {
        launchCatchError(block = {
            val productData = getVisitablesValue().getProductById(product.productId, previousCartId)
            productData?.let {
                val dataList = getVisitablesValue()
                val newProductData = it.second.copy()
                newProductData.notes = product.getMetadata().notes
                newProductData.cartId = product.cartId
                dataList[it.first] = newProductData
                _visitables.value = dataList
            }
        }, onError = {
            // TODO: Add Error
        })
    }

    fun updateNotesDebug(product: CartTokoFood, previousCartId: String, notes: String) {
        launchCatchError(block = {
            val productData = getVisitablesValue().getProductById(product.productId, previousCartId)
            productData?.let {
                val dataList = getVisitablesValue()
                val newProductData = it.second.copy()
                newProductData.notes = notes
                newProductData.cartId = product.cartId
                dataList[it.first] = newProductData
                _visitables.value = dataList
            }
        }, onError = {
            // TODO: Add Error
        })
    }

    fun triggerEditQuantity() {
        viewModelScope.launch {
            val dataList = getVisitablesValue().filterIsInstance<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
            _updateQuantityState.emit(dataList)
        }
    }

    fun validateSetPinpoint() {
        val addressData = getVisitablesValue().getAddressUiModel()
        addressData?.let {
            _uiEvent.value = PurchaseUiEvent(
                    state = PurchaseUiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT,
                    data = LocationPass().apply {
                        cityName = it.second.cityName
                        districtName = it.second.districtName
                    }
            )
        }
    }

    fun updateAddressPinpoint(latitude: String, longitude: String) {
        // Todo : hit API set address pinpoint, then reload purchase page if success
        launchCatchError(
            block = {
                val isSuccess = withContext(dispatcher.io) {
                    keroEditAddressUseCase.execute()
                }
                if (isSuccess) {
                    _isAddressHasPinpoint.value = latitude.isNotEmpty() && longitude.isNotEmpty()
                    _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT)
                } else {
                    _isAddressHasPinpoint.value = false
                    _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
                }
            }, onError = {
                _isAddressHasPinpoint.value = false
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT)
            }
        )
    }

    // TODO: refresh cart
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
            // TODO: Hit checkout general
            delay(1000)
            throw SocketTimeoutException()
            val dummyIsSuccess = false
            if (dummyIsSuccess) {
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_SUCCESS_CHECKOUT_GENERAL)
            } else {
                _uiEvent.value = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL)
            }
        }, onError = {
            _uiEvent.value = PurchaseUiEvent(
                state = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL,
                data = it.getGlobalErrorType()
            )
        })
    }

    fun setPaymentButtonLoading(isLoading: Boolean = false) {
        val dataList = getVisitablesValue().toMutableList().apply {
            getUiModel<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>()?.let { pair ->
                val (totalAmountIndex, uiModel) = pair
                if (totalAmountIndex >= 0) {
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

    private fun getAddressInfo() {

    }

}