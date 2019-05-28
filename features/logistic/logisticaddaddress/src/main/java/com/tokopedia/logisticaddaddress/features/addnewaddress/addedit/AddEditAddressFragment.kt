package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.form_add_new_address_data_item.*
import kotlinx.android.synthetic.main.form_add_new_address_default_item.*
import kotlinx.android.synthetic.main.fragment_add_edit_new_address.*
import kotlinx.android.synthetic.main.header_add_new_address_data_item.*
import kotlinx.android.synthetic.main.header_add_new_address_mismatch_data_item.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressFragment: BaseDaggerFragment(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, ResultCallback<LocationSettingsResult>, AddEditAddressListener{

    private var googleMap: GoogleMap? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private var currentDetailAddress: String? = ""
    private var labelRumah: String? = "Rumah"
    private var isMismatch: Boolean? = false

    @Inject
    lateinit var presenter: AddEditAddressPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        // private const val CURRENT_LAT = "CURRENT_LAT"
        // private const val CURRENT_LONG = "CURRENT_LONG"
        // private const val CURRENT_DETAIL_ADDRESS = "CURRENT_DETAIL_ADDRESS"
        private const val CURRENT_IS_MISMATCH = "CURRENT_IS_MISMATCH"
        private const val CURRENT_SAVE_DATA_UI_MODEL = "CURRENT_SAVE_DATA_UI_MODEL"

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(extra: Bundle): AddEditAddressFragment {
            return AddEditAddressFragment().apply {
                arguments = Bundle().apply {
                    // putDouble(CURRENT_LAT, extra.getDouble(AddressConstants.EXTRA_LAT))
                    // putDouble(CURRENT_LONG, extra.getDouble(AddressConstants.EXTRA_LONG))
                    // putString(CURRENT_DETAIL_ADDRESS, extra.getString(AddressConstants.EXTRA_DETAIL_ADDRESS))
                    putBoolean(CURRENT_IS_MISMATCH, extra.getBoolean(AddressConstants.EXTRA_IS_MISMATCH))
                    putParcelable(CURRENT_SAVE_DATA_UI_MODEL, extra.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            // currentLat = arguments?.getDouble("CURRENT_LAT")
            // currentLong = arguments?.getDouble("CURRENT_LONG")
            // currentDetailAddress = arguments?.getString("CURRENT_DETAIL_ADDRESS")
            isMismatch = arguments?.getBoolean(CURRENT_IS_MISMATCH)
            saveAddressDataModel = arguments?.getParcelable(CURRENT_SAVE_DATA_UI_MODEL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_new_address, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.connectGoogleApi(this)
        back_button_detail.setOnClickListener {
            if (this.isMismatch!!) {
                mismatch_map_view_detail.onPause()
            } else {
                map_view_detail.onPause()
            }

            presenter.disconnectGoogleApi()
            activity?.finish()
        }
        if (this.isMismatch!!) {
            mismatch_map_view_detail.onCreate(savedInstanceState)
            mismatch_map_view_detail.getMapAsync(this)
            ll_normal.visibility = View.GONE
            ll_mismatch.visibility = View.VISIBLE
            mismatch_btn_map.text = getString(R.string.define_pinpoint)
        } else {
            map_view_detail.onCreate(savedInstanceState)
            map_view_detail.getMapAsync(this)
            ll_mismatch.visibility = View.GONE
            ll_normal.visibility = View.VISIBLE
            btn_map.text = getString(R.string.change_pinpoint)
            tv_address_based_on_pinpoint.text = "${saveAddressDataModel?.title}, ${saveAddressDataModel?.formattedAddress}"
        }

        et_label_address.setText(labelRumah)
        et_receiver_name.setText(userSession.name)
        et_phone.setText(userSession.phoneNumber)

        /*btn_map.setOnClickListener {
            presenter.changePinpoint(currentLat, currentLong)
            activity?.finish()
        }*/

        btn_save_address.setOnClickListener {
            setSaveAddressModel()
            presenter.saveAddress(saveAddressDataModel)
        }
    }

    private fun setSaveAddressModel() {
        saveAddressDataModel?.address1 = "${saveAddressDataModel?.title} ${et_detail_address.text}, ${saveAddressDataModel?.formattedAddress}"
        saveAddressDataModel?.address2 = "$currentLat,$currentLong"
        saveAddressDataModel?.addressName = et_label_address.text.toString()
        saveAddressDataModel?.receiverName = et_receiver_name.text.toString()
        saveAddressDataModel?.phone = et_phone.text.toString()
    }

    override fun onSuccessAddAddress(addAddressDataUiModel: AddAddressDataUiModel) {
        activity?.finish()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        MapsInitializer.initialize(activity!!)
        moveMap(PinpointMapUtils.generateLatLng(currentLat, currentLong))
    }

    override fun moveMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(15f)
                .bearing(0f)
                .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResult(locationSettingsResult: LocationSettingsResult) {
    }

    override fun getScreenName(): String {
        return AddEditAddressFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@AddEditAddressFragment)
            presenter.attachView(this@AddEditAddressFragment)
        }
    }

    override fun onResume() {
        if (this.isMismatch!!) {
            mismatch_map_view_detail.onResume()
        } else {
            map_view_detail.onResume()
        }

        super.onResume()
    }

    override fun onPause() {
        if (this.isMismatch!!) {
            mismatch_map_view_detail.onPause()
        } else {
            map_view_detail.onPause()
        }
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.disconnectGoogleApi()
    }

    override fun onDestroy() {
        if (this.isMismatch!!) {
            mismatch_map_view_detail.onDestroy()
        } else {
            map_view_detail.onDestroy()
        }
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (this.isMismatch!!) {
            mismatch_map_view_detail.onLowMemory()
        } else {
            map_view_detail.onLowMemory()
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}