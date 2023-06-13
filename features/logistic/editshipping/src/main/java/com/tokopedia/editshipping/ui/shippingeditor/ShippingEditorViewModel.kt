package com.tokopedia.editshipping.ui.shippingeditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.data.repository.ShippingEditorRepository
import com.tokopedia.editshipping.domain.mapper.ShipperDetailMapper
import com.tokopedia.editshipping.domain.mapper.ShippingEditorMapper
import com.tokopedia.editshipping.domain.mapper.ValidateShippingNewMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.CourierTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperListModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.editshipping.domain.model.shippingEditor.ValidateShippingEditorModel
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shippingeditor.SaveShippingResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibility
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShippingEditorViewModel @Inject constructor(
    private val repo: ShopLocationRepository,
    private val shippingEditorRepo: ShippingEditorRepository,
    private val mapper: ShippingEditorMapper,
    private val validateShippingMapper: ValidateShippingNewMapper,
    private val detailMapper: ShipperDetailMapper
) : ViewModel() {

    private val _shopWhitelist = MutableLiveData<ShippingEditorState<KeroGetRolloutEligibility>>()
    val shopWhitelist: LiveData<ShippingEditorState<KeroGetRolloutEligibility>>
        get() = _shopWhitelist

    private val _shipperList = MutableLiveData<ShippingEditorState<ShipperListModel>>()
    val shipperList: LiveData<ShippingEditorState<ShipperListModel>>
        get() = _shipperList

    private val _shipperTickerList = MutableLiveData<ShippingEditorState<ShipperTickerModel>>()
    val shipperTickerList: LiveData<ShippingEditorState<ShipperTickerModel>>
        get() = _shipperTickerList

    private val _shipperDetail = MutableLiveData<ShippingEditorState<ShipperDetailModel>>()
    val shipperDetail: LiveData<ShippingEditorState<ShipperDetailModel>>
        get() = _shipperDetail

    private val _validateDataShipper =
        MutableLiveData<ShippingEditorState<ValidateShippingEditorModel>>()
    val validateDataShipper: LiveData<ShippingEditorState<ValidateShippingEditorModel>>
        get() = _validateDataShipper

    private val _saveShippingData = MutableLiveData<ShippingEditorState<SaveShippingResponse>>()
    val saveShippingData: LiveData<ShippingEditorState<SaveShippingResponse>>
        get() = _saveShippingData

    fun getWhitelistData(shopId: Long) {
        _shopWhitelist.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetWhitelistData) {
            val getWhitelistShop = repo.getShopLocationWhitelist(shopId)
            _shopWhitelist.value = ShippingEditorState.Success(getWhitelistShop.keroGetRolloutEligibility)
        }
    }

    fun getShipperList(shopId: Long) {
        _shipperList.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperData) {
            val getShipperListData = shippingEditorRepo.getShippingEditor(shopId)
            val model = mapper.mapShipperList(getShipperListData)
            getShipperTickerList(shopId, model)
        }
    }

    fun getShipperTickerList(shopId: Long, shipperList: ShipperListModel) {
        _shipperTickerList.value = ShippingEditorState.Loading
        viewModelScope.launch(getOnErrorGetShipperTicker(shipperList)) {
            val getShipperTickerData = shippingEditorRepo.getShippingEditorShipperTicker(shopId)
            val data = mapper.mapShipperTickerList(getShipperTickerData)
            _shipperList.value =
                ShippingEditorState.Success(combineTickerAndShipperList(shipperList, data))
            _shipperTickerList.value = ShippingEditorState.Success(data)
        }
    }

    fun getShipperDetail() {
        _shipperDetail.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperDetails) {
            val getShipperDetail = shippingEditorRepo.getShipperDetails()
            val data =
                detailMapper.mapShipperDetails(getShipperDetail.ongkirShippingEditorGetShipperDetail.data)
            _shipperDetail.value = ShippingEditorState.Success(data)
        }
    }

    fun validateShippingEditor(shopId: Long, activatedSpIds: String) {
        _validateDataShipper.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorValidateShippingEditor) {
            val getValidateData = shippingEditorRepo.validateShippingEditor(shopId, activatedSpIds)
            _validateDataShipper.value = ShippingEditorState.Success(
                validateShippingMapper.mapShippingEditorData(getValidateData.ongkirShippingEditorPopup.data)
            )
        }
    }

    fun saveShippingData(shopId: Long, activatedSpIds: String, featuresId: String?) {
        _saveShippingData.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorSaveShippingEditor) {
            val saveShippingEditor =
                shippingEditorRepo.saveShippingEditor(shopId, activatedSpIds, featuresId)
            _saveShippingData.value =
                ShippingEditorState.Success(saveShippingEditor.saveShippingEditor)
        }
    }

    private fun combineTickerAndShipperList(
        shipperList: ShipperListModel,
        tickerModel: ShipperTickerModel
    ): ShipperListModel {
        tickerModel.courierTicker.forEach { tickerShipper ->
            shipperList.findShipperById(tickerShipper.shipperId)?.run {
                setTickerDataToShipper(tickerShipper)
            }
        }
        return shipperList
    }

    private fun ShipperListModel.findShipperById(shipperId: Long): ShipperModel? {
        return shippers.conventional.find { shipper -> shipperId == shipper.shipperId }
            ?: shippers.onDemand.find { shipper -> shipperId == shipper.shipperId }
    }

    private fun ShipperModel.setTickerDataToShipper(tickerShipper: CourierTickerModel) {
        tickerState = tickerShipper.tickerState
        isAvailable = tickerState != EditShippingConstant.TICKER_STATE_UNAVAILABLE
        warehouseModel = tickerShipper.warehouses
        if (!isAvailable) {
            isActive = false
        }
        setTickerDataToShipperProduct(tickerShipper.shipperProduct)
    }

    private fun ShipperModel.setTickerDataToShipperProduct(
        tickerShipperProducts: List<ShipperProductTickerModel>
    ) {
        var shouldReCheckActiveState = false
        shipperProduct.forEach { productModel ->
            val tickerShipperProductData =
                tickerShipperProducts.find { tickerShipperProduct -> tickerShipperProduct.shipperProductId.toString() == productModel.shipperProductId }
            val shipperProductAvailable =
                tickerShipperProductData?.isAvailable ?: isAvailable
            productModel.isAvailable = shipperProductAvailable
            if (shipperProductAvailable.not()) {
                productModel.isActive = false
                shouldReCheckActiveState = true
            }
        }

        if (shouldReCheckActiveState) {
            setActiveState()
        }
    }

    private fun ShipperModel.setActiveState() {
        isActive = shipperProduct.any { shipperProductModel -> shipperProductModel.isActive }
    }

    private val onErrorGetWhitelistData = CoroutineExceptionHandler { _, e ->
        _shopWhitelist.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorGetShipperData = CoroutineExceptionHandler { _, e ->
        _shipperList.value = ShippingEditorState.Fail(e, "")
    }

    private fun getOnErrorGetShipperTicker(shipperList: ShipperListModel) =
        CoroutineExceptionHandler { _, e ->
            _shipperList.value = ShippingEditorState.Success(shipperList)
            _shipperTickerList.value = ShippingEditorState.Fail(e, "")
        }

    private val onErrorGetShipperDetails = CoroutineExceptionHandler { _, e ->
        _shipperDetail.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorValidateShippingEditor = CoroutineExceptionHandler { _, e ->
        _validateDataShipper.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorSaveShippingEditor = CoroutineExceptionHandler { _, e ->
        _saveShippingData.value = ShippingEditorState.Fail(e, "")
    }
}
