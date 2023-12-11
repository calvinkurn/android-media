package com.tokopedia.logisticaddaddress.features.pinpoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
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
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper.map
import com.tokopedia.logisticaddaddress.domain.model.mapsgeocode.MapsGeocodeParam
import com.tokopedia.logisticaddaddress.domain.usecase.MapsGeocodeUseCase
import com.tokopedia.logisticaddaddress.features.addeditaddress.uimodel.DistrictBoundaryResponseUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.ChoosePinpoint
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.MoveMap
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointBottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointUiModel
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
    private val districtBoundaryMapper: DistrictBoundaryMapper,
    private val mapsGeocodeUseCase: MapsGeocodeUseCase
) : ViewModel() {

    companion object {
        private const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."
        private const val LOCATION_NOT_FOUND_MESSAGE = "Lokasi gagal ditemukan"
    }

    // only set this for pinpoint webview
    private var addressDataFromWebview: SaveAddressDataModel? = null

    // district detail from autocomplete BE (search page)
    private var searchAddressData: PinpointUiModel? = null

    var uiModel = PinpointUiModel()
    var addressId = ""
    var whDistrictId = 0L
    var isEditWarehouse = false
    var uiState: AddressUiState? = null
    var source = ""

    /*
     this variable shows the real state of positive / negative flow in add address flow
     - true (positive flow) means address has pinpoint (search page -> pinpoint page -> accept pinpoint)
     - false (negative flow) means address has no pinpoint (search page -> isi alamat manual OR search page -> pinpoint page -> discard pinpoint)
     - null value means the flow is not decided yet (search page -> pinpoint page (hasn't decided to choose/discard the pinpoint)
     this variable is set as nullable because the value can't be changed once it's been set
     ex: search page -> isi alamat manual -> pilih pinpoint -> accept pinpoint. this case is still negative flow
     */
    private var _isPositiveFlow: Boolean? = null

    // this getter is used for analytic in the view fragment
    // when flow is undecided, default is true
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

    private val _pinpointBottomSheet = MutableLiveData<PinpointBottomSheetState>()

    val pinpointBottomSheet: LiveData<PinpointBottomSheetState>
        get() = _pinpointBottomSheet

    private val _map = MutableLiveData<MoveMap>()
    val map: LiveData<MoveMap>
        get() = _map

    fun onViewCreated(
        districtName: String = "",
        cityName: String = "",
        lat: Double = 0.0,
        long: Double = 0.0,
        districtId: Long = 0L,
        whDistrictId: Long = 0L,
        addressId: String = "",
        uiState: AddressUiState,
        isEditWarehouse: Boolean,
        source: String,
        isPositiveFlow: Boolean?,
        searchAddressData: SuggestedPlace?
    ) {
        this.uiModel = this.uiModel.copy(
            districtName = districtName,
            cityName = cityName,
            lat = lat,
            long = long,
            districtId = districtId
        )
        this.whDistrictId = whDistrictId
        this.addressId = addressId
        this.uiState = uiState
        this.isEditWarehouse = isEditWarehouse
        this.source = source
        this._isPositiveFlow = isPositiveFlow
        this.searchAddressData = searchAddressData?.toPinpointUiModel()
    }

    fun fetchData() {
        _pinpointBottomSheet.value = getInitialBottomSheetState()
        if (searchAddressData != null) {
            getLocationDetail()
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
                        _pinpointBottomSheet.value = PinpointBottomSheetState.LocationDetail(
                            title = uiModel.title,
                            description = uiModel.formattedAddress,
                            buttonPrimary = createButtonPrimary(success = true, enable = true),
                            buttonSecondary = createButtonSecondary(enable = true)
                        )
                    } else {
                        _pinpointBottomSheet.value = PinpointBottomSheetState.LocationDetail(
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

    private fun getLocationDetail() {
        if (searchAddressData?.districtId == 0L) {
            getDistrictDetail()
        } else {
            renderLocationDetail()
        }
    }

    private fun getDistrictDetail() {
        viewModelScope.launch {
            try {
                searchAddressData?.placeId?.takeIf { it.isNotEmpty() }?.run {
                    val res = getDistrict(
                        GetDistrictParam(
                            placeId = this,
                            isManageAddressFlow = true
                        )
                    )
                    if (res.keroPlacesGetDistrict.messageError.firstOrNull()?.contains(
                            LOCATION_NOT_FOUND_MESSAGE
                        ) == true
                    ) {
                        locationNotFound(false)
                    } else {
                        val data = res.keroPlacesGetDistrict.data
                        searchAddressData = searchAddressData?.copy(
                            title = data.title,
                            districtId = data.districtId,
                            districtName = data.districtName,
                            cityId = data.cityId,
                            cityName = data.cityName,
                            provinceId = data.provinceId,
                            provinceName = data.provinceName,
                            postalCode = data.postalCode,
                            lat = data.latitude.toDoubleOrZero(),
                            long = data.longitude.toDoubleOrZero()
                        )
                        renderLocationDetail()
                    }
                }
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }

    private fun renderLocationDetail() {
        searchAddressData?.run {
            if (lat != 0.0 && long != 0.0) {
                _map.value = MoveMap(lat, long)
            }
            if ((postalCode.isEmpty() && uiModel.postalCode.isEmpty()) || districtId == 0L) {
                locationNotFound(false)
            } else {
                if (validateDistrict(districtId)) {
                    uiModel = this
                    _pinpointBottomSheet.value = PinpointBottomSheetState.LocationDetail(
                        title = uiModel.title,
                        description = uiModel.formattedAddress,
                        buttonPrimary = createButtonPrimary(success = true, enable = true),
                        buttonSecondary = createButtonSecondary(enable = true)
                    )
                } else {
                    _pinpointBottomSheet.value = PinpointBottomSheetState.LocationDetail(
                        title = title,
                        description = formattedAddress,
                        buttonPrimary = createButtonPrimary(success = false, enable = false),
                        buttonSecondary = createButtonSecondary(enable = true)
                    )
                }
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

    private fun getDistrictCenter() {
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
        this.addressDataFromWebview = address
        this.uiModel = this.uiModel.copy(
            districtName = address.districtName,
            cityName = address.cityName,
            provinceName = address.provinceName,
            districtId = address.districtId,
            cityId = address.cityId,
            provinceId = address.provinceId,
            postalCode = address.postalCode,
            lat = address.latitude.toDoubleOrZero(),
            long = address.longitude.toDoubleOrZero(),
            title = uiModel.title
        )
    }

    /*
    By default, SaveAddressDataModel from Pinpoint page only contains data from PinpointUiModel
    In case of being redirected pinpoint webview (device doesn't support Google Map Service,
    this method returns the SaveAddressDataModel retrieved from Pinpoint Webview Page
     */
    fun getAddress(): SaveAddressDataModel {
        return addressDataFromWebview ?: SaveAddressDataModel(
            districtName = uiModel.districtName,
            cityName = uiModel.cityName,
            provinceName = uiModel.provinceName,
            districtId = uiModel.districtId,
            cityId = uiModel.cityId,
            provinceId = uiModel.provinceId,
            postalCode = uiModel.postalCode,
            latitude = uiModel.lat.toString(),
            longitude = uiModel.long.toString(),
            title = uiModel.title,
            formattedAddress = uiModel.formattedAddress,
            selectedDistrict = uiModel.selectedDistrict
        )
    }

    private fun getLocationFromLatLong() {
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
                PinpointAction.InvalidDistrictPinpoint(source = PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.SHOP_ADDRESS)
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

    private fun getGeocodeByDistrictAndCityName() {
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
                handleError(it)
            }
        )
    }

    fun onResultFromSearchAddress(searchAddressData: SuggestedPlace?, lat: Double, long: Double) {
        if (searchAddressData != null) {
            this.searchAddressData = searchAddressData.toPinpointUiModel()
            getLocationDetail()
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

    private fun getInitialBottomSheetState(): PinpointBottomSheetState {
        return PinpointBottomSheetState.LocationDetail(
            title = "",
            description = "",
            buttonPrimary = createButtonPrimary(success = false, enable = false),
            buttonSecondary = createButtonSecondary(enable = false)
        )
    }

    private fun createButtonSecondary(enable: Boolean): PinpointBottomSheetState.LocationDetail.SecondaryButtonUiModel {
        return PinpointBottomSheetState.LocationDetail.SecondaryButtonUiModel(
            show = uiState.isAdd(),
            state = uiState,
            enable = uiState.isAdd() && enable
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
                    PinpointAction.InvalidDistrictPinpoint(source = PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.ADD_ADDRESS_BUYER)
            }
        }
        return result
    }

    private fun createButtonPrimary(
        success: Boolean,
        enable: Boolean
    ): PinpointBottomSheetState.LocationDetail.PrimaryButtonUiModel {
        return PinpointBottomSheetState.LocationDetail.PrimaryButtonUiModel(
            show = true,
            success = success,
            enable = enable,
            addressUiState = uiState
        )
    }

    private fun locationNotFound(showIllustrationMap: Boolean) {
        val buttonState: Boolean =
            if (uiState.isPinpointOnly()) {
                false
            } else if (uiState.isEdit()) {
                !uiModel.hasPinpoint()
            } else {
                uiState.isAdd()
            }

        _pinpointBottomSheet.value = PinpointBottomSheetState.LocationInvalid(
            type = PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND,
            showMapIllustration = showIllustrationMap,
            buttonState = PinpointBottomSheetState.LocationInvalid.ButtonInvalidModel(show = buttonState),
            uiState = uiState,
            uiModel = uiModel
        )
    }

    private fun checkErrorMessage(message: String, showIllustationMap: Boolean = true) {
        when {
            message.contains(FOREIGN_COUNTRY_MESSAGE) -> locationOutOfReach()
            message.contains(LOCATION_NOT_FOUND_MESSAGE) -> locationNotFound(showIllustationMap)
            else -> {
                locationNotFound(showIllustationMap)
            }
        }
    }

    private fun locationOutOfReach() {
        _pinpointBottomSheet.value = PinpointBottomSheetState.LocationInvalid(
            type = PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE,
            showMapIllustration = false,
            buttonState = PinpointBottomSheetState.LocationInvalid.ButtonInvalidModel(
                show = false
            ),
            uiState = uiState,
            uiModel = uiModel
        )
    }

    private fun SuggestedPlace.toPinpointUiModel(): PinpointUiModel {
        return PinpointUiModel(
            placeId = placeId,
            title = title,
            districtName = districtName,
            cityName = cityName,
            provinceName = provinceName,
            districtId = districtId,
            cityId = cityId,
            provinceId = provinceId,
            postalCode = postalCode,
            lat = lat.toDoubleOrZero(),
            long = long.toDoubleOrZero()
        )
    }
}
