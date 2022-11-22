package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LOCATION_PASS
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LONG
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL

class PinpointWebviewFragment : BaseSessionWebViewFragment() {

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        val uri = Uri.parse(url)

        if (url.isNotEmpty() && url.contains(ApplinkConst.PINPOINT_WEBVIEW)) {
            val lat = uri.getQueryParameter(PARAM_LAT)
            val long = uri.getQueryParameter(PARAM_LONG)
            activity?.finish()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    companion object {
        fun newInstance(url: String, locationPass: LocationPass?, addressData: SaveAddressDataModel?): PinpointWebviewFragment {
            return PinpointWebviewFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_URL, url)
                    putParcelable(KEY_LOCATION_PASS, locationPass)
                    putParcelable(KEY_ADDRESS_DATA, addressData)
                }
            }
        }
    }
}
