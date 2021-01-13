package com.tokopedia.editshipping.ui.shippingeditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.domain.mapper.ShippingEditorMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.logisticCommon.data.entity.shippingeditor.ShipperListModel
import com.tokopedia.logisticCommon.data.entity.shippingeditor.ShipperTickerModel
import com.tokopedia.logisticCommon.data.repository.ShippingEditorRepository
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocWhitelist
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShippingEditorViewModel @Inject constructor(
        private val repo: ShopLocationRepository,
        private val shippingEditorRepo: ShippingEditorRepository,
        private val mapper: ShippingEditorMapper) : ViewModel() {

    private val _shopWhitelist = MutableLiveData<ShippingEditorState<ShopLocWhitelist>>()
    val shopWhitelist: LiveData<ShippingEditorState<ShopLocWhitelist>>
        get() = _shopWhitelist

    private val _shipperList = MutableLiveData<ShippingEditorState<ShipperListModel>>()
    val shipperList: LiveData<ShippingEditorState<ShipperListModel>>
        get() = _shipperList

    private val _shipperTickerList = MutableLiveData<ShippingEditorState<ShipperTickerModel>>()
    val shipperTickerList: LiveData<ShippingEditorState<ShipperTickerModel>>
        get() = _shipperTickerList

    fun getWhitelistData(shopId: Int) {
        _shopWhitelist.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetWhitelistData) {
            val getWhitelistShop = repo.getShopLocationWhitelist(shopId)
            _shopWhitelist.value = ShippingEditorState.Success(getWhitelistShop.shopLocWhitelist)
        }
    }

    fun getShipperList(shopId: Int) {
        _shipperList.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperData) {
            val getShipperListData =  shippingEditorRepo.getShippingEditor(shopId)
            _shipperList.value = ShippingEditorState.Success(mapper.mapShipperList(getShipperListData))
        }
    }

    fun getShipperTickerList(shopId: Int) {
        _shipperTickerList.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperTicker) {
            val getShipperTickerData = shippingEditorRepo.getShippingEditorShipperTicker(shopId)
            _shipperTickerList.value = ShippingEditorState.Success(mapper.mapShipperTickerList(getShipperTickerData))
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

}