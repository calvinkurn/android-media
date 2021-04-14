package com.tokopedia.editshipping.ui.shippingeditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.data.repository.ShippingEditorRepository
import com.tokopedia.editshipping.domain.mapper.ShipperDetailMapper
import com.tokopedia.editshipping.domain.mapper.ShippingEditorMapper
import com.tokopedia.editshipping.domain.mapper.ValidateShippingNewMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shippingeditor.SaveShippingResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocWhitelist
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShippingEditorViewModel @Inject constructor(
        private val repo: ShopLocationRepository,
        private val shippingEditorRepo: ShippingEditorRepository,
        private val mapper: ShippingEditorMapper,
        private val validateShippingMapper: ValidateShippingNewMapper,
        private val detailMapper: ShipperDetailMapper) : ViewModel() {

    private val _shopWhitelist = MutableLiveData<ShippingEditorState<ShopLocWhitelist>>()
    val shopWhitelist: LiveData<ShippingEditorState<ShopLocWhitelist>>
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

    private val _validateDataShipper = MutableLiveData<ShippingEditorState<ValidateShippingEditorModel>>()
    val validateDataShipper: LiveData<ShippingEditorState<ValidateShippingEditorModel>>
        get() = _validateDataShipper

    private val _saveShippingData = MutableLiveData<ShippingEditorState<SaveShippingResponse>>()
    val saveShippingData: LiveData<ShippingEditorState<SaveShippingResponse>>
        get() = _saveShippingData

    var conventionalModel = ConventionalModel()
    var onDemandModel = OnDemandModel()

    fun getWhitelistData(shopId: Long) {
        _shopWhitelist.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetWhitelistData) {
            val getWhitelistShop = repo.getShopLocationWhitelist(shopId)
            _shopWhitelist.value = ShippingEditorState.Success(getWhitelistShop.shopLocWhitelist)
        }
    }

    fun getShipperList(shopId: Long) {
        _shipperList.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperData) {
            val getShipperListData =  shippingEditorRepo.getShippingEditor(shopId)
            _shipperList.value = ShippingEditorState.Success(mapper.mapShipperList(getShipperListData))
        }
    }

    fun getShipperTickerList(shopId: Long) {
        _shipperTickerList.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperTicker) {
            val getShipperTickerData = shippingEditorRepo.getShippingEditorShipperTicker(shopId)
            val data = mapper.mapShipperTickerList(getShipperTickerData)
            _shipperTickerList.value = ShippingEditorState.Success(data)
        }
    }

    fun getShipperDetail() {
        _shipperDetail.value  = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperDetails) {
            val getShipperDetail = shippingEditorRepo.getShipperDetails()
            val data = detailMapper.mapShipperDetails(getShipperDetail.ongkirShippingEditorGetShipperDetail.data)
            _shipperDetail.value = ShippingEditorState.Success(data)
        }
    }

    fun validateShippingEditor(shopId: Long, activatedSpIds: String) {
        _validateDataShipper.value  = ShippingEditorState.Loading
        viewModelScope.launch(onErrorValidateShippingEditor) {
            val getValidateData = shippingEditorRepo.validateShippingEditor(shopId, activatedSpIds)
            _validateDataShipper.value = ShippingEditorState.Success(validateShippingMapper.mapShippingEditorData(getValidateData.ongkirShippingEditorPopup.data))
        }
    }

    fun saveShippingData(shopId: Long, activatedSpIds: String, featuresId: String?) {
        _saveShippingData.value  = ShippingEditorState.Loading
        viewModelScope.launch(onErrorSaveShippingEditor) {
            val saveShippingEditor = shippingEditorRepo.saveShippingEditor(shopId, activatedSpIds, featuresId)
            _saveShippingData.value = ShippingEditorState.Success(saveShippingEditor.saveShippingEditor)
        }
    }

    private val onErrorGetWhitelistData = CoroutineExceptionHandler { _, e ->
        _shopWhitelist.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorGetShipperData = CoroutineExceptionHandler { _, e ->
        _shipperList.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorGetShipperTicker = CoroutineExceptionHandler { _, e ->
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