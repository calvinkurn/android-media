package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticCommon.domain.param.GetDistrictGeoCodeParam
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictBoundariesUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictCenterUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.isEdit
import com.tokopedia.logisticCommon.uimodel.isPinpointOnly
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper.map
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictCenterUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.PinpointFragment
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageFragment.Companion.LOCATION_NOT_FOUND_MESSAGE
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.BottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

private val PinpointUiModel.anaNegativeFullFlow: Boolean
    get() {
        // todo need to test first
        return this.districtId != 0L
    }

class PinpointViewModel @Inject constructor(
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
    var whDistrictId = 0L
    var uiState: AddressUiState? = null

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

    private val _action = MutableLiveData<PinpointAction>()
    val action: LiveData<PinpointAction>
        get() = _action

    private val _pinpointBottomSheet = MutableLiveData<BottomSheetState>()
    val pinpointBottomSheet: LiveData<BottomSheetState>
        get() = _pinpointBottomSheet
    fun onViewCreated(
        districtName: String = "",
        cityName: String = "",
        lat: Double = 0.0,
        long: Double = 0.0,
        placeId: String = "",
        districtId: Long = 0L,
        whDistrictId: Long = 0L,
        addressId: String = "",
        uiState: AddressUiState
    ) {
        this.uiModel = this.uiModel.copy(
            districtName = districtName,
            cityName = cityName,
            lat = lat,
            long = long,
            placeId = placeId,
            districtId = districtId
        )
        this.whDistrictId = whDistrictId
        this.addressId = addressId
        this.uiState = uiState
    }

    fun fetchData() {
        if (uiModel.placeId.isNotEmpty()) {
            getDistrictLocation(uiModel.placeId)
        } else if (uiModel.hasPinpoint()) {
            getLocationFromLatLong()
        } else if (uiModel.districtId != 0L) {
            getDistrictCenter()
        } else if (uiModel.hasDistrictAndCityName) {
            getGeocodeByDistrictAndCityName()
        } else if (uiState.isPinpointOnly()) {
            _action.value = PinpointAction.GetCurrentLocation
        }
    }

    // batas suci

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
                if (districtData.keroMapsAutofill.messageError.isEmpty()) {
                    if (uiState.isAdd() && uiModel.anaNegativeFullFlow && districtData.keroMapsAutofill.data.districtId != uiModel.districtId) {
                        // is polygon but coordinate not in selected district
                        _action.value = PinpointAction.InvalidDistrictPinpoint
                        _pinpointBottomSheet.value = BottomSheetState.LocationDetail(
                            title = "",
                            description = "",
                            buttonPrimary = createButtonPrimary(success = false, enable = false),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    } else {
                        uiModel = uiModel.map(districtData.keroMapsAutofill.data, null)
                        _pinpointBottomSheet.value = BottomSheetState.LocationDetail(
                            title = uiModel.title,
                            description = uiModel.formattedAddress,
                            buttonPrimary = createButtonPrimary(success = true, enable = true),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    }
                } else {
                    districtData.keroMapsAutofill.messageError.getOrNull(0)?.run {
                        checkErrorMessage(this)
                    }
                }
//                _autofillDistrictData.value = Success(districtData.keroMapsAutofill)
            } catch (e: Throwable) {
                checkErrorMessage(e.message.toString())
            }
        }
    }

    private fun createButtonSecondary(enable: Boolean): BottomSheetState.LocationDetail.LocationDetailButtonUiModel {
        return BottomSheetState.LocationDetail.LocationDetailButtonUiModel(
            show = uiState.isAdd(),
            state = uiState,
            enable = enable
        )
    }

    private fun isCoordinateInsideDistrict(districtData: Data): Boolean {
        return uiState.isAdd() && uiModel.anaNegativeFullFlow && districtData.districtId != uiModel.districtId
    }

    private fun createButtonPrimary(success: Boolean, enable: Boolean): BottomSheetState.LocationDetail.PrimaryButtonUiModel {
        return BottomSheetState.LocationDetail.PrimaryButtonUiModel(
            show = true,
            state = BottomSheetState.LocationDetail.PrimaryButtonUiModel.PrimaryButtonState(addressUiState = uiState, success = success),
            enable = enable,
            text = if (uiState.isPinpointOnly()) { "Pilih Lokasi Ini" } else { "Pilih Lokasi & Lanjut Isi Alamat" }
        )
    }

    private fun locationNotFound(showIllustationMap: Boolean) {
        val invalidLocationDetail =
            if (uiModel.hasPinpoint() || uiState.isPinpointOnly()) {
                "Tenang, kamu bisa pilih ulang lokasimu atau pakai lokasimu sebelumnya."
            } else {
                "Tenang, kamu bisa pilih ulang lokasimu atau tetap menyimpan alamat tanpa pinpoint."
            }
        val buttonState: BottomSheetState.LocationInvalid.ButtonInvalidModel = if (uiState.isPinpointOnly()) {
            BottomSheetState.LocationInvalid.ButtonInvalidModel(
                show = false,
                state = BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND
            )
        } else if (uiState.isEdit()) {
            if (uiModel.hasPinpoint()) {
                BottomSheetState.LocationInvalid.ButtonInvalidModel(
                    show = false,
                    state = BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND
                )
            } else {
                BottomSheetState.LocationInvalid.ButtonInvalidModel(
                    variant = UnifyButton.Variant.GHOST,
                    type = UnifyButton.Type.MAIN,
                    text = "Mengerti",
                    show = true,
                    state = BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND
                )
            }
        } else {
            if (uiState.isAdd()) {
                BottomSheetState.LocationInvalid.ButtonInvalidModel(
                    show = true,
                    state = BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND
                )
            } else {
                BottomSheetState.LocationInvalid.ButtonInvalidModel(
                    show = false,
                    state = BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND
                )
            }
        }

        _pinpointBottomSheet.value = BottomSheetState.LocationInvalid(
            type = BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND,
            title = "Yaah, lokasimu tidak terdeteksi",
            detail = invalidLocationDetail,
            imageUrl = TokopediaImageUrl.LOCATION_NOT_FOUND,
            showMapIllustration = showIllustationMap,
            buttonState = buttonState
        )
    }

    fun getDistrictLocation(placeId: String) {
        viewModelScope.launch {
            try {
                val districtLoc =
                    getDistrict(GetDistrictParam(placeId = placeId, isManageAddressFlow = true))
                val responseModel = getDistrictMapper.map(districtLoc)
                val latitude = responseModel.latitude.toDoubleOrZero()
                val longitude = responseModel.longitude.toDoubleOrZero()
                if (latitude != 0.0 && longitude != 0.0) {
                    uiModel = uiModel.copy(
                        lat = responseModel.latitude.toDoubleOrZero(),
                        long = responseModel.longitude.toDoubleOrZero()
                    )
                    _action.value = PinpointAction.MoveMap(latitude, longitude)
                }
                if ((responseModel.postalCode.isEmpty() && uiModel.postalCode.isEmpty()) || responseModel.districtId == 0L) {
                    locationNotFound(false)
                } else {
                    uiModel = uiModel.map(responseModel, null)
                    if (responseModel.errMessage.isNullOrEmpty()) {
                        _pinpointBottomSheet.value = BottomSheetState.LocationDetail(
                            title = uiModel.title,
                            description = uiModel.formattedAddress,
                            buttonPrimary = createButtonPrimary(success = true, enable = true),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    } else if (responseModel.errMessage?.contains(LOCATION_NOT_FOUND_MESSAGE) == true) {
                        locationNotFound(false)
                    }
                }
//                _districtLocation.value = Success(getDistrictMapper.map(districtLoc))
            } catch (e: Throwable) {
//                _districtLocation.value = Fail(e)
                checkErrorMessage(e.message.toString())
            }
        }
    }

    private fun checkErrorMessage(message: String) {
        when {
            message.contains(PinpointFragment.FOREIGN_COUNTRY_MESSAGE) -> locationOutOfReach()
            message.contains(PinpointFragment.LOCATION_NOT_FOUND_MESSAGE) -> locationNotFound(true)
            else -> {
                locationNotFound(true)
            }
        }
    }

    private fun locationOutOfReach() {
        _pinpointBottomSheet.value = BottomSheetState.LocationInvalid(
            type = BottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE,
            title = "Lokasi di luar jangkauan",
            detail = "Saat ini Tokopedia hanya melayani pengiriman di Indonesia saja. Pilih ulang lokasimu, ya.",
            imageUrl = TokopediaImageUrl.LOCATION_NOT_FOUND,
            showMapIllustration = false,
            buttonState = BottomSheetState.LocationInvalid.ButtonInvalidModel(
                show = false,
                state = BottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE
            )
        )
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
                _action.value = PinpointAction.MoveMap(
                    data.keroAddrGetDistrictCenter.district.latitude,
                    data.keroAddrGetDistrictCenter.district.longitude
                )
                getDistrictData(
                    data.keroAddrGetDistrictCenter.district.latitude,
                    data.keroAddrGetDistrictCenter.district.longitude
                )
//                _districtCenter.value = Success(mapDistrictCenterResponseToUiModel(data))
            } catch (e: Throwable) {
//                _districtCenter.value = Fail(e)
                // todo show toaster maybe?
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
            zipCodes = uiModel.postalCodeList,
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
                    _action.value = PinpointAction.MoveMap(
                        lat,
                        lng
                    )
                    getDistrictData(
                        lat,
                        lng
                    )

//                    _mapsGeocodeState.value = MapsGeocodeState.Success(this)
                } ?: kotlin.run {
                    locationNotFound(true)
                }
            },
            onError = {
                checkErrorMessage(it.message.orEmpty())
            }
        )
    }
}
