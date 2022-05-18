package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticCommon.data.response.KeroMapsAutocomplete
import com.tokopedia.logisticCommon.data.response.KeroPlacesGetDistrict
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinpointNewPageViewModel @Inject constructor(private val repo: KeroRepository,
                                                   private val getDistrictMapper: GetDistrictMapper,
                                                   private val districtBoundaryMapper: DistrictBoundaryMapper): ViewModel() {

    private var saveAddressDataModel = SaveAddressDataModel()

    private val _autofillDistrictData = MutableLiveData<Result<KeroMapsAutofill>>()
    val autofillDistrictData: LiveData<Result<KeroMapsAutofill>>
        get() = _autofillDistrictData

    private val _districtLocation = MutableLiveData<Result<GetDistrictDataUiModel>>()
    val districtLocation: LiveData<Result<GetDistrictDataUiModel>>
        get() = _districtLocation

    private val _districtBoundary = MutableLiveData<Result<DistrictBoundaryResponseUiModel>>()
    val districtBoundary: LiveData<Result<DistrictBoundaryResponseUiModel>>
        get() = _districtBoundary

    private val _districtCenter = MutableLiveData<Result<DistrictCenterUiModel>>()
    val districtCenter: LiveData<Result<DistrictCenterUiModel>>
        get() = _districtCenter

    fun getDistrictData(lat: Double, long: Double) {
        val param = "$lat,$long"
        viewModelScope.launch {
            try {
                val districtData = repo.getDistrictGeocode(param)
                _autofillDistrictData.value = Success(districtData.keroMapsAutofill)
            } catch (e: Throwable) {
                _autofillDistrictData.value = Fail(e)
            }
        }
    }

    fun getDistrictLocation(placeId: String) {
        viewModelScope.launch {
            try {
                val districtLoc = repo.getDistrict(placeId)
                _districtLocation.value = Success(getDistrictMapper.map(districtLoc))
            }
            catch (e: Throwable) {
                _districtLocation.value = Fail(e)
            }
        }
    }

    fun getDistrictBoundaries(districtId: Long) {
        viewModelScope.launch {
            try {
                val districtBoundary = repo.getDistrictBoundaries(districtId)
                _districtBoundary.value = Success(districtBoundaryMapper.mapDistrictBoundaryNew(districtBoundary))
            } catch (e: Throwable) {
                _districtBoundary.value = Fail(e)
            }
        }
    }

    fun getDistrictCenter(districtId: Long) {
        viewModelScope.launch {
            try {
                val data = repo.getDistrictCenter(districtId)
                _districtCenter.value = Success(mapDistrictCenterResponseToUiModel(data))
            } catch (e: Throwable) {
                _districtCenter.value = Fail(e)
            }
        }
    }

    private fun mapDistrictCenterResponseToUiModel(data: KeroAddrGetDistrictCenterResponse.Data) : DistrictCenterUiModel {
        return data.keroAddrGetDistrictCenter.district.let {
            DistrictCenterUiModel(
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }

    fun setAddress(address: SaveAddressDataModel) {
        this.saveAddressDataModel = address
    }

    fun getAddress() : SaveAddressDataModel {
        return this.saveAddressDataModel
    }

}