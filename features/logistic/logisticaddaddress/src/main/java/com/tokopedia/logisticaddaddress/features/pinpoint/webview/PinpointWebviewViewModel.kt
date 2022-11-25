package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinpointWebviewViewModel @Inject constructor(
    private val repo: KeroRepository,
    private val saveAddressMapper: SaveAddressMapper
) : ViewModel() {

    private val _pinpointState = MutableLiveData<Result<Pair<Double, Double>>>()
    val pinpointState: LiveData<Result<Pair<Double, Double>>>
        get() = _pinpointState

    var saveAddressDataModel: SaveAddressDataModel? = null
    var locationPass: LocationPass? = null

    fun saveLatLong(lat: Double, long: Double) {
        val param = "$lat,$long"
        viewModelScope.launch {
            try {
                val districtData = repo.getDistrictGeocode(param)
                val data = districtData.keroMapsAutofill.data
                locationPass?.let {
                    it.cityName = data.cityName
                    it.latitude = data.latitude
                    it.longitude = data.longitude
                    it.districtName = data.districtName
                }
                if (saveAddressDataModel != null) {
                    saveAddressDataModel = saveAddressMapper.map(data, null, saveAddressDataModel)
                }
                _pinpointState.value = Success(Pair(data.latitude.toDoubleOrZero(), data.longitude.toDoubleOrZero()))
            } catch (e: Throwable) {
                _pinpointState.value = Fail(e)
            }
        }
    }
}
