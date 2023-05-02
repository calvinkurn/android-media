package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LAT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LOCATION_PASS
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LONG_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_SOURCE_PINPOINT
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_TRACKER
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_TRACKER_LABEL
import com.tokopedia.logisticaddaddress.di.pinpointwebview.DaggerPinpointWebviewComponent
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.AddAddressPinpointTracker
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.EditAddressPinpointTracker
import com.tokopedia.logisticaddaddress.utils.ParcelableHelper.parcelable
import com.tokopedia.unifycomponents.Toaster
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

    override fun initInjector() {
        DaggerPinpointWebviewComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        observeState()
        setOnBackPressed()
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.finishWithoutSaveChanges()
                }
            }
        )
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        val uri = Uri.parse(url)

        if (url.isNotEmpty() && url.contains(ApplinkConst.PINPOINT_WEBVIEW)) {
            val lat = uri.getQueryParameter(PARAM_LAT)?.toDoubleOrNull()
            val long = uri.getQueryParameter(PARAM_LONG)?.toDoubleOrNull()
            if (lat != null && long != null) {
                viewModel.saveLatLong(lat, long)
            }

            val trackerId = uri.getQueryParameter(PARAM_TRACKER)
            val trackerLabel = uri.getQueryParameter(PARAM_TRACKER_LABEL)
            trackerId?.takeIf { it.isNotEmpty() }?.let { viewModel.sendTracker(it, trackerLabel) }
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    private fun getData() {
        arguments?.let {
            it.parcelable<LocationPass>(KEY_LOCATION_PASS)
                ?.let { locationPass -> viewModel.setLocationPass(locationPass) }
            it.parcelable<SaveAddressDataModel>(KEY_ADDRESS_DATA)
                ?.let { addressData -> viewModel.setAddressDataModel(addressData) }
            it.getString(KEY_SOURCE_PINPOINT)?.let { source ->
                viewModel.setSource(source)
            }
        }
    }

    private fun observeState() {
        viewModel.pinpointState.observe(viewLifecycleOwner, {
            when (it) {
                is PinpointWebviewState.AddressDetailResult.Success -> {
                    finishActivity(
                        it.saveAddressDataModel,
                        it.locationPass,
                        it.latitude,
                        it.longitude
                    )
                }
                is PinpointWebviewState.AddressDetailResult.Fail -> {
                    showErrorToaster(it.message)
                }
                is PinpointWebviewState.SendTracker.AddAddress -> {
                    sendAddAddressTracker(it.tracker, it.label)
                }
                is PinpointWebviewState.SendTracker.EditAddress -> {
                    sendEditAddressTracker(it.tracker)
                }
                is PinpointWebviewState.FinishActivity -> {
                    finishActivity()
                }
            }
        })
    }

    private fun finishActivity(
        addressData: SaveAddressDataModel? = null,
        locationPass: LocationPass? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        activity?.run {
            if (latitude != null && longitude != null) {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        locationPass?.let { data -> putExtra(KEY_LOCATION_PASS, data) }
                        addressData?.let { data -> putExtra(KEY_ADDRESS_DATA, data) }
                        putExtra(KEY_LAT_ID, latitude)
                        putExtra(KEY_LONG_ID, longitude)
                    }
                )
            }
            finish()
        }
    }

    private fun showErrorToaster(message: String?) {
        if (message != null) {
            view?.let { v -> Toaster.build(v, message, type = Toaster.TYPE_ERROR).show() }
        }
    }

    private fun sendAddAddressTracker(tracker: AddAddressPinpointTracker, label: String?) {
        when (tracker) {
            AddAddressPinpointTracker.ClickFieldCariLokasi -> {
                AddNewAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
            }
            AddAddressPinpointTracker.ClickDropdownSuggestion -> {
                AddNewAddressRevampAnalytics.onClickDropdownSuggestion(userSession.userId)
            }
            AddAddressPinpointTracker.ClickGunakanLokasiSaatIniSearch -> {
                AddNewAddressRevampAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            }
            AddAddressPinpointTracker.ClickAllowLocationSearch -> {
                AddNewAddressRevampAnalytics.onClickAllowLocationSearch(userSession.userId)
            }
            AddAddressPinpointTracker.ClickDontAllowLocationSearch -> {
                AddNewAddressRevampAnalytics.onClickDontAllowLocationSearch(userSession.userId)
            }
            AddAddressPinpointTracker.ClickAktifkanLayananLokasiSearch -> {
                AddNewAddressRevampAnalytics.onClickAktifkanLayananLokasiSearch(userSession.userId)
            }
            AddAddressPinpointTracker.ClickXOnBlockGpsSearch -> {
                AddNewAddressRevampAnalytics.onClickXOnBlockGpsSearch(userSession.userId)
            }
            AddAddressPinpointTracker.ClickBackArrowSearch -> {
                AddNewAddressRevampAnalytics.onClickBackArrowSearch(userSession.userId)
            }
            AddAddressPinpointTracker.ImpressBottomSheetAlamatTidakTerdeteksi -> {
                AddNewAddressRevampAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(userSession.userId)
            }
            AddAddressPinpointTracker.ClickIsiAlamatManualUndetectedLocation -> {
                AddNewAddressRevampAnalytics.onClickIsiAlamatManualUndetectedLocation(userSession.userId)
            }
            AddAddressPinpointTracker.ImpressBottomSheetOutOfIndo -> {
                AddNewAddressRevampAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
            }
            AddAddressPinpointTracker.ClickIsiAlamatOutOfIndo -> {
                AddNewAddressRevampAnalytics.onClickIsiAlamatOutOfIndo(userSession.userId)
            }
            AddAddressPinpointTracker.ClickCariUlangAlamat -> {
                AddNewAddressRevampAnalytics.onClickCariUlangAlamat(userSession.userId)
            }
            AddAddressPinpointTracker.ClickGunakanLokasiSaatIniPinpoint -> {
                AddNewAddressRevampAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
            }
            AddAddressPinpointTracker.ClickAllowLocationPinpoint -> {
                AddNewAddressRevampAnalytics.onClickAllowLocationPinpoint(userSession.userId)
            }
            AddAddressPinpointTracker.ClickDontAllowLocationPinpoint -> {
                AddNewAddressRevampAnalytics.onClickDontAllowLocationPinpoint(userSession.userId)
            }
            AddAddressPinpointTracker.ClickAktifkanLayananLokasiPinpoint -> {
                AddNewAddressRevampAnalytics.onClickAktifkanLayananLokasiPinpoint(userSession.userId)
            }
            AddAddressPinpointTracker.ClickXOnBlockGpsPinpoint -> {
                AddNewAddressRevampAnalytics.onClickXOnBlockGpsPinpoint(userSession.userId)
            }
            AddAddressPinpointTracker.ClickIconQuestion -> {
                AddNewAddressRevampAnalytics.onClickIconQuestion(userSession.userId)
            }
            AddAddressPinpointTracker.ClickPilihLokasiPositive -> {
                AddNewAddressRevampAnalytics.onClickPilihLokasiPositive(userSession.userId)
            }
            AddAddressPinpointTracker.ClickPilihLokasiNegative -> {
                label?.run {
                    AddNewAddressRevampAnalytics.onClickPilihLokasiNegative(
                        userSession.userId,
                        this
                    )
                }
            }
            AddAddressPinpointTracker.ViewToasterPinpointTidakSesuai -> {
                AddNewAddressRevampAnalytics.onViewToasterPinpointTidakSesuai(userSession.userId)
            }
            AddAddressPinpointTracker.ClickBackArrowPinpoint -> {
                AddNewAddressRevampAnalytics.onClickBackArrowPinpoint(userSession.userId)
            }
        }
    }

    private fun sendEditAddressTracker(tracker: EditAddressPinpointTracker) {
        when (tracker) {
            EditAddressPinpointTracker.ClickGunakanLokasiSaatIniPinpoint -> {
                EditAddressRevampAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
            }
            EditAddressPinpointTracker.ClickCariUlangAlamat -> {
                EditAddressRevampAnalytics.onClickCariUlangAlamat(userSession.userId)
            }
            EditAddressPinpointTracker.ClickPilihLokasiIni -> {
                EditAddressRevampAnalytics.onClickPilihLokasiIni(userSession.userId)
            }
            EditAddressPinpointTracker.ImpressBottomSheetOutOfIndo -> {
                EditAddressRevampAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
            }
            EditAddressPinpointTracker.ImpressBottomSheetAlamatTidakTerdeteksi -> {
                EditAddressRevampAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(userSession.userId)
            }
            EditAddressPinpointTracker.ClickFieldCariLokasi -> {
                EditAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
            }
            EditAddressPinpointTracker.ClickDropdownSuggestion -> {
                EditAddressRevampAnalytics.onClickDropdownSuggestionAlamat(userSession.userId)
            }
            EditAddressPinpointTracker.ClickGunakanLokasiSaatIniSearch -> {
                EditAddressRevampAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            }
            EditAddressPinpointTracker.ClickBackArrowSearch -> {
                EditAddressRevampAnalytics.onClickBackArrowSearch(userSession.userId)
            }
            EditAddressPinpointTracker.ClickBackArrowPinpoint -> {
                EditAddressRevampAnalytics.onClickBackPinpoint(userSession.userId)
            }
        }
    }

    companion object {
        fun newInstance(
            url: String,
            locationPass: LocationPass?,
            addressData: SaveAddressDataModel?,
            source: String?
        ): PinpointWebviewFragment {
            return PinpointWebviewFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_URL, url)
                    putParcelable(KEY_LOCATION_PASS, locationPass)
                    putParcelable(KEY_ADDRESS_DATA, addressData)
                    putString(KEY_SOURCE_PINPOINT, source)
                }
            }
        }
    }
}
