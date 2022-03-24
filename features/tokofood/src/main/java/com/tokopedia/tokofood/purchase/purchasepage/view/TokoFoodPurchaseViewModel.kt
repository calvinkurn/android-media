package com.tokopedia.tokofood.purchase.purchasepage.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.tokofood.purchase.purchasepage.view.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.*
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoFoodPurchaseViewModel @Inject constructor(val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    private val _fragmentUiModel = MutableLiveData<FragmentUiModel>()
    val fragmentUiModel: LiveData<FragmentUiModel>
        get() = _fragmentUiModel

    // List of recyclerview items
    private val _visitables = MutableLiveData<MutableList<Visitable<*>>>()
    val visitables: LiveData<MutableList<Visitable<*>>>
        get() = _visitables

    // Temporary field to store collapsed unavailable products
    private var tmpCollapsedUnavailableItems = mutableListOf<Visitable<*>>()

    private fun getVisitablesValue(): MutableList<Visitable<*>> {
        return visitables.value ?: mutableListOf()
    }

    private fun getAddressUiModel(): Pair<Int, TokoFoodPurchaseAddressUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when (data) {
                is TokoFoodPurchaseAddressUiModel -> {
                    return Pair(index, data)
                }
            }
        }
        return null
    }

    private fun getProductByProductId(productId: String): Pair<Int, TokoFoodPurchaseProductUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when {
                data is TokoFoodPurchaseProductUiModel && data.id == productId -> {
                    return Pair(index, data)
                }
                data is TokoFoodPurchaseAccordionUiModel || data is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    private fun getAccordionUiModel(): Pair<Int, TokoFoodPurchaseAccordionUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when (data) {
                is TokoFoodPurchaseAccordionUiModel -> {
                    return Pair(index, data)
                }
                is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    private fun getSummaryTransactionUiModel(): Pair<Int, TokoFoodPurchaseSummaryTransactionUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when (data) {
                is TokoFoodPurchaseSummaryTransactionUiModel -> {
                    return Pair(index, data)
                }
            }
        }
        return null
    }

    private fun getTotalAmountUiModel(): Pair<Int, TokoFoodPurchaseTotalAmountUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when (data) {
                is TokoFoodPurchaseTotalAmountUiModel -> {
                    return Pair(index, data)
                }
            }
        }
        return null
    }

    private fun getTickerErrorShopLevelUiModel(): Pair<Int, TokoFoodPurchaseTickerErrorShopLevelUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            when (data) {
                is TokoFoodPurchaseTickerErrorShopLevelUiModel -> {
                    return Pair(index, data)
                }
                is TokoFoodPurchaseProductUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    private fun getUnavailableReasonUiModel(): Pair<Int, TokoFoodPurchaseProductUnavailableReasonUiModel>? {
        val dataList = getVisitablesValue()
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductUnavailableReasonUiModel) {
                return Pair(index, data)
            }
        }
        return null
    }

    private fun getAllUnavailableProducts(): Pair<Int, List<TokoFoodPurchaseProductUiModel>> {
        val dataList = getVisitablesValue()
        var firstItemIndex = -1
        val unavailableProducts = mutableListOf<TokoFoodPurchaseProductUiModel>()
        loop@ for ((index, data) in dataList.withIndex()) {
            when {
                data is TokoFoodPurchaseProductUiModel && data.isUnavailable -> {
                    if (firstItemIndex == -1) firstItemIndex = index
                    unavailableProducts.add(data)
                }
                data is TokoFoodPurchaseAccordionUiModel || data is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return Pair(firstItemIndex, unavailableProducts)
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
        // Todo : Load from API, if success then map to UiModel, if error show global error
        launch {
            delay(3000) // Simulate hit API
            val isSuccess = true
            if (isSuccess) {
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE)
                _fragmentUiModel.value = FragmentUiModel(shopName = "Kopi Kenangan", shopLocation = "Tokopedia Tower")
                constructRecycleViewItem()
                calculateTotal()
            } else {
                // Todo : Set throwable from network
                _uiEvent.value = UiEvent(state = UiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE)
            }
        }
    }

    private fun constructRecycleViewItem() {
        val tmpData = mutableListOf<Visitable<*>>()
        val shippingData = TokoFoodPurchaseUiModelMapper.mapShippingUiModel()
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapGeneralTickerUiModel(shippingData.isShippingUnavailable))
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "1"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAddressUiModel())
        tmpData.add(shippingData)
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "2"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(shippingData.isShippingUnavailable, false))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapTickerErrorShopLevelUiModel(shippingData.isShippingUnavailable))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, false, "1"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, false, "2"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, false, "3"))
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "3"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductListHeaderUiModel(shippingData.isShippingUnavailable, true))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUnavailableReasonUiModel(shippingData.isShippingUnavailable))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, true, "4"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, true, "5"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapProductUiModel(shippingData.isShippingUnavailable, true, "6"))
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "4"))
        tmpData.add(TokoFoodPurchaseUiModelMapper.mapAccordionUiModel(shippingData.isShippingUnavailable))
        if (!shippingData.isShippingUnavailable) {
            tmpData.add(TokoFoodPurchaseDividerUiModel(id = "5"))
            tmpData.add(TokoFoodPurchaseUiModelMapper.mapPromoUiModel())
            tmpData.add(TokoFoodPurchaseDividerUiModel(id = "6"))
            tmpData.add(TokoFoodPurchaseUiModelMapper.mapSummaryTransactionUiModel())
        }
        tmpData.add(TokoFoodPurchaseDividerUiModel(id = "7"))
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
        val toBeDeletedProduct = getProductByProductId(productId)
        if (toBeDeletedProduct != null) {
            val toBeDeleteItems = mutableListOf<Visitable<*>>()
            val dataList = getVisitablesValue()
            toBeDeleteItems.add(toBeDeletedProduct.second)

            if (isLastAvailableProduct()) {
                var from = toBeDeletedProduct.first - 2
                val tickerShopErrorData = getTickerErrorShopLevelUiModel()
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
                is TokoFoodPurchaseProductUiModel -> {
                    return true
                }
                is TokoFoodPurchasePromoUiModel -> {
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
                data is TokoFoodPurchaseProductUiModel && !data.isUnavailable -> {
                    count++
                }
                (data is TokoFoodPurchaseProductUiModel && data.isUnavailable) || data is TokoFoodPurchasePromoUiModel -> {
                    break@loop
                }
            }
        }
        return count == 1
    }

    fun validateBulkDelete() {
        val unavailableProducts = getAllUnavailableProducts()
        _uiEvent.value = UiEvent(
                state = UiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG,
                data = unavailableProducts.second.size
        )
    }

    fun bulkDeleteUnavailableProducts() {
        // Todo : hit API to remove product, once it's success, perform below code to remove local data
        val dataList = getVisitablesValue()
        val unavailableSectionItems = mutableListOf<Visitable<*>>()

        val unavailableProducts = getAllUnavailableProducts()
        var indexOfUnavailableHeaderDivider = unavailableProducts.first - 3
        var indexOfFirstUnavailableProduct = unavailableProducts.first
        if (indexOfUnavailableHeaderDivider < 0) indexOfUnavailableHeaderDivider = 0
        if (indexOfFirstUnavailableProduct >= dataList.size) indexOfFirstUnavailableProduct = dataList.size - 1
        val unavailableSectionDividerHeaderAndReason = dataList.subList(indexOfUnavailableHeaderDivider, indexOfFirstUnavailableProduct)
        unavailableSectionItems.addAll(unavailableSectionDividerHeaderAndReason)
        unavailableSectionItems.addAll(unavailableProducts.second)
        val accordionUiModel = getAccordionUiModel()
        accordionUiModel?.let {
            val accordionDivider = dataList.get(accordionUiModel.first - 1)
            unavailableSectionItems.add(accordionDivider)
            unavailableSectionItems.add(it.second)
        }
        tmpCollapsedUnavailableItems.clear()
        deleteProducts(unavailableSectionItems, unavailableProducts.second.size)
    }

    fun toggleUnavailableProductsAccordion() {
        val dataList = getVisitablesValue()
        val accordionData = getAccordionUiModel()
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

    private fun collapseUnavailableProducts(newAccordionUiModel: TokoFoodPurchaseAccordionUiModel,
                                            dataList: MutableList<Visitable<*>>,
                                            mAccordionData: Pair<Int, TokoFoodPurchaseAccordionUiModel>) {
        newAccordionUiModel.isCollapsed = true
        dataList[mAccordionData.first] = newAccordionUiModel
        val unavailableReasonData = getUnavailableReasonUiModel()
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

    private fun expandUnavailableProducts(newAccordionUiModel: TokoFoodPurchaseAccordionUiModel,
                                          dataList: MutableList<Visitable<*>>,
                                          mAccordionData: Pair<Int, TokoFoodPurchaseAccordionUiModel>) {
        newAccordionUiModel.isCollapsed = false
        dataList[mAccordionData.first] = newAccordionUiModel
        val index = mAccordionData.first - 1
        dataList.addAll(index, tmpCollapsedUnavailableItems)
    }

    fun scrollToUnavailableItem() {
        val dataList = getVisitablesValue()
        var targetIndex = -1
        loop@ for ((index, data) in dataList.withIndex()) {
            if (data is TokoFoodPurchaseProductListHeaderUiModel && data.isUnavailableHeader) {
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

    fun updateNotes(product: TokoFoodPurchaseProductUiModel, notes: String) {
        val productData = getProductByProductId(product.id)
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
        var summaryTransactionUiModel: TokoFoodPurchaseSummaryTransactionUiModel? = null
        var summaryTransactionUiModelIndex = -1
        var totalAmountUiModel: TokoFoodPurchaseTotalAmountUiModel? = null
        var totalAmountUiModelIndex = -1
        var shippingUiModel: TokoFoodPurchaseShippingUiModel? = null
        var totalProduct = 0
        var subTotal = 0L
        var wrappingFee = 0L
        var shippingFee = 0L
        var serviceFee = 0L
        loop@ for ((index, data) in dataList.withIndex()) {
            when {
                data is TokoFoodPurchaseShippingUiModel -> {
                    shippingUiModel = data
                    if (!data.isShippingUnavailable) {
                        shippingFee = data.shippingPrice
                        wrappingFee = data.wrappingFee
                        serviceFee = data.serviceFee
                    }
                }
                data is TokoFoodPurchaseProductUiModel && !data.isUnavailable && !data.isDisabled -> {
                    subTotal += (data.price * data.quantity)
                    totalProduct++
                }
                data is TokoFoodPurchaseSummaryTransactionUiModel -> {
                    summaryTransactionUiModel = data
                    summaryTransactionUiModelIndex = index
                }
                data is TokoFoodPurchaseTotalAmountUiModel -> {
                    totalAmountUiModel = data
                    totalAmountUiModelIndex = index
                }
            }
        }

        summaryTransactionUiModel?.let {
            val newSummaryTransactionData = it.copy()
            newSummaryTransactionData.subTotal = TokoFoodPurchaseSummaryTransactionUiModel.Transaction(
                    title = "Total Harga ($totalProduct item)",
                    value = subTotal,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_ZERO
            )
            newSummaryTransactionData.wrappingFee = TokoFoodPurchaseSummaryTransactionUiModel.Transaction(
                    title = "Biaya Bungkus dari Restoran",
                    value = wrappingFee,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_FREE
            )
            newSummaryTransactionData.shippingFee = TokoFoodPurchaseSummaryTransactionUiModel.Transaction(
                    title = "Ongkir",
                    value = shippingFee,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_FREE
            )
            newSummaryTransactionData.serviceFee = TokoFoodPurchaseSummaryTransactionUiModel.Transaction(
                    title = "Biaya Jasa Aplikasi",
                    value = serviceFee,
                    defaultValueForZero = TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_FREE
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
        val addressData = getAddressUiModel()
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

    fun updateAddressPinpoint() {
        // Todo : hit API set address pinpoint, then reload purchase page if success
    }

}