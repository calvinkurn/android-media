package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticCommon.domain.param.GetDistrictGeoCodeParam
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictBoundariesUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictCenterUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageFragment.Companion.LOCATION_NOT_FOUND_MESSAGE
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinpointNewPageViewModel @Inject constructor(
    private val getDistrict: GetDistrictUseCase,
    private val getDistrictBoundaries: GetDistrictBoundariesUseCase,
    private val getDistrictCenter: GetDistrictCenterUseCase,
    private val getDistrictGeoCode: GetDistrictGeoCodeUseCase,
    private val getDistrictMapper: GetDistrictMapper,
    private val districtBoundaryMapper: DistrictBoundaryMapper,
    private val mapsGeocodeUseCase: MapsGeocodeUseCase
) : ViewModel() {

    // only set this for pinpoint webview
    private var saveAddressDataModel: SaveAddressDataModel? = null
    var uiModel = PinpointUiModel()
    var addressId = ""

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

    fun setPinpointUiModel(
        districtName: String = "",
        cityName: String = "",
        lat: Double = 0.0,
        long: Double = 0.0,
        placeId: String = "",
        districtId: Long = 0L
    ) {
        this.uiModel = this.uiModel.copy(
            districtName = districtName,
            cityName = cityName,
            lat = lat,
            long = long,
            placeId = placeId,
            districtId = districtId
        )
    }

//    fun setDistrictAndCityName(
//        districtName: String?,
//        cityName: String?
//    ) {
//        if (districtName?.isNotBlank() == true && cityName?.isNotBlank() == true) {
//            saveAddressDataModel.districtName = districtName
//            saveAddressDataModel.cityName = cityName
//        }
//    }

    fun getDistrictData(lat: Double, long: Double) {
        val param = "$lat,$long"
        viewModelScope.launch {
            try {
                val districtData = getDistrictGeoCode(
                    GetDistrictGeoCodeParam(
                        latLng = param,
                        isManageAddressFlow = true
                    )
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
                val districtLoc = getDistrict(GetDistrictParam(placeId = placeId, isManageAddressFlow = true))
                _districtLocation.value = Success(getDistrictMapper.map(districtLoc))
            } catch (e: Throwable) {
                _districtLocation.value = Fail(e)
            }
        }
    }

    fun getDistrictBoundaries() {
        viewModelScope.launch {
            try {
                val districtBoundary = getDistrictBoundaries(uiModel.districtId)
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
                val data = getDistrictCenter(uiModel.districtId)
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
        return saveAddressDataModel ?: SaveAddressDataModel(
            districtName = uiModel.districtName,
            cityName = uiModel.cityName,
            provinceName = uiModel.provinceName,
            districtId = uiModel.districtId,
            cityId = uiModel.cityId,
            provinceId = uiModel.provinceId,
            postalCode = uiModel.postalCode,
            latitude = uiModel.lat.toString(),
            longitude = uiModel.long.toString(),
            title = uiModel.title
        )
    }

    fun setLatLong(lat: Double, long: Double) {
        this.uiModel =
            this.uiModel.copy(lat = lat, long = long)
    }

    fun getLocationFromLatLong() {
        getDistrictData(
            uiModel.lat,
            uiModel.long
        )
    }

    fun getGeocodeByDistrictAndCityName() {
        viewModelScope.launchCatchError(
            block = {
                val response = mapsGeocodeUseCase(
                    MapsGeocodeParam(
                        input = MapsGeocodeParam.MapsGeocodeDataParam(
                            address = "${uiModel.districtName}, ${uiModel.cityName}"
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
