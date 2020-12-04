package com.tokopedia.manageaddress.ui.shoplocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.manageaddress.data.repository.ShopLocationRepository
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationState
import com.tokopedia.manageaddress.domain.model.shoplocation.Warehouse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopLocationViewModel @Inject constructor(
        private val repo: ShopLocationRepository) : ViewModel() {

    val shop: List<Warehouse> = emptyList()

    private val _shopLocation = MutableLiveData<ShopLocationState<List<Warehouse>>>()
    val shopLocation: LiveData<ShopLocationState<List<Warehouse>>>
        get() = _shopLocation

    private val _result = MutableLiveData<ShopLocationState<String>>()
    val result: LiveData<ShopLocationState<String>>
        get() = _result

    fun getShopLocationList(shopId: Int) {
        viewModelScope.launch(onErrorGetShopLocation) {
            val getShopLocation = repo.getShopLocation(shopId)
            // put mapper here, change shop here
             _shopLocation.value = ShopLocationState.Success(shop)
        }
    }

    fun setShopLocationState(warehouseId: Int, status: Int) {
        viewModelScope.launch(onErrorSetShopLocation) {
            val setShopLocationStatus = repo.setShopLocationStatus()
            _result.value = ShopLocationState.Success("success")
        }
    }

    private val onErrorGetShopLocation = CoroutineExceptionHandler { _, e ->
        _shopLocation.value = ShopLocationState.Fail(e, "")
    }

    private val onErrorSetShopLocation = CoroutineExceptionHandler { _, e ->
        _result.value = ShopLocationState.Fail(e, "")
    }

}