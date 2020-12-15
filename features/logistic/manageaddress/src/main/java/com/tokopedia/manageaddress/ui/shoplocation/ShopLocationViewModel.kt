package com.tokopedia.manageaddress.ui.shoplocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.data.repository.ShopLocationRepository
import com.tokopedia.manageaddress.domain.mapper.ShopLocationMapper
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopLocationViewModel @Inject constructor(
        private val repo: ShopLocationRepository,
        private val mapper: ShopLocationMapper) : ViewModel() {

    var shopLocationStateStatus: Boolean = false

    private val _shopLocation = MutableLiveData<ShopLocationState<List<Warehouse>>>()
    val shopLocation: LiveData<ShopLocationState<List<Warehouse>>>
        get() = _shopLocation

    private val _result = MutableLiveData<ShopLocationState<String>>()
    val result: LiveData<ShopLocationState<String>>
        get() = _result

    fun getShopLocationList(shopId: Int?) {
        _shopLocation.value = ShopLocationState.Loading
        viewModelScope.launch(onErrorGetShopLocation) {
            val getShopLocation = repo.getShopLocation(shopId)
             _shopLocation.value = ShopLocationState.Success(mapper.mapShopLocation(getShopLocation))
        }
    }

    fun setShopLocationState(warehouseId: Int, status: Int) {
        _result.value = ShopLocationState.Loading
        viewModelScope.launch(onErrorSetShopLocation) {
            val setShopLocationStatus = repo.setShopLocationStatus()
            shopLocationStateStatus = true
            _result.value = ShopLocationState.Success("success")
        }
    }

    private val onErrorGetShopLocation = CoroutineExceptionHandler { _, e ->
        _shopLocation.value = ShopLocationState.Fail(e, "")
    }

    private val onErrorSetShopLocation = CoroutineExceptionHandler { _, e ->
        shopLocationStateStatus = false
        _result.value = ShopLocationState.Fail(e, "")
    }

}