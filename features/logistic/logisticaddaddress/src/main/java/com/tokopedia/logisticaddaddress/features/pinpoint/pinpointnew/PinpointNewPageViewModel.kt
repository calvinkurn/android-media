package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageFragment.Companion.LOCATION_NOT_FOUND_MESSAGE
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinpointNewPageViewModel @Inject constructor(
    private val repo: KeroRepository,
    private val getDistrictMapper: GetDistrictMapper,
    private val districtBoundaryMapper: DistrictBoundaryMapper,
    private val mapsGeocodeUseCase: MapsGeocodeUseCase
) : ViewModel() {

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

    private val _mapsGeocodeState = MutableLiveData<MapsGeocodeState>()
    val mapsGeocodeState: LiveData<MapsGeocodeState>
        get() = _mapsGeocodeState

    fun setDistrictAndCityName(
        districtName: String?,
        cityName: String?
    ) {
        if (districtName?.isNotBlank() == true && cityName?.isNotBlank() == true) {
            saveAddressDataModel.districtName = districtName
            saveAddressDataModel.cityName = cityName
        }
    }

    fun getDistrictData(lat: Double, long: Double) {
        val param = "$lat,$long"
        viewModelScope.launch {
            try {
                val districtData = repo.getDistrictGeocode(
                    latlong = param,
                    isManageAddressFlow = true
                )
                _autofillDistrictData.value = Success(districtData.keroMapsAutofill)
            } catch (e: Throwable) {
                _autofillDistrictData.value = Fail(e)
            }
        }
    }

    fun getDistrictLocation(placeId: String) {
        viewModelScope.launch {
            try {
                val districtLoc = repo.getDistrict(placeId = placeId, isManageAddressFlow = true)
                _districtLocation.value = Success(getDistrictMapper.map(districtLoc))
            } catch (e: Throwable) {
                _districtLocation.value = Fail(e)
            }
        }
    }

    fun getDistrictBoundaries() {
        viewModelScope.launch {
            try {
                val districtBoundary = repo.getDistrictBoundaries(saveAddressDataModel.districtId)
                _districtBoundary.value =
                    Success(districtBoundaryMapper.mapDistrictBoundaryNew(districtBoundary))
            } catch (e: Throwable) {
                _districtBoundary.value = Fail(e)
            }
        }
    }

    fun getDistrictCenter() {
        viewModelScope.launch {
            try {
                val data = repo.getDistrictCenter(saveAddressDataModel.districtId)
                _districtCenter.value = Success(mapDistrictCenterResponseToUiModel(data))
            } catch (e: Throwable) {
                _districtCenter.value = Fail(e)
            }
        }
    }

    private fun mapDistrictCenterResponseToUiModel(data: KeroAddrGetDistrictCenterResponse.Data): DistrictCenterUiModel {
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

    fun getAddress(): SaveAddressDataModel {
        return this.saveAddressDataModel
    }

    fun setLatLong(lat: Double, long: Double) {
        this.saveAddressDataModel =
            this.saveAddressDataModel.copy(latitude = lat.toString(), longitude = long.toString())
    }

    fun getLocationFromLatLong() {
        getDistrictData(
            saveAddressDataModel.latitude.toDoubleOrZero(),
            saveAddressDataModel.longitude.toDoubleOrZero()
        )
    }

    fun getGeocodeByDistrictAndCityName() {
        viewModelScope.launchCatchError(
            block = {
                val response = mapsGeocodeUseCase(
                    MapsGeocodeParam(
                        input = MapsGeocodeParam.MapsGeocodeDataParam(
                            address = "${saveAddressDataModel.districtName}, ${saveAddressDataModel.cityName}"
                        )
                    )
                )

                response.firstLocation?.apply {
                    setLatLong(
                        lat = lat,
                        long = lng
                    )

                    _mapsGeocodeState.value = MapsGeocodeState.Success(this)
                } ?: kotlin.run {
                    _mapsGeocodeState.value = MapsGeocodeState.Fail(LOCATION_NOT_FOUND_MESSAGE)
                }
            },
            onError = {
                _mapsGeocodeState.value = MapsGeocodeState.Fail(it.message.orEmpty())
            }
        )
    }
}
