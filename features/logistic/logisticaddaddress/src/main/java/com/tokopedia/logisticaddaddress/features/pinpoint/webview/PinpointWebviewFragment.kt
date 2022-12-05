package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LAT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LOCATION_PASS
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LONG_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LONG
import com.tokopedia.logisticaddaddress.di.pinpointwebview.DaggerPinpointWebviewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

class PinpointWebviewFragment : BaseSessionWebViewFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PinpointWebviewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PinpointWebviewViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        observeDistrictDetail()
    }

    private fun getData() {
        arguments?.let {
            it.getParcelable<LocationPass>(KEY_LOCATION_PASS)
                ?.let { locationPass -> viewModel.locationPass = locationPass }
            it.getParcelable<SaveAddressDataModel>(KEY_ADDRESS_DATA)
                ?.let { addressData -> viewModel.saveAddressDataModel = addressData }
        }
    }

    private fun observeDistrictDetail() {
        viewModel.pinpointState.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    finishActivity(it.data)
                }
                is Fail -> {
                    showErrorToaster(it.throwable.message)
                }
            }
        })
    }

    private fun showErrorToaster(message: String?) {
        if (message != null) {
            view?.let { v -> Toaster.build(v, message, type = Toaster.TYPE_ERROR) }
        }
    }

    private fun finishActivity(coordinate: Pair<Double, Double>) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    viewModel.locationPass?.let { data -> putExtra(KEY_LOCATION_PASS, data) }
                    viewModel.saveAddressDataModel?.let { data -> putExtra(KEY_ADDRESS_DATA, data) }
                    putExtra(KEY_LAT_ID, coordinate.first)
                    putExtra(KEY_LONG_ID, coordinate.second)
                }
            )
            finish()
        }
    }

    override fun initInjector() {
        DaggerPinpointWebviewComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        val uri = Uri.parse(url)

        if (url.isNotEmpty() && url.contains(ApplinkConst.PINPOINT_WEBVIEW)) {
            val lat = uri.getQueryParameter(PARAM_LAT)?.toDoubleOrNull()
            val long = uri.getQueryParameter(PARAM_LONG)?.toDoubleOrNull()
            if (lat != null && long != null) {
                viewModel.saveLatLong(lat, long)
            }
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    companion object {
        fun newInstance(
            url: String,
            locationPass: LocationPass?,
            addressData: SaveAddressDataModel?
        ): PinpointWebviewFragment {
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
