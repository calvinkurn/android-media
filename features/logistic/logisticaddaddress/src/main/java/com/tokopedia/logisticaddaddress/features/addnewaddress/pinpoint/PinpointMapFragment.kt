package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
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
import com.tokopedia.design.component.Dialog
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.et_detail_address
import kotlinx.android.synthetic.main.form_add_new_address_data_item.*
import kotlinx.android.synthetic.main.fragment_pinpoint_map.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class PinpointMapFragment: BaseDaggerFragment(), PinpointMapListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, ResultCallback<LocationSettingsResult>,
        AutocompleteBottomSheetFragment.ActionListener {

    private var googleMap: GoogleMap? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>
    val handler = Handler()
    private var unnamedRoad: String = "Unnamed Road"
    private var isShowingAutocomplete: Boolean? = null
    private var isGetDistrict = false
    private val FINISH_FLAG = 1212
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"

    @Inject
    lateinit var presenter: PinpointMapPresenter

    override fun getScreenName(): String {
        return PinpointMapFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@PinpointMapFragment)
            presenter.attachView(this@PinpointMapFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(extra: Bundle): PinpointMapFragment {
            return PinpointMapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(AddressConstants.EXTRA_LAT, extra.getDouble(AddressConstants.EXTRA_LAT))
                    putDouble(AddressConstants.EXTRA_LONG, extra.getDouble(AddressConstants.EXTRA_LONG))
                    putBoolean(AddressConstants.EXTRA_SHOW_AUTOCOMPLETE, extra.getBoolean(AddressConstants.EXTRA_SHOW_AUTOCOMPLETE))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currentLat = arguments?.getDouble(AddressConstants.EXTRA_LAT)
            currentLong = arguments?.getDouble(AddressConstants.EXTRA_LONG)
            isShowingAutocomplete = arguments?.getBoolean(AddressConstants.EXTRA_SHOW_AUTOCOMPLETE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pinpoint_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
        presenter.connectGoogleApi(this)

        bottomSheetBehavior = BottomSheetBehavior.from<CoordinatorLayout>(bottomsheet_getdistrict)
        getdistrict_container.visibility = View.GONE
        invalid_container.visibility = View.GONE
        whole_loading_container.visibility = View.VISIBLE

        back_button.setOnClickListener {
            map_view?.onPause()
            presenter.disconnectGoogleApi()
            activity?.finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        MapsInitializer.initialize(activity!!)
        moveMap(PinpointMapUtils.generateLatLng(currentLat, currentLong))

        // nanti cek permission location di sini!
        if (isShowingAutocomplete == true) {
            handler.postDelayed({
                showAutocompleteGeocodeBottomSheet()
            }, 1500)
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        this.googleMap?.setOnCameraIdleListener {
            if (!isGetDistrict) {
                val target: LatLng? = this.googleMap?.cameraPosition?.target
                val latTarget = target?.latitude
                val longTarget = target?.longitude

                getdistrict_container.visibility = View.GONE
                invalid_container.visibility = View.GONE
                whole_loading_container.visibility = View.VISIBLE

                presenter.clearCacheAutofill()
                presenter.autofill("$latTarget,$longTarget")
            }
            isGetDistrict = false
        }
    }

    override fun moveMap(latLng: LatLng) {
        println("## masuk moveMap - lat = ${latLng.latitude}, long = ${latLng.longitude}")
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(15f)
                .bearing(0f)
                .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        map_view?.onResume()
        super.onResume()
    }

    override fun onPause() {
        map_view?.onPause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.disconnectGoogleApi()
    }

    override fun onDestroy() {
        map_view?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun onConnected(bundle: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onResult(locationSettingsResult: LocationSettingsResult) {
    }

    private fun showAutocompleteGeocodeBottomSheet() {
        val autocompleteGeocodeBottomSheetFragment =
                AutocompleteBottomSheetFragment.newInstance()
        autocompleteGeocodeBottomSheetFragment.setActionListener(this)
        autocompleteGeocodeBottomSheetFragment.show(fragmentManager, "")
    }

    override fun onGetPlaceId(placeId: String) {
        presenter.clearCacheGetDistrict()
        presenter.getDistrict(placeId)
    }

    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        invalid_container.visibility = View.GONE
        whole_loading_container.visibility = View.GONE
        getdistrict_container.visibility = View.VISIBLE
        currentLat = getDistrictDataUiModel.latitude.toDouble()
        currentLong = getDistrictDataUiModel.longitude.toDouble()
        isGetDistrict = true
        moveMap(PinpointMapUtils.generateLatLng(currentLat, currentLong))
        updateGetDistrictBottomSheet(presenter.convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel))
    }

    override fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
        invalid_container.visibility = View.GONE
        whole_loading_container.visibility = View.GONE
        getdistrict_container.visibility = View.VISIBLE
        updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel))
    }

    private fun updateGetDistrictBottomSheet(saveAddressDataModel: SaveAddressDataModel) {
        if (saveAddressDataModel.title.equals(unnamedRoad, true)) {
            whole_loading_container.visibility = View.GONE
            getdistrict_container.visibility = View.GONE
            invalid_container.visibility = View.VISIBLE

            invalid_title.text = getString(R.string.invalid_title)
            invalid_desc.text = saveAddressDataModel.formattedAddress

        } else {
            invalid_container.visibility = View.GONE
            whole_loading_container.visibility = View.GONE
            getdistrict_container.visibility = View.VISIBLE

            tv_title_getdistrict.text = saveAddressDataModel.title
            tv_address_getdistrict.text = saveAddressDataModel.formattedAddress
            btn_choose_location.setOnClickListener{
                saveAddressDataModel.editDetailAddress = et_detail_address.text.toString()
                presenter.loadAddEdit()
            }
        }
    }

    override fun showFailedDialog() {
        val tkpdDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        tkpdDialog.setTitle(getString(R.string.mismatch_title))
        tkpdDialog.setDesc(getString(R.string.mismatch_desc))
        tkpdDialog.setBtnOk(getString(R.string.mismatch_btn_title))
        tkpdDialog.setOnOkClickListener {
            goToAddEditActivity()
            tkpdDialog.dismiss()
        }
        tkpdDialog.show()
    }

    override fun goToAddEditActivity() {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(AddressConstants.EXTRA_IS_MISMATCH, false)
        intent.putExtra(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, presenter.getSaveAddressDataModel())
        startActivityForResult(intent, FINISH_FLAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == FINISH_FLAG){
            if (data != null && data.hasExtra(EXTRA_ADDRESS_NEW)) {
                val newAddress = data.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
                finishActivity(newAddress)
            }
        }
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        val intent = activity?.intent
        intent?.putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}