package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Build
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
import com.google.android.gms.location.*
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
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressComponent
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.et_detail_address
import kotlinx.android.synthetic.main.fragment_pinpoint_map.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class PinpointMapFragment : BaseDaggerFragment(), PinpointMapListener, OnMapReadyCallback,
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
    private val EXTRA_DETAIL_ADDRESS_LATEST = "EXTRA_DETAIL_ADDRESS_LATEST"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var token: Token? = null
    private var isPolygon: Boolean? = null
    private var districtId: Int? = null
    private val GREEN_ARGB = 0x40388E3C
    private var isMismatchSolved: Boolean? = null
    private var zipCodes: MutableList<String>? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    protected var addNewAddressComponent: AddNewAddressComponent? = null
    private var isChangesRequested: Boolean? = null
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private val SCREEN_NAME = "/user/address/create/cart/pinpoint1"

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
            presenter.setPermissionChecker(permissionCheckerHelper)
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
                    putBoolean(AddressConstants.EXTRA_IS_CHANGES_REQUESTED, extra.getBoolean(AddressConstants.EXTRA_IS_CHANGES_REQUESTED))
                }
                permissionCheckerHelper = PermissionCheckerHelper()
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
            isChangesRequested = arguments?.getBoolean(AddressConstants.EXTRA_IS_CHANGES_REQUESTED)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pinpoint_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareMap(savedInstanceState)
        prepareLayout()
        setViewListener()
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
    }

    private fun prepareLayout() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet_getdistrict)
        getdistrict_container.visibility = View.GONE
        invalid_container.visibility = View.GONE
        whole_loading_container.visibility = View.VISIBLE
        // et_detail_address.setText(saveAddressDataModel?.editDetailAddress)
    }

    private fun setViewListener() {
        back_button.setOnClickListener {
            map_view?.onPause()
            AddNewAddressAnalytics.eventClickBackArrowOnPinPoint()
            activity?.finish()
        }

        ic_search_btn.setOnClickListener {
            currentLat?.let { it1 -> currentLong?.let { it2 -> showAutocompleteGeocodeBottomSheet(it1, it2) } }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        activity?.let { MapsInitializer.initialize(activity) }
        moveMap(AddNewAddressUtils.generateLatLng(currentLat, currentLong))

        this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                districtId?.let { districtId ->
                    token?.let { token ->
                        presenter.getDistrictBoundary(districtId, token.districtRecommendation, token.ut)
                    }
                }
            }
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        this.googleMap?.setOnCameraMoveListener { onMapDraggedListener() }
        this.googleMap?.setOnCameraMoveStartedListener { onMapDraggedListener() }
    }

    private fun onMapDraggedListener() {
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

    /*override fun loadPoiList(lat: Double, long: Double) {
        autocompleteGeocodeBottomSheetFragment.loadAutocompleteGeocode(lat, long)
    }*/

    override fun showAutoComplete(lat: Double, long: Double) {
        if (isShowingAutocomplete == true) {
            handler.postDelayed({
                showAutocompleteGeocodeBottomSheet(lat, long)
            }, 1000)
        }
    }

    private fun moveMap(latLng: LatLng) {
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
        presenter.requestLocation(requireActivity())
        super.onResume()
    }

    override fun onPause() {
        map_view?.onPause()
        super.onPause()
    }

    override fun onStop() {
        map_view?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        map_view?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    private fun showAutocompleteGeocodeBottomSheet(lat: Double, long: Double) {
        activity?.let { AddNewAddressAnalytics.sendScreenName(it, SCREEN_NAME) }
        val autocompleteGeocodeBottomSheetFragment =
                AutocompleteBottomSheetFragment.newInstance(lat, long)
        autocompleteGeocodeBottomSheetFragment.setActionListener(this)
        autocompleteGeocodeBottomSheetFragment.show(fragmentManager, "")
        isShowingAutocomplete = false
    }

    override fun onGetPlaceId(placeId: String) {
        presenter.clearCacheGetDistrict()
        presenter.getDistrict(placeId)
    }

    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        invalid_container.visibility = View.GONE

        currentLat = getDistrictDataUiModel.latitude.toDouble()
        currentLong = getDistrictDataUiModel.longitude.toDouble()
        isGetDistrict = true
        moveMap(AddNewAddressUtils.generateLatLng(currentLat, currentLong))

        whole_loading_container.visibility = View.GONE
        getdistrict_container.visibility = View.VISIBLE
        updateGetDistrictBottomSheet(presenter.convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel, zipCodes))
    }

    override fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
        isShowingAutocomplete?.let {
            if (it) {
                handler.postDelayed({
                    updateAfterOnSuccessAutofill(autofillDataUiModel)
                }, 2000)
            } else {
                updateAfterOnSuccessAutofill(autofillDataUiModel)
            }
        }
    }

    private fun updateAfterOnSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
        invalid_container.visibility = View.GONE
        whole_loading_container.visibility = View.GONE
        getdistrict_container.visibility = View.VISIBLE

        this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                if (autofillDataUiModel.districtId != districtId) {
                    view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                    AddNewAddressAnalytics.eventViewToasterAlamatTidakSesuaiDenganPeta()
                } else {
                    updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
                }
            } else {
                updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
            }
        }
    }

    private fun updateGetDistrictBottomSheet(saveAddressDataModel: SaveAddressDataModel) {
        this.saveAddressDataModel = saveAddressDataModel
        if (saveAddressDataModel.title.equals(unnamedRoad, true)) {
            whole_loading_container.visibility = View.GONE
            getdistrict_container.visibility = View.GONE
            invalid_container.visibility = View.VISIBLE

            invalid_title.text = getString(R.string.invalid_title)
            invalid_desc.text = saveAddressDataModel.formattedAddress
            AddNewAddressAnalytics.eventViewErrorAlamatTidakValid()

        } else {
            invalid_container.visibility = View.GONE
            whole_loading_container.visibility = View.GONE
            getdistrict_container.visibility = View.VISIBLE

            ic_search_btn.setOnClickListener {
                currentLat?.let { it1 -> currentLong?.let { it2 -> showAutocompleteGeocodeBottomSheet(it1, it2) } }
                AddNewAddressAnalytics.eventClickFieldCariLokasi()
            }

            et_detail_address.setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldDetailAlamat()
            }

            tv_title_getdistrict.text = saveAddressDataModel.title
            tv_address_getdistrict.text = saveAddressDataModel.formattedAddress
            btn_choose_location.setOnClickListener {
                isMismatchSolved?.let {
                    if (it) {
                        doLoadAddEdit()
                    } else {
                        if (validateDetailAlamat()) {
                            doLoadAddEdit()
                        }
                    }
                }
                AddNewAddressAnalytics.eventClickButtonPilihLokasi()
            }
        }
    }

    private fun doLoadAddEdit() {
        saveAddressDataModel?.editDetailAddress = et_detail_address.text.toString()
        /*this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                isMismatchSolved = true
            }
        }*/

        presenter.loadAddEdit( isMismatchSolved, isChangesRequested)
    }

    private fun validateDetailAlamat(): Boolean {
        var validate = true

        if (et_detail_address.text.isEmpty()) {
            validate = false
            view?.let { activity?.let { it1 -> AddNewAddressUtils.showToastError(getString(R.string.validate_detail_alamat), it, it1) } }
        }

        return validate
    }

    override fun showFailedDialog() {
        val tkpdDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        tkpdDialog.setTitle(getString(R.string.mismatch_title))
        tkpdDialog.setDesc(getString(R.string.mismatch_desc))
        tkpdDialog.setBtnOk(getString(R.string.mismatch_btn_title))
        tkpdDialog.setOnOkClickListener {
            tkpdDialog.dismiss()
            goToAddEditActivity(true, false)
        }
        tkpdDialog.show()
        AddNewAddressAnalytics.eventViewFailedPinPointNotification()
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean) {
        Intent(context, AddEditAddressActivity::class.java).apply {
            putExtra(AddressConstants.EXTRA_IS_MISMATCH, isMismatch)
            putExtra(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, presenter.getSaveAddressDataModel())
            putExtra(AddressConstants.KERO_TOKEN, token)
            putExtra(AddressConstants.EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
            startActivityForResult(this, FINISH_FLAG)
        }
    }

    override fun finishBackToAddEdit(isMismatch: Boolean, isMismatchSolved: Boolean) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(AddressConstants.EXTRA_IS_MISMATCH, isMismatch)
                putExtra(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, presenter.getSaveAddressDataModel())
                putExtra(AddressConstants.KERO_TOKEN, token)
                putExtra(AddressConstants.EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
            })
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FINISH_FLAG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(EXTRA_ADDRESS_NEW)) {
                    val newAddress = data.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
                    finishActivity(newAddress)
                } else if (data.hasExtra(EXTRA_DETAIL_ADDRESS_LATEST)) {
                    val latestDetailAddress = data.getStringExtra(EXTRA_DETAIL_ADDRESS_LATEST)
                    et_detail_address.setText(latestDetailAddress)
                }
            }
        }
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
            })
            finish()
        }
    }

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper.onRequestPermissionsResult(it,
                        requestCode, permissions,
                        grantResults)
            }
        }
    }

    override fun useCurrentLocation(lat: Double?, long: Double?) {
        currentLat = lat
        currentLong = long
        val latLng = AddNewAddressUtils.generateLatLng(currentLat, currentLong)
        moveMap(latLng)
        presenter.clearCacheAutofill()
        presenter.autofill(latLng.toString())
    }
}