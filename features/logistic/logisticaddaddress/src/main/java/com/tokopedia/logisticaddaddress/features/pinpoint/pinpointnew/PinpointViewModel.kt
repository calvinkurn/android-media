package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.param.GetDistrictGeoCodeParam
import com.tokopedia.logisticCommon.domain.param.GetDistrictParam
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictBoundariesUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictCenterUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictGeoCodeUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.isEdit
import com.tokopedia.logisticCommon.uimodel.isEditOrPinpointOnly
import com.tokopedia.logisticCommon.uimodel.isPinpointOnly
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper.map
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.PinpointFragment
import com.tokopedia.logisticaddaddress.features.pinpoint.PinpointFragment.Companion.LOCATION_NOT_FOUND_MESSAGE
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.BottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.ChoosePinpoint
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MoveMap
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

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
    var isEditWarehouse = false
    var uiState: AddressUiState? = null
    var source = ""

    // this variable shows the real state of positive / negative flow in add address flow
    // positive flow means address has pinpoint (search page -> pinpoint page -> accept pinpoint page)
    // negative flow means address has no pinpoint (search page -> isi alamat manual OR search page -> pinpoint page -> discard pinpoint)
    // null value means the flow is not decided yet
    // ex: search page -> pinpoint page (hasn't decided to choose/discard the pinpoint)
    // this variable is set as nullable because the value can't be changed once it's been set
    // ex: user already click isi alamat manual, then decide to put pinpoint. this case is still negative flow
    private var _isPositiveFlow: Boolean? = null

    // this getter is used for analytic in the view fragment
    // the default is true
    val isPositiveFlow: Boolean
        get() = _isPositiveFlow ?: true

    private val _districtBoundary = MutableLiveData<Result<DistrictBoundaryResponseUiModel>>()
    val districtBoundary: LiveData<Result<DistrictBoundaryResponseUiModel>>
        get() = _districtBoundary

    private val _action = SingleLiveEvent<PinpointAction>()
    val action: SingleLiveEvent<PinpointAction>
        get() = _action

    private val _choosePinpoint = SingleLiveEvent<ChoosePinpoint>()
    val choosePinpoint: SingleLiveEvent<ChoosePinpoint>
        get() = _choosePinpoint

    private val _pinpointBottomSheet = MutableLiveData<BottomSheetState>()

    val pinpointBottomSheet: LiveData<BottomSheetState>
        get() = _pinpointBottomSheet

    private val _map = MutableLiveData<MoveMap>()
    val map: LiveData<MoveMap>
        get() = _map

    fun onViewCreated(
        districtName: String = "",
        cityName: String = "",
        lat: Double = 0.0,
        long: Double = 0.0,
        placeId: String = "",
        districtId: Long = 0L,
        whDistrictId: Long = 0L,
        addressId: String = "",
        uiState: AddressUiState,
        isEditWarehouse: Boolean,
        source: String,
        isPositiveFlow: Boolean?
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
        this.isEditWarehouse = isEditWarehouse
        this.source = source
        this._isPositiveFlow = isPositiveFlow
    }

    fun fetchData() {
        _pinpointBottomSheet.value = getInitialBottomSheetState()
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
                    val validateDistrict =
                        validateDistrict(districtData.keroMapsAutofill.data.districtId)
                    if (validateDistrict) {
                        uiModel = uiModel.map(districtData.keroMapsAutofill.data)
                        _pinpointBottomSheet.value = BottomSheetState.LocationDetail(
                            title = uiModel.title,
                            description = uiModel.formattedAddress,
                            buttonPrimary = createButtonPrimary(success = true, enable = true),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    } else {
                        _pinpointBottomSheet.value = BottomSheetState.LocationDetail(
                            title = districtData.keroMapsAutofill.data.title,
                            description = districtData.keroMapsAutofill.data.formattedAddress,
                            buttonPrimary = createButtonPrimary(success = false, enable = false),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    }
                } else {
                    districtData.keroMapsAutofill.messageError.getOrNull(0)?.run {
                        checkErrorMessage(this, showIllustationMap = false)
                    }
                }
            } catch (e: Throwable) {
                handleError(e)
            }
        }
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
                    _map.value = MoveMap(latitude, longitude)
                }
                if ((responseModel.postalCode.isEmpty() && uiModel.postalCode.isEmpty()) || responseModel.districtId == 0L) {
                    locationNotFound(false)
                } else {
                    if (validateDistrict(responseModel.districtId)) {
                        uiModel = uiModel.map(responseModel)
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
                    } else {
                        _pinpointBottomSheet.value = BottomSheetState.LocationDetail(
                            title = responseModel.title,
                            description = responseModel.formattedAddress,
                            buttonPrimary = createButtonPrimary(success = false, enable = false),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    }
                }
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }

    fun getDistrictBoundaries() {
        viewModelScope.launch {
            try {
                if (uiState.isAdd() && uiModel.districtId != 0L) {
                    val districtBoundary = getDistrictBoundaries(uiModel.districtId)
                    _districtBoundary.value =
                        Success(districtBoundaryMapper.mapDistrictBoundaryNew(districtBoundary))
                }
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }

    fun getDistrictCenter() {
        viewModelScope.launch {
            try {
                val data = getDistrictCenter(uiModel.districtId)
                _map.value = MoveMap(
                    data.keroAddrGetDistrictCenter.district.latitude,
                    data.keroAddrGetDistrictCenter.district.longitude
                )
                getDistrictData(
                    data.keroAddrGetDistrictCenter.district.latitude,
                    data.keroAddrGetDistrictCenter.district.longitude
                )
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }

    /*
    this method is to save pinpoint result from webview pinpoint
     */
    fun setAddress(address: SaveAddressDataModel) {
        this.saveAddressDataModel = address
    }

    /*
    By default, SaveAddressDataModel from Pinpoint page only contains data from PinpointUiModel
    In case of being redirected pinpoint webview (device doesn't support Google Map Service,
    this method returns the SaveAddressDataModel retrieved from Pinpoint Webview Page
     */
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

    fun getLocationFromLatLong() {
        getDistrictData(
            uiModel.lat,
            uiModel.long
        )
    }

    /*
    Validate pinpoint's district on click save only when caller is shop edit address
    else, return the pinpoint result
     */
    fun validatePinpoint() {
        if (isEditWarehouse && whDistrictId != 0L && whDistrictId != uiModel.districtId) {
            _action.value =
                PinpointAction.InvalidDistrictPinpoint("Lokasi pinpoint (peta) tidak sesuai dengan kota/kecamatan mu. Harap ulangi pinpoint.")
        } else if (uiState.isPinpointOnly()) {
            _choosePinpoint.value = ChoosePinpoint.SetPinpointResult(
                saveAddressDataModel = getAddress(),
                pinpointUiModel = uiModel
            )
        } else {
            _choosePinpoint.value = ChoosePinpoint.GoToAddressForm(
                saveChanges = true,
                pinpointUiModel = uiModel,
                addressState = uiState,
                source = source,
                isPositiveFlow = _isPositiveFlow ?: true
            )
        }
    }

    fun discardPinpoint() {
        _choosePinpoint.value = ChoosePinpoint.GoToAddressForm(
            saveChanges = false,
            pinpointUiModel = uiModel,
            addressState = uiState,
            source = source,
            isPositiveFlow = _isPositiveFlow ?: false
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
                    _map.value = MoveMap(
                        lat,
                        lng
                    )
                    getDistrictData(
                        lat,
                        lng
                    )
                } ?: kotlin.run {
                    locationNotFound(true)
                }
            },
            onError = {
                checkErrorMessage(it.message.orEmpty())
            }
        )
    }

    fun onResultFromSearchAddress(placeId: String, lat: Double, long: Double) {
        if (placeId.isNotEmpty()) {
            this.uiModel = this.uiModel.copy(placeId)
            getDistrictLocation(placeId)
        } else if (lat != 0.0 && long != 0.0) {
            _map.value = MoveMap(lat, long)
            getDistrictData(lat, long)
        }
    }

    private fun handleError(e: Throwable) {
        when (e) {
            is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                _action.value = PinpointAction.NetworkError(e.message.orEmpty())
            }
            else -> {
                checkErrorMessage(e.message.toString())
            }
        }
    }

    private fun getInitialBottomSheetState(): BottomSheetState {
        return BottomSheetState.LocationDetail(
            title = "",
            description = "",
            buttonPrimary = BottomSheetState.LocationDetail.PrimaryButtonUiModel(
                text = if (uiState.isEditOrPinpointOnly()) "Pilih lokasi ini" else "",
                show = true,
                enable = false,
                state = BottomSheetState.LocationDetail.PrimaryButtonUiModel.PrimaryButtonState(
                    uiState,
                    false
                )
            ),
            buttonSecondary = BottomSheetState.LocationDetail.LocationDetailButtonUiModel(
                show = uiState.isAdd(),
                enable = false,
                state = uiState
            )
        )
    }

    private fun createButtonSecondary(enable: Boolean): BottomSheetState.LocationDetail.LocationDetailButtonUiModel {
        return BottomSheetState.LocationDetail.LocationDetailButtonUiModel(
            show = uiState.isAdd(),
            state = uiState,
            enable = enable
        )
    }

    /*
    This function only called when user in add address negative case
     */
    private fun validateDistrict(newDistrictId: Long): Boolean {
        var result = true
        if (uiState.isAdd() && uiModel.districtId != 0L && _isPositiveFlow == false) {
            // check new district id
            val isDistrictTheSame = newDistrictId == uiModel.districtId
            if (!isDistrictTheSame) {
                result = false
                // is polygon but coordinate not in selected district
                _action.value =
                    PinpointAction.InvalidDistrictPinpoint("Pastikan pinpoint sesuai kota & kecamatan pilihanmu.")
            }
        }
        return result
    }

    private fun createButtonPrimary(
        success: Boolean,
        enable: Boolean
    ): BottomSheetState.LocationDetail.PrimaryButtonUiModel {
        return BottomSheetState.LocationDetail.PrimaryButtonUiModel(
            show = true,
            state = BottomSheetState.LocationDetail.PrimaryButtonUiModel.PrimaryButtonState(
                addressUiState = uiState,
                success = success
            ),
            enable = enable,
            text = if (uiState.isPinpointOnly()) {
                "Pilih Lokasi Ini"
            } else {
                "Pilih Lokasi & Lanjut Isi Alamat"
            }
        )
    }

    private fun locationNotFound(showIllustationMap: Boolean) {
        val invalidLocationDetail =
            if (uiModel.hasPinpoint() || uiState.isPinpointOnly()) {
                "Tenang, kamu bisa pilih ulang lokasimu atau pakai lokasimu sebelumnya."
            } else {
                "Tenang, kamu bisa pilih ulang lokasimu atau tetap menyimpan alamat tanpa pinpoint."
            }
        val buttonState: BottomSheetState.LocationInvalid.ButtonInvalidModel =
            if (uiState.isPinpointOnly()) {
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

    private fun checkErrorMessage(message: String, showIllustationMap: Boolean = true) {
        when {
            message.contains(PinpointFragment.FOREIGN_COUNTRY_MESSAGE) -> locationOutOfReach()
            message.contains(PinpointFragment.LOCATION_NOT_FOUND_MESSAGE) -> locationNotFound(showIllustationMap)
            else -> {
                locationNotFound(showIllustationMap)
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
}
