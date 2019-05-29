package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation.DistrictRecommendationBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.form_add_new_address_data_item.*
import kotlinx.android.synthetic.main.form_add_new_address_default_item.*
import kotlinx.android.synthetic.main.form_add_new_address_mismatch_data_item.*
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
    private var labelRumah: String? = "Rumah"
    private var isMismatch: Boolean? = false
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private lateinit var etLabelAlamat: EditText
    private lateinit var etNamaPenerima: EditText
    private lateinit var etNoPonsel: EditText

    @Inject
    lateinit var presenter: AddEditAddressPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        private const val CURRENT_IS_MISMATCH = "CURRENT_IS_MISMATCH"
        private const val CURRENT_SAVE_DATA_UI_MODEL = "CURRENT_SAVE_DATA_UI_MODEL"

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(extra: Bundle): AddEditAddressFragment {
            return AddEditAddressFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(CURRENT_IS_MISMATCH, extra.getBoolean(AddressConstants.EXTRA_IS_MISMATCH))
                    putParcelable(CURRENT_SAVE_DATA_UI_MODEL, extra.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isMismatch = arguments?.getBoolean(CURRENT_IS_MISMATCH)
            saveAddressDataModel = arguments?.getParcelable(CURRENT_SAVE_DATA_UI_MODEL)
            currentLat = saveAddressDataModel?.latitude?.toDouble()
            currentLong = saveAddressDataModel?.longitude?.toDouble()
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
            et_label_address_mismatch.setText(labelRumah)
            et_receiver_name_mismatch.setText(userSession.name)
            et_phone_mismatch.setText(userSession.phoneNumber)
            et_kota_kecamatan.setOnClickListener {
                showDistrictRecommendationBottomSheet()
            }

        } else {
            map_view_detail.onCreate(savedInstanceState)
            map_view_detail.getMapAsync(this)
            ll_mismatch.visibility = View.GONE
            ll_normal.visibility = View.VISIBLE
            btn_map.text = getString(R.string.change_pinpoint)
            tv_address_based_on_pinpoint.text = "${saveAddressDataModel?.title}, ${saveAddressDataModel?.formattedAddress}"
            et_detail_address.setText(saveAddressDataModel?.editDetailAddress)
            et_label_address.setText(labelRumah)
            et_receiver_name.setText(userSession.name)
            et_phone.setText(userSession.phoneNumber)
        }

        /*btn_map.setOnClickListener {
            presenter.changePinpoint(currentLat, currentLong)
            activity?.finish()
        }*/

        btn_save_address.setOnClickListener {
            setSaveAddressModel()
            presenter.saveAddress(saveAddressDataModel)
        }
    }

    private fun showDistrictRecommendationBottomSheet() {
        val districtRecommendationBottomSheetFragment =
                DistrictRecommendationBottomSheetFragment.newInstance()
        // districtRecommendationBottomSheetFragment.setActionListener(this)
        districtRecommendationBottomSheetFragment.show(fragmentManager, "")
    }

    private fun setSaveAddressModel() {
        saveAddressDataModel?.address1 = "${saveAddressDataModel?.title} ${et_detail_address.text}, ${saveAddressDataModel?.formattedAddress}"
        saveAddressDataModel?.address2 = "$currentLat,$currentLong"
        saveAddressDataModel?.addressName = etLabelAlamat.text.toString()
        saveAddressDataModel?.receiverName = etNamaPenerima.text.toString()
        saveAddressDataModel?.phone = etNoPonsel.text.toString()
    }

    override fun onSuccessAddAddress(addAddressDataUiModel: AddAddressDataUiModel, saveAddressDataModel: SaveAddressDataModel) {
        finishActivity(saveAddressDataModel)
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
            if (mismatch_map_view_detail != null) {
                mismatch_map_view_detail.onDestroy()
            }
        } else {
            if (map_view_detail != null) {
                map_view_detail.onDestroy()
            }
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

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        val intent = activity?.intent
        intent?.putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}