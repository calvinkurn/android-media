package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.AddAddressPinpointTracker
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.EditAddressPinpointTracker

sealed class PinpointWebviewState {
    sealed class AddressDetailResult : PinpointWebviewState() {
        data class Success(
            val locationPass: LocationPass?,
            val saveAddressDataModel: SaveAddressDataModel?,
            val latitude: Double,
            val longitude: Double
        ) : AddressDetailResult()

        data class Fail(val message: String?) : AddressDetailResult()
    }

    sealed class SendTracker : PinpointWebviewState() {
        data class AddAddress(val tracker: AddAddressPinpointTracker, val label: String? = null) : SendTracker()
        data class EditAddress(val tracker: EditAddressPinpointTracker, val label: String? = null) : SendTracker()
    }

    object FinishActivity : PinpointWebviewState()
}
