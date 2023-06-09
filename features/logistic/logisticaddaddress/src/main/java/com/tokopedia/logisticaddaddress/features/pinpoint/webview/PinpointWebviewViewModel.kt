package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.constant.PinpointSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.AddAddressPinpointTracker
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.EditAddressPinpointTracker
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinpointWebviewViewModel @Inject constructor(
    private val repo: KeroRepository
) : ViewModel() {

    private val _pinpointState = MutableLiveData<PinpointWebviewState>()
    val pinpointState: LiveData<PinpointWebviewState>
        get() = _pinpointState

    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var locationPass: LocationPass? = null
    private var source: PinpointSource? = null

    fun saveLatLong(lat: Double, long: Double) {
        val param = "$lat,$long"
        viewModelScope.launch {
            try {
                val districtData = repo.getDistrictGeocode(
                    latlong = param,
                    isManageAddressFlow = source != null
                )
                val data = districtData.keroMapsAutofill.data
                locationPass?.let {
                    it.cityName = data.cityName
                    it.latitude = data.latitude
                    it.longitude = data.longitude
                    it.districtName = data.districtName
                }
                if (saveAddressDataModel != null) {
                    saveAddressDataModel = SaveAddressMapper.map(data, null, saveAddressDataModel)
                }
                sendSuccessTracker()
                _pinpointState.value = PinpointWebviewState.AddressDetailResult.Success(
                    locationPass,
                    saveAddressDataModel,
                    lat,
                    long
                )
            } catch (@Suppress("SwallowedException") e: Throwable) {
                sendFailedTracker()
                _pinpointState.value =
                    PinpointWebviewState.AddressDetailResult.Fail(e.message)
            }
        }
    }

    private fun sendFailedTracker() {
        source?.takeIf { it == PinpointSource.ADD_ADDRESS_NEGATIVE }?.let {
            _pinpointState.value = PinpointWebviewState.SendTracker.AddAddress(
                AddAddressPinpointTracker.ClickPilihLokasiNegative,
                "not success"
            )
        }
    }

    private fun sendSuccessTracker() {
        source?.let {
            when (it) {
                PinpointSource.EDIT_ADDRESS -> {
                    _pinpointState.value = PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni,
                        null
                    )
                }
                PinpointSource.ADD_ADDRESS_POSITIVE -> {
                    _pinpointState.value = PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiPositive,
                        null
                    )
                }
                PinpointSource.ADD_ADDRESS_NEGATIVE -> {
                    _pinpointState.value = PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiNegative,
                        "success"
                    )
                }
            }
        }
    }

    fun sendTracker(trackerId: String, label: String?) {
        source?.let {
            if (it == PinpointSource.EDIT_ADDRESS) {
                EditAddressPinpointTracker.getById(trackerId)?.let { tracker ->
                    _pinpointState.value = PinpointWebviewState.SendTracker.EditAddress(
                        tracker,
                        label
                    )
                }
            } else {
                AddAddressPinpointTracker.getById(trackerId)?.let { tracker ->
                    _pinpointState.value = PinpointWebviewState.SendTracker.AddAddress(
                        tracker,
                        label
                    )
                }
            }
        }
    }

    fun finishWithoutSaveChanges() {
        if (source != null) {
            when (source) {
                PinpointSource.EDIT_ADDRESS -> {
                    _pinpointState.value = PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickBackArrowPinpoint
                    )
                }
                else -> {
                    _pinpointState.value = PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickBackArrowPinpoint
                    )
                }
            }
        }
        _pinpointState.value = PinpointWebviewState.FinishActivity
    }

    fun setAddressDataModel(data: SaveAddressDataModel?) {
        saveAddressDataModel = data
    }

    fun setLocationPass(data: LocationPass?) {
        locationPass = data
    }

    fun setSource(data: String) {
        data.takeIf { value -> value.isNotEmpty() }?.run {
            try {
                source = PinpointSource.valueOf(this)
            } catch (@Suppress("SwallowedException") e: IllegalArgumentException) {
                // no op
            }
        }
    }
}
