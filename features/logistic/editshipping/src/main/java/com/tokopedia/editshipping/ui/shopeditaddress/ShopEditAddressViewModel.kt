package com.tokopedia.editshipping.ui.shopeditaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.domain.mapper.AutoCompleteMapper
import com.tokopedia.editshipping.domain.model.shopeditaddress.DistrictLocation
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocUpdateWarehouse
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopEditAddressViewModel @Inject constructor(private val repo: KeroRepository,
                                                   private val shopRepo: ShopLocationRepository,
                                                   private val mapper: AutoCompleteMapper) : ViewModel() {

    private val _autoCompleteList = MutableLiveData<Result<Place>>()
    val autoCompleteList: LiveData<Result<Place>>
        get() = _autoCompleteList

    private val _districtLocation = MutableLiveData<Result<DistrictLocation>>()
    val districtLocation: LiveData<Result<DistrictLocation>>
        get() = _districtLocation

    private val _saveEditShop = MutableLiveData<Result<ShopLocUpdateWarehouse>>()
    val saveEditShop: LiveData<Result<ShopLocUpdateWarehouse>>
        get() = _saveEditShop


    fun getAutoCompleteList(keyword: String) {
        viewModelScope.launch(onErrorAutoComplete) {
            val autoComplete = repo.getAutoComplete(keyword)
            _autoCompleteList.value = Success(mapper.mapAutoComplete(autoComplete))
        }
    }

    private val onErrorAutoComplete = CoroutineExceptionHandler { _, e ->
        _autoCompleteList.value = Fail(e)
    }

    fun getDistrictLocation(placeId: String) {
        viewModelScope.launch(onErrorGetDistrictLocation) {
            val districtLoc = repo.getDistrict(placeId)
            _districtLocation.value = Success(mapper.mapDistrictLoc(districtLoc))
        }
    }

    private val onErrorGetDistrictLocation = CoroutineExceptionHandler { _, e ->
        _districtLocation.value = Fail(e)
    }

    fun saveEditShopLocation(shopId: Int, warehouseId: Int, warehouseName: String,
                             districtId: Int, latLon: String, email: String,
                             addressDetail: String, postalCode: String, phone: String) {
        viewModelScope.launch(onErrorSaveEditShopLocation) {
            val saveEditLocation  = shopRepo.saveEditShopLocation(shopId, warehouseId, warehouseName, districtId, latLon, email, addressDetail, postalCode, phone)
            _saveEditShop.value = Success(saveEditLocation.shopLocUpdateWarehouse)
        }
    }


    private val onErrorSaveEditShopLocation = CoroutineExceptionHandler { _, e ->
        _saveEditShop.value = Fail(e)
    }
}