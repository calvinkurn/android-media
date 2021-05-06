package com.tokopedia.editshipping.ui.shopeditaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.domain.mapper.AutoCompleteMapper
import com.tokopedia.editshipping.domain.model.shopeditaddress.DistrictLocation
import com.tokopedia.editshipping.domain.model.shopeditaddress.ShopEditAddressState
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.KeroDistrictRecommendation
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocCheckCouriers
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

    private val _saveEditShop = MutableLiveData<ShopEditAddressState<ShopLocUpdateWarehouse>>()
    val saveEditShop: LiveData<ShopEditAddressState<ShopLocUpdateWarehouse>>
        get() = _saveEditShop

    private val _zipCodeList = MutableLiveData<Result<KeroDistrictRecommendation>>()
    val zipCodeList: LiveData<Result<KeroDistrictRecommendation>>
        get() = _zipCodeList

    private val _districtGeocode = MutableLiveData<Result<KeroMapsAutofill>>()
    val districtGeocode: LiveData<Result<KeroMapsAutofill>>
        get() = _districtGeocode

    private val _checkCouriers = MutableLiveData<ShopEditAddressState<ShopLocCheckCouriers>>()
    val checkCouriers: LiveData<ShopEditAddressState<ShopLocCheckCouriers>>
        get() = _checkCouriers


    fun getAutoCompleteList(keyword: String) {
        viewModelScope.launch(onErrorAutoComplete) {
            val autoComplete = repo.getAutoComplete(keyword)
            _autoCompleteList.value = Success(mapper.mapAutoComplete(autoComplete))
        }
    }

    fun getDistrictLocation(placeId: String) {
        viewModelScope.launch(onErrorGetDistrictLocation) {
            val districtLoc = repo.getDistrict(placeId)
            _districtLocation.value = Success(mapper.mapDistrictLoc(districtLoc))
        }
    }

    fun getZipCode(districtId: String) {
        viewModelScope.launch(onErrorGetZipCode) {
            val zipCode = repo.getZipCode(districtId)
            _zipCodeList.value = Success(zipCode.keroDistrictDetails)
        }
    }

    fun getDistrictGeocode(latlon: String?) {
        viewModelScope.launch(onErrorGetDistrictGeocode) {
            val reverseGeocode = repo.getDistrictGeocode(latlon)
            _districtGeocode.value = Success(reverseGeocode.keroMapsAutofill)
        }
    }

    fun saveEditShopLocation(shopId: Long, warehouseId: Int, warehouseName: String,
                             districtId: Int, latLon: String, email: String,
                             addressDetail: String, postalCode: String, phone: String) {
        _saveEditShop.value = ShopEditAddressState.Loading
        viewModelScope.launch(onErrorSaveEditShopLocation) {
            val saveEditLocation  = shopRepo.saveEditShopLocation(shopId, warehouseId, warehouseName, districtId, latLon, email, addressDetail, postalCode, phone)
            _saveEditShop.value = ShopEditAddressState.Success(saveEditLocation.shopLocUpdateWarehouse)
        }
    }


    fun checkCouriersAvailability(shopId: Long, districtId: Int) {
        _checkCouriers.value = ShopEditAddressState.Loading
        viewModelScope.launch(onErrorCheckCouriersAvailability){
            val getCheckCouriersData = shopRepo.shopCheckCouriersNewLoc(shopId, districtId)
            _checkCouriers.value = ShopEditAddressState.Success(getCheckCouriersData.shopLocCheckCouriers)
        }
    }

    private val onErrorAutoComplete = CoroutineExceptionHandler { _, e ->
        _autoCompleteList.value = Fail(e)
    }

    private val onErrorGetDistrictLocation = CoroutineExceptionHandler { _, e ->
        _districtLocation.value = Fail(e)
    }

    private val onErrorGetZipCode = CoroutineExceptionHandler { _, e ->
        _zipCodeList.value = Fail(e)
    }

    private val onErrorGetDistrictGeocode = CoroutineExceptionHandler{ _, e ->
        _districtGeocode.value = Fail(e)
    }

    private val onErrorSaveEditShopLocation = CoroutineExceptionHandler { _, e ->
        _saveEditShop.value = ShopEditAddressState.Fail(e, "")
    }

    private val onErrorCheckCouriersAvailability = CoroutineExceptionHandler { _, e ->
        _checkCouriers.value = ShopEditAddressState.Fail(e, "")
    }
}