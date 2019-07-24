package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.Dialog
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.AddressConstants.MONAS_LAT
import com.tokopedia.logisticaddaddress.AddressConstants.MONAS_LONG
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressComponent
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
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
    private var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>? = null
    val handler = Handler()
    private var UNNAMED_ROAD: String = "Unnamed Road"
    private var isShowingAutocomplete: Boolean? = null
    private var isRequestingLocation: Boolean? = null
    private var isGetDistrict = false
    private val FINISH_FLAG = 1212
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private val EXTRA_DETAIL_ADDRESS_LATEST = "EXTRA_DETAIL_ADDRESS_LATEST"
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var token: Token? = null
    private var isPolygon: Boolean? = null
    private var districtId: Int? = null
    private val GREEN_ARGB = 0x40388E3C
    private var isMismatchSolved: Boolean? = null
    private var isMismatch: Boolean? = null
    private var zipCodes: MutableList<String>? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var addNewAddressComponent: AddNewAddressComponent? = null
    private var isChangesRequested: Boolean? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var continueWithLocation: Boolean? = false

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
                    putBoolean(AddressConstants.EXTRA_IS_MISMATCH, extra.getBoolean(AddressConstants.EXTRA_IS_MISMATCH))
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
            isMismatch = arguments?.getBoolean(AddressConstants.EXTRA_IS_MISMATCH)
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
        presenter.autofill("$currentLat,$currentLong")
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
    }

    private fun prepareLayout() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet_getdistrict)
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.VISIBLE
        isMismatch?.let {
            if (!it) et_detail_address?.setText(saveAddressDataModel?.editDetailAddress)
        }
    }

    private fun setViewListener() {
        back_button?.setOnClickListener {
            // hide keyboard
            val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
            (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.rootView?.windowToken, 0)

            map_view?.onPause()
            AddNewAddressAnalytics.eventClickBackArrowOnPinPoint()
            activity?.finish()
        }

        ic_search_btn?.setOnClickListener {
            currentLat?.let { it1 -> currentLong?.let { it2 -> showAutocompleteGeocodeBottomSheet(it1, it2, "") } }
        }

        et_detail_address?.run {
            setOnClickListener {
                getdistrict_container?.findViewById<ButtonCompat>(R.id.btn_choose_location)?.requestFocusFromTouch()
                getdistrict_container?.findViewById<EditText>(R.id.et_detail_address)?.requestFocusFromTouch()
            }

            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
        }

        ic_current_location?.setOnClickListener {
            AddNewAddressAnalytics.eventClickButtonPilihLokasi()
            doUseCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper?.checkPermissions(it, getPermissions(),
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper?.onPermissionDenied(it, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper?.onNeverAskAgain(it, permissionText)
                            }

                            override fun onPermissionGranted() {
                                googleMap?.isMyLocationEnabled = true
                            }

                        },
                        it.getString(R.string.rationale_need_location))
            } else {
                googleMap?.isMyLocationEnabled = true
            }
        }
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

        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior?.isHideable = false
        this.googleMap?.setOnCameraMoveListener { onMapDraggedListener() }
        this.googleMap?.setOnCameraIdleListener { onMapIdleListener() }
    }

    private fun onMapDraggedListener() {
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.VISIBLE
    }

    private fun onMapIdleListener() {
        if (!isGetDistrict) {
            val target: LatLng? = this.googleMap?.cameraPosition?.target
            val latTarget = target?.latitude
            val longTarget = target?.longitude

            presenter.clearCacheAutofill()
            presenter.autofill("$latTarget,$longTarget")
        } else {
            whole_loading_container?.visibility = View.GONE
            invalid_container?.visibility = View.GONE
            getdistrict_container?.visibility = View.VISIBLE
        }
        isGetDistrict = false
    }

    override fun showAutoComplete(lat: Double, long: Double) {
        if (isShowingAutocomplete == true) {
            handler.postDelayed({
                showAutocompleteGeocodeBottomSheet(lat, long, "")
            }, 500)
        }

        if (lat == 0.0 && long == 0.0) {
            currentLat = AddressConstants.MONAS_LAT
            currentLong = AddressConstants.MONAS_LONG
        } else {
            currentLat = lat
            currentLong = long
        }
        moveMap(AddNewAddressUtils.generateLatLng(currentLat, currentLong))
    }

    private fun moveMap(latLng: LatLng) {
        println("## masuk moveMap - lat = ${latLng.latitude}, long = ${latLng.longitude}")
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(16f)
                .build()

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            if ((currentLat == 0.0 && currentLong == 0.0) || currentLat == MONAS_LAT && currentLong == MONAS_LONG) {
                presenter.requestLocation(requireActivity())
            }
            ic_current_location.setImageResource(R.drawable.ic_gps_enable)
        } else {
            ic_current_location.setImageResource(R.drawable.ic_gps_disable)
        }
    }

    private fun doUseCurrentLocation() {
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            activity?.let { presenter.requestLocation(it) }
        } else {
            showLocationInfoBottomSheet()
        }
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    private fun showAutocompleteGeocodeBottomSheet(lat: Double, long: Double, search: String) {
        val autocompleteGeocodeBottomSheetFragment =
                AutocompleteBottomSheetFragment.newInstance(lat, long, search)
        autocompleteGeocodeBottomSheetFragment.setActionListener(this)
        autocompleteGeocodeBottomSheetFragment.show(fragmentManager, "")
        isShowingAutocomplete = false
    }

    override fun onGetPlaceId(placeId: String) {

        presenter.clearCacheGetDistrict()
        presenter.getDistrict(placeId)
    }

    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        invalid_container?.visibility = View.GONE

        currentLat = getDistrictDataUiModel.latitude.toDouble()
        currentLong = getDistrictDataUiModel.longitude.toDouble()
        isGetDistrict = true
        moveMap(AddNewAddressUtils.generateLatLng(currentLat, currentLong))

        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.VISIBLE
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
        invalid_container?.visibility = View.GONE
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.VISIBLE

        this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                if (autofillDataUiModel.districtId != districtId) {
                    continueWithLocation = false
                    view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                    AddNewAddressAnalytics.eventViewToasterAlamatTidakSesuaiDenganPeta()
                } else {
                    continueWithLocation = true
                    updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
                }
            } else {
                continueWithLocation = true
                updateGetDistrictBottomSheet(presenter.convertAutofillToSaveAddressDataUiModel(autofillDataUiModel, zipCodes))
            }
        }
    }

    override fun showInvalidDialog(error: String) {
        whole_loading_container?.visibility = View.GONE
        getdistrict_container?.visibility = View.GONE
        invalid_container?.visibility = View.VISIBLE

        if (error == "out_of_indonesia") {
            invalid_title?.text = getString(R.string.out_of_indonesia_title)
            invalid_desc?.text = getString(R.string.out_of_indonesia_desc)
            invalid_img?.setImageResource(R.drawable.tokopedia_out_of_indonesia)

            invalid_ic_search_btn?.setOnClickListener {
                currentLat?.let { it1 -> currentLong?.let { it2 -> showAutocompleteGeocodeBottomSheet(it1, it2, "") } }
            }
        }
    }

    private fun updateGetDistrictBottomSheet(saveAddressDataModel: SaveAddressDataModel) {
        this.saveAddressDataModel = saveAddressDataModel
        if (saveAddressDataModel.title.equals(UNNAMED_ROAD, true)) {
            whole_loading_container?.visibility = View.GONE
            getdistrict_container?.visibility = View.GONE
            invalid_container?.visibility = View.VISIBLE

            invalid_title?.text = getString(R.string.invalid_title)
            invalid_desc?.text = saveAddressDataModel.formattedAddress
            invalid_img?.setImageResource(R.drawable.ic_invalid_location)

            invalid_ic_search_btn?.setOnClickListener {
                currentLat?.let { it1 -> currentLong?.let { it2 -> showAutocompleteGeocodeBottomSheet(it1, it2, "") } }
            }
            AddNewAddressAnalytics.eventViewErrorAlamatTidakValid()

        } else {
            invalid_container?.visibility = View.GONE
            whole_loading_container?.visibility = View.GONE
            getdistrict_container?.visibility = View.VISIBLE

            ic_search_btn?.setOnClickListener {
                AddNewAddressAnalytics.eventClickMagnifier()
                showAutoCompleteBottomSheet("")
            }

            et_detail_address?.apply {
                setOnClickListener { AddNewAddressAnalytics.eventClickFieldDetailAlamat() }
                setupClearButtonWithAction()
                addTextChangedListener(setDetailAlamatWatcher())
            }

            tv_title_getdistrict?.apply {
                text = saveAddressDataModel.title
                setOnClickListener {
                    AddNewAddressAnalytics.eventClickFieldCariLokasi()
                    showAutoCompleteBottomSheet(saveAddressDataModel.title)
                }
            }

            tv_address_getdistrict?.apply {
                text = saveAddressDataModel.formattedAddress
                setOnClickListener {
                    AddNewAddressAnalytics.eventClickFieldCariLokasi()
                    showAutoCompleteBottomSheet(saveAddressDataModel.title)
                }
            }

            btn_choose_location?.setOnClickListener {
                continueWithLocation?.let {
                    if (it) {
                        doLoadAddEdit()
                        AddNewAddressAnalytics.eventClickButtonPilihLokasi()
                    } else {
                        view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.invalid_district), it1, it2) } }
                    }
                }
            }
        }
    }


    private fun showAutoCompleteBottomSheet(searchStr: String) {
        currentLat?.let { it1 -> currentLong?.let { it2 -> showAutocompleteGeocodeBottomSheet(it1, it2, searchStr) } }
    }

    private fun doLoadAddEdit() {
        saveAddressDataModel?.editDetailAddress = et_detail_address.text.toString()
        this.isPolygon?.let {
            if (this.isPolygon as Boolean) {
                isMismatchSolved = true
            }
        }

        presenter.loadAddEdit(isMismatchSolved, isChangesRequested)
    }

    override fun showFailedDialog() {
        val tkpdDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        tkpdDialog.setTitle(getString(R.string.mismatch_title))
        tkpdDialog.setDesc(getString(R.string.mismatch_desc))
        tkpdDialog.setBtnOk(getString(R.string.mismatch_btn_title))
        tkpdDialog.setOnOkClickListener {
            tkpdDialog.dismiss()
            goToAddEditActivity(isMismatch = true, isMismatchSolved = false)
        }
        tkpdDialog.show()
        AddNewAddressAnalytics.eventViewFailedPinPointNotification()
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean) {
        Intent(context, AddEditAddressActivity::class.java).apply {
            if (isMismatch && !isMismatchSolved) {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
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
                permissionCheckerHelper?.onRequestPermissionsResult(it,
                        requestCode, permissions,
                        grantResults)
            }
        }
    }

    override fun useCurrentLocation() {
        doUseCurrentLocation()
    }

    private fun EditText.setupClearButtonWithAction() {

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_close_btn else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun showLocationInfoBottomSheet() {
        val locationInfoBottomSheetFragment = LocationInfoBottomSheetFragment.newInstance()
        locationInfoBottomSheetFragment.show(fragmentManager, "")
    }

    private fun setDetailAlamatWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    val countCharLeft = 60 - s.toString().length
                    when (countCharLeft) {
                        60 -> {
                            tv_detail_address_counter.text = "0/60"
                        }
                        else -> {
                            tv_detail_address_counter.text = "$countCharLeft/60"
                        }
                    }
                } else {
                    tv_detail_address_counter.text = "60/60"
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
    }
}