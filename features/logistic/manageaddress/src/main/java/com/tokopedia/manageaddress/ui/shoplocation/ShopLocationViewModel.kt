package com.tokopedia.manageaddress.ui.shoplocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.entity.shoplocation.ShopLocationModel
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocationSetStatusResponse
import com.tokopedia.manageaddress.domain.mapper.ShopLocationMapper
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopLocationViewModel @Inject constructor(
    private val repo: ShopLocationRepository,
    private val mapper: ShopLocationMapper
) : ViewModel() {

    var shopLocationStateStatus: Boolean = false

    private val _shopLocation = MutableLiveData<ShopLocationState<ShopLocationModel>>()
    val shopLocation: LiveData<ShopLocationState<ShopLocationModel>>
        get() = _shopLocation

    private val _result = MutableLiveData<ShopLocationState<ShopLocationSetStatusResponse>>()
    val result: LiveData<ShopLocationState<ShopLocationSetStatusResponse>>
        get() = _result

    fun getShopLocationList(shopId: Long?) {
        _shopLocation.value = ShopLocationState.Loading
        viewModelScope.launch(onErrorGetShopLocation) {
            val getShopLocation = repo.getShopLocation(shopId)
            _shopLocation.value =
                ShopLocationState.Success(mapper.mapLocationResponse(getShopLocation))
        }
    }

    fun setShopLocationState(warehouseId: Long, status: Int) {
        _result.value = ShopLocationState.Loading
        viewModelScope.launch(onErrorSetShopLocation) {
            val getShopLocationStatus = repo.setShopLocationStatus(warehouseId, status)
            shopLocationStateStatus = true
            _result.value = ShopLocationState.Success(getShopLocationStatus.shopLocationSetStatus)
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
