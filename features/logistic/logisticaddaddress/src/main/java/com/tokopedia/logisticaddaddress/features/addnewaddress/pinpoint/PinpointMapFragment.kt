package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressComponent
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.et_detail_address
import kotlinx.android.synthetic.main.fragment_pinpoint_map.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class PinpointMapFragment: BaseDaggerFragment(), PinpointMapListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, ResultCallback<LocationSettingsResult>,
        AutocompleteBottomSheetFragment.ActionListener, HasComponent<AddNewAddressComponent> {
    private var googleMap: GoogleMap? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>
    val handler = Handler()
    private var unnamedRoad: String = "Unnamed Road"
    private var isShowingAutocomplete: Boolean? = null
    private var isRequestingLocation: Boolean? = null
    private var isGetDistrict = false
    private val FINISH_FLAG = 1212
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var permissionCheckerHelper = PermissionCheckerHelper()
    private var token: Token? = null
    private var isPolygon: Boolean? = null
    private var districtId: Int? = null
    private val GREEN_ARGB = 0x40388E3C
    private var isMismatchSolved: Boolean? = null
    private var zipCodes : MutableList<String>? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    protected var addNewAddressComponent: AddNewAddressComponent? = null

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
                    putBoolean(AddressConstants.EXTRA_REQUEST_LOCATION, extra.getBoolean(AddressConstants.EXTRA_REQUEST_LOCATION))
                    putParcelable(AddressConstants.KERO_TOKEN, extra.getParcelable(AddressConstants.KERO_TOKEN))
                    putBoolean(AddressConstants.EXTRA_IS_POLYGON, extra.getBoolean(AddressConstants.EXTRA_IS_POLYGON))
                    putInt(AddressConstants.EXTRA_DISTRICT_ID, extra.getInt(AddressConstants.EXTRA_DISTRICT_ID))
                    putBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED, extra.getBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED))
                    putParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            token = arguments?.getParcelable(AddressConstants.KERO_TOKEN)
            currentLat = arguments?.getDouble(AddressConstants.EXTRA_LAT)
            currentLong = arguments?.getDouble(AddressConstants.EXTRA_LONG)
            isShowingAutocomplete = arguments?.getBoolean(AddressConstants.EXTRA_SHOW_AUTOCOMPLETE)
            isRequestingLocation = arguments?.getBoolean(AddressConstants.EXTRA_REQUEST_LOCATION)
            isPolygon = arguments?.getBoolean(AddressConstants.EXTRA_IS_POLYGON)
            districtId = arguments?.getInt(AddressConstants.EXTRA_DISTRICT_ID)
            isMismatchSolved = arguments?.getBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED)
            saveAddressDataModel = arguments?.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL)
            zipCodes = saveAddressDataModel?.zipCodes?.toMutableList()
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
        et_detail_address.setText(saveAddressDataModel?.editDetailAddress)

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

        this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                districtId?.let { districtId ->
                    token?.let { token ->
                        presenter.getDistrictBoundary(districtId, token.districtRecommendation, token.ut)
                    }
                }
            }
        }

        // nanti cek permission location di sini!
        if (isShowingAutocomplete == true) {
            handler.postDelayed({
                showAutocompleteGeocodeBottomSheet(latitude, longitude)
            }, 1500)
        }
        //
        // requestLocation()

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

    /*private fun requestLocation() {
        val locationDetectorHelper = activity?.applicationContext?.let {
            LocationDetectorHelper(
                    permissionCheckerHelper,
                    LocationServices.getFusedLocationProviderClient(it),
                    it) }

        activity?.let {
            locationDetectorHelper?.getLocation(onGetLocation(), it,
                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                    it.getString(R.string.rationale_need_location))
        }
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return (deviceLocation) -> {
            println("## lat = ${it.latitude}, long = ${it.longitude}")
        }
        *//*return { (latitude, longitude) ->
            // getCampaign(latitude, longitude)
        }*//*
    }*/

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

    private fun showAutocompleteGeocodeBottomSheet(lat: Double, long: Double) {
        val autocompleteGeocodeBottomSheetFragment =
                AutocompleteBottomSheetFragment.newInstance(lat, long)
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
        updateGetDistrictBottomSheet(presenter.convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel, zipCodes))
    }

    override fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
        invalid_container.visibility = View.GONE
        whole_loading_container.visibility = View.GONE
        getdistrict_container.visibility = View.VISIBLE

        this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                if (autofillDataUiModel.districtId != districtId) {
                    showToastError(getString(R.string.invalid_district))
                } else {
                    updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
                }
            } else {
                updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
            }
        }
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
                this.isPolygon?.let {
                    if (this.isPolygon as Boolean) {
                        isMismatchSolved = true
                    }
                }
                presenter.loadAddEdit(isMismatchSolved)
            }
        }
    }

    private fun showToastError(message: String) {
        var msg = message
        if (view != null && activity != null) {
            if (message.isEmpty()) {
                msg = getString(R.string.default_request_error_unknown)
            }
            val snackbar = Snackbar.make(view!!, msg, BaseToaster.LENGTH_SHORT)
            val snackbarTextView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            val snackbarActionButton = snackbar.view.findViewById<Button>(android.support.design.R.id.snackbar_action)
            snackbar.view.background = ContextCompat.getDrawable(view!!.context, com.tokopedia.design.R.drawable.bg_snackbar_error)
            snackbarTextView.setTextColor(ContextCompat.getColor(view!!.context, R.color.font_black_secondary_54))
            snackbarActionButton.setTextColor(ContextCompat.getColor(view!!.context, R.color.font_black_primary_70))
            snackbarTextView.maxLines = 5
            snackbar.setAction(getString(R.string.label_action_snackbar_close)) { }.show()
        }
    }

    override fun showFailedDialog() {
        val tkpdDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        tkpdDialog.setTitle(getString(R.string.mismatch_title))
        tkpdDialog.setDesc(getString(R.string.mismatch_desc))
        tkpdDialog.setBtnOk(getString(R.string.mismatch_btn_title))
        tkpdDialog.setOnOkClickListener {
            goToAddEditActivity(true, false)
            tkpdDialog.dismiss()
        }
        tkpdDialog.show()
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(AddressConstants.EXTRA_IS_MISMATCH, isMismatch)
        intent.putExtra(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, presenter.getSaveAddressDataModel())
        intent.putExtra(AddressConstants.KERO_TOKEN, token)
        intent.putExtra(AddressConstants.EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
        startActivityForResult(intent, FINISH_FLAG)
        activity?.finish()
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

    private fun checkPermissions() =
            context?.let { ActivityCompat.checkSelfPermission(it, ACCESS_COARSE_LOCATION) } == PERMISSION_GRANTED

    override fun onSuccessGetDistrictBoundary(districtBoundaryGeometryUiModel: DistrictBoundaryGeometryUiModel) {
        this.googleMap?.addPolygon(PolygonOptions()
                .addAll(districtBoundaryGeometryUiModel.listCoordinates)
                .fillColor(GREEN_ARGB)
                .strokeWidth(3F))
    }

    override fun getComponent(): AddNewAddressComponent? {
        if (addNewAddressComponent == null) initInjector()
        return addNewAddressComponent
    }
}