package com.tokopedia.tokofood.purchase.purchasepage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.tokofood.purchase.purchasepage.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getAccordionUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getAddressUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getAllUnavailableProducts
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getProductByProductId
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getTickerErrorShopLevelUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.VisitableDataHelper.getUnavailableReasonUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodPurchaseViewModel @Inject constructor(
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    private val _fragmentUiModel = MutableLiveData<TokoFoodPurchaseFragmentUiModel>()
    val fragmentUiModel: LiveData<TokoFoodPurchaseFragmentUiModel>
        get() = _fragmentUiModel

    // List of recyclerview items
    private val _visitables = MutableLiveData<MutableList<Visitable<*>>>()
    val visitables: LiveData<MutableList<Visitable<*>>>
        get() = _visitables

    // Temporary field to store collapsed unavailable products
    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    // Dummy temporary field to store address data
    private var tmpAddressData = "0" to false

    private fun getVisitablesValue(): MutableList<Visitable<*>> {
        return visitables.value ?: mutableListOf()
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

        launch {
            delay(3000) // Simulate hit API
            val isSuccess = true
            if (isSuccess) {
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE)
                _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(isLastLoadStateSuccess = true, shopName = "Kopi Kenangan", shopLocation = "Tokopedia Tower")
                constructRecycleViewItem()
                calculateTotal()
            } else {
                // Todo : Set throwable from network
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE)
                _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(isLastLoadStateSuccess = false, shopName = "", shopLocation = "")
            }
        }
        // Todo : Load from API, if success then map to UiModel and update shared cart data, if error show global error
//        if (fragmentUiModel.value != null) {
//            if (fragmentUiModel.value?.isLastLoadStateSuccess == true) {
//                _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE)
//                _fragmentUiModel.value = fragmentUiModel.value
//                _visitables.value = visitables.value
//                calculateTotal()
//            } else {
//                _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE)
//                _fragmentUiModel.value = fragmentUiModel.value
//            }
//        } else {
//            launch {
//                delay(3000) // Simulate hit API
//                val isSuccess = true
//                if (isSuccess) {
//                    _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE)
//                    _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(isLastLoadStateSuccess = true, shopName = "Kopi Kenangan", shopLocation = "Tokopedia Tower")
//                    constructRecycleViewItem()
//                    calculateTotal()
//                } else {
//                    // Todo : Set throwable from network
//                    _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE)
//                    _fragmentUiModel.value = TokoFoodPurchaseFragmentUiModel(isLastLoadStateSuccess = false, shopName = "", shopLocation = "")
//                }
//            }
//        }
    }

    private fun constructRecycleViewItem() {
        // Todo : read API response, map to UiModel, below is example mapping using mock data
        val tmpData = mutableListOf<Visitable<*>>()
        val needPinpoint = !tmpAddressData.second
        val shippingData = TokoFoodPurchaseUiModelMapper.mapShippingUiModel(needPinpoint = needPinpoint)
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapGeneralTickerUiModel(shippingData.isShippingUnavailable))
        tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "1"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAddressUiModel())
        tmpData.add(shippingData)
        tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "2"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(shippingData.isShippingUnavailable, false))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapTickerErrorShopLevelUiModel(shippingData.isShippingUnavailable))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, false, "1"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, false, "2"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, false, "3"))
        tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "3"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(shippingData.isShippingUnavailable, true))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUnavailableReasonUiModel(shippingData.isShippingUnavailable))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, true, "4"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, true, "5"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, true, "6"))
        tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "4"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAccordionUiModel(shippingData.isShippingUnavailable))
        if (!shippingData.isShippingUnavailable) {
            tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "5"))
            tmpData.add(TokoFoodPurchaseUiModelMapper.mapPromoUiModel())
            tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "6"))
            tmpData.add(TokoFoodPurchaseUiModelMapper.mapSummaryTransactionUiModel())
        }
        tmpData.add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "7"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapTotalAmountUiModel())
        _visitables.value = tmpData
    }

    private fun deleteProducts(visitables: List<Visitable<*>>, productCount: Int) {
        val dataList = getVisitablesValue()
        dataList.removeAll(visitables)
        if (!hasRemainingProduct()) {
            _uiEvent.value = UiEvent(state = UiEvent.EVENT_REMOVE_ALL_PRODUCT)
        } else {
            _visitables.value = dataList
            _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_REMOVE_PRODUCT, data = productCount)
        }

        calculateTotal()
    }

    fun deleteProduct(productId: String) {
        // Todo : hit API to remove product, once it's success, perform below code to remove local data
        val toBeDeletedProduct = getVisitablesValue().getProductByProductId(productId)
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
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !data.isUnavailable -> {
                    count++
                }
                (data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && data.isUnavailable) || data is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return count == 1
    }

    fun validateBulkDelete() {
        val unavailableProducts = getVisitablesValue().getAllUnavailableProducts()
        val collapsedUnavailableProducts = tmpCollapsedUnavailableItems.getAllUnavailableProducts()
        _uiEvent.value = UiEvent(
                state = UiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG,
                data = unavailableProducts.second.size + collapsedUnavailableProducts.second.size
        )
    }

    fun bulkDeleteUnavailableProducts() {
        // Todo : hit API to remove product, once it's success, perform below code to remove local data
        val dataList = getVisitablesValue()
        val unavailableSectionItems = mutableListOf<Visitable<*>>()

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

    fun scrollToUnavailableItem() {
        val dataList = getVisitablesValue()
        var targetIndex = -1
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel && data.isUnavailableHeader) {
                targetIndex = index
                break@loop
            }
        }

        if (targetIndex > -1) {
            _uiEvent.value = UiEvent(
                    state = UiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS,
                    data = targetIndex
            )
        }
    }

    fun updateNotes(product: TokoFoodPurchaseProductTokoFoodPurchaseUiModel, notes: String) {
        val productData = getVisitablesValue().getProductByProductId(product.id)
        productData?.let {
            val dataList = getVisitablesValue()
            val newProductData = it.second.copy()
            newProductData.notes = notes
            dataList[it.first] = newProductData

            _visitables.value = dataList
        }
    }

    fun calculateTotal() {
        val dataList = getVisitablesValue()
        var summaryTransactionUiModel: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel? = null
        var summaryTransactionUiModelIndex = -1
        var totalAmountUiModel: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel? = null
        var totalAmountUiModelIndex = -1
        var shippingUiModel: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel? = null
        var totalProduct = 0
        var subTotal = 0L
        var wrappingFee = 0L
        var shippingFee = 0L
        var serviceFee = 0L
        loop@ for ((index, data) in dataList.withIndex()) {
            when {
                data is TokoFoodPurchaseShippingTokoFoodPurchaseUiModel -> {
                    shippingUiModel = data
                    if (!data.isShippingUnavailable) {
                        shippingFee = data.shippingPrice
                        wrappingFee = data.wrappingFee
                        serviceFee = data.serviceFee
                    }
                }
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !data.isUnavailable && !data.isDisabled -> {
                    subTotal += (data.price * data.quantity)
                    totalProduct++
                }
                data is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel -> {
                    summaryTransactionUiModel = data
                    summaryTransactionUiModelIndex = index
                }
                data is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel -> {
                    totalAmountUiModel = data
                    totalAmountUiModelIndex = index
                }
            }
        }

        summaryTransactionUiModel?.let {
            val newSummaryTransactionData = it.copy()
            newSummaryTransactionData.subTotal = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                    title = "Total Harga ($totalProduct item)",
                    value = subTotal,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO
            )
            newSummaryTransactionData.wrappingFee = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                    title = "Biaya Bungkus dari Restoran",
                    value = wrappingFee,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_FREE
            )
            newSummaryTransactionData.shippingFee = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                    title = "Ongkir",
                    value = shippingFee,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_FREE
            )
            newSummaryTransactionData.serviceFee = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                    title = "Biaya Jasa Aplikasi",
                    value = serviceFee,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_FREE
            )
            dataList[summaryTransactionUiModelIndex] = newSummaryTransactionData
        }

        totalAmountUiModel?.let {
            val newTotalAmountData = totalAmountUiModel.copy()
            newTotalAmountData.apply {
                isDisabled = shippingUiModel?.isShippingUnavailable ?: false
                totalAmount = subTotal + shippingFee + wrappingFee + serviceFee
            }
            dataList[totalAmountUiModelIndex] = newTotalAmountData
        }

        _visitables.value = dataList
    }

    fun updateAddress(chosenAddressModel: ChosenAddressModel) {
        // Todo : hit API change address, then reload purchase page if success and show toaster
    }

    fun validateSetPinpoint() {
        val addressData = getVisitablesValue().getAddressUiModel()
        addressData?.let {
            _uiEvent.value = UiEvent(
                    state = UiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT,
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
                tmpAddressData = tmpAddressData.first to isSuccess
                if (isSuccess) {
                    _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_EDIT_PINPOINT)
                } else {
                    _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_EDIT_PINPOINT)
                }
            }, onError = {
                tmpAddressData = tmpAddressData.first to false
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_EDIT_PINPOINT)
            }
        )
    }

    private fun getAddressInfo() {

    }

}