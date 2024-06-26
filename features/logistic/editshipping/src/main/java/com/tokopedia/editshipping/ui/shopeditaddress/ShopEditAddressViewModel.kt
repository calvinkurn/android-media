package com.tokopedia.editshipping.ui.shopeditaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.domain.mapper.AutoCompleteMapper
import com.tokopedia.editshipping.domain.model.shopeditaddress.DistrictLocation
import com.tokopedia.editshipping.domain.model.shopeditaddress.ShopEditAddressState
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.KeroDistrictRecommendation
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocCheckCouriers
import com.tokopedia.logisticCommon.domain.param.GetDistrictGeoCodeParam
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import com.tokopedia.logisticCommon.domain.param.GetZipCodeParam
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetZipCodeUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopEditAddressViewModel @Inject constructor(
    private val getDistrict: GetDistrictUseCase,
    private val getZipCodeUseCase: GetZipCodeUseCase,
    private val getDistrictGeoCode: GetDistrictGeoCodeUseCase,
    private val shopRepo: ShopLocationRepository,
    private val mapper: AutoCompleteMapper
) : ViewModel() {

    private val _districtLocation = MutableLiveData<Result<DistrictLocation>>()
    val districtLocation: LiveData<Result<DistrictLocation>>
        get() = _districtLocation

    private val _saveEditShop = MutableLiveData<ShopEditAddressState<String>>()
    val saveEditShop: LiveData<ShopEditAddressState<String>>
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

    fun getDistrictLocation(placeId: String) {
        viewModelScope.launch(onErrorGetDistrictLocation) {
            val districtLoc = getDistrict(GetDistrictParam(placeId))
            _districtLocation.value = Success(mapper.mapDistrictLoc(districtLoc))
        }
    }

    fun getZipCode(districtId: String) {
        viewModelScope.launch(onErrorGetZipCode) {
            val zipCode = getZipCodeUseCase(GetZipCodeParam(districtId = districtId))
            _zipCodeList.value = Success(zipCode.keroDistrictDetails)
        }
    }

    fun getDistrictGeocode(latlon: String) {
        viewModelScope.launch(onErrorGetDistrictGeocode) {
            val reverseGeocode = getDistrictGeoCode(GetDistrictGeoCodeParam(latLng = latlon))
            _districtGeocode.value = Success(reverseGeocode.keroMapsAutofill)
        }
    }

    fun saveEditShopLocation(
        shopId: Long,
        warehouseId: Long,
        warehouseName: String,
        districtId: Long,
        latLon: String,
        addressDetail: String,
        postalCode: String
    ) {
        _saveEditShop.value = ShopEditAddressState.Loading
        viewModelScope.launch(onErrorSaveEditShopLocation) {
            val saveEditLocation = shopRepo.saveEditShopLocation(
                shopId = shopId,
                warehouseId = warehouseId,
                warehouseName = warehouseName,
                districtId = districtId,
                latLon = latLon,
                addressDetail = addressDetail,
                postalCode = postalCode
            )
            _saveEditShop.value =
                ShopEditAddressState.Success(warehouseName)
        }
    }

    fun checkCouriersAvailability(shopId: Long, districtId: Long) {
        _checkCouriers.value = ShopEditAddressState.Loading
        viewModelScope.launch(onErrorCheckCouriersAvailability) {
            val getCheckCouriersData = shopRepo.shopCheckCouriersNewLoc(shopId, districtId)
            _checkCouriers.value =
                ShopEditAddressState.Success(getCheckCouriersData.shopLocCheckCouriers)
        }
    }

    private val onErrorGetDistrictLocation = CoroutineExceptionHandler { _, e ->
        _districtLocation.value = Fail(e)
    }

    private val onErrorGetZipCode = CoroutineExceptionHandler { _, e ->
        _zipCodeList.value = Fail(e)
    }

    private val onErrorGetDistrictGeocode = CoroutineExceptionHandler { _, e ->
        _districtGeocode.value = Fail(e)
    }

    private val onErrorSaveEditShopLocation = CoroutineExceptionHandler { _, e ->
        _saveEditShop.value = ShopEditAddressState.Fail(e, "")
    }

    private val onErrorCheckCouriersAvailability = CoroutineExceptionHandler { _, e ->
        _checkCouriers.value = ShopEditAddressState.Fail(e, "")
    }
}
